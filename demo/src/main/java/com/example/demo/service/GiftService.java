package com.example.demo.service;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.entity.NfcTag;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.NfcTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private NfcTagRepository nfcTagRepository;

    private final String BASE_DIR = System.getProperty("user.home") + "/memory-gift/uploads";

    /**
     * Toàn bộ method chạy trong MỘT transaction / MỘT persistence context.
     * -> gift luôn ở trạng thái "managed", Hibernate tự dirty-check và flush
     *    khi transaction commit, không cần gọi merge() theo kiểu detached-entity
     *    nữa -> tránh việc orphanRemoval xóa nhầm ảnh cũ khi chỉ sửa audio/message.
     */
    @Transactional
    public Gift saveOrUpdate(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio,
            String uid,
            boolean removeOldAudio,
            List<String> deleteImages
    ) throws IOException {

        Gift gift = (id != null)
                ? giftRepository.findByIdWithImages(id).orElseThrow(() -> new RuntimeException("Gift not found"))
                : new Gift();

        System.out.println(">>> [BEFORE] gift=" + id + " images=" + gift.getImages().size()
                + " deleteImages=" + deleteImages
                + " newImages=" + (images == null ? 0 : images.length));

        gift.setMessage(message);

        handleAudio(gift, audio, removeOldAudio);
        handleImages(gift, images, deleteImages);

        // gift đang managed trong transaction này -> save() ở đây thực chất
        // không bắt buộc (Hibernate sẽ tự flush), nhưng gọi cho rõ ràng và
        // để lấy lại instance/id khi tạo mới (id != null trường hợp new Gift()).
        giftRepository.flush();
        Gift saved = gift;

        System.out.println(">>> [AFTER] gift=" + saved.getId() + " images=" + saved.getImages().size());

        if (uid != null && !uid.isBlank()) {
            NfcTag tag = nfcTagRepository.findByUid(uid).orElse(null);
            if (tag != null) {
                tag.setGift(saved);
                nfcTagRepository.save(tag);
            }
        }

        return saved;
    }

    // ================= AUDIO =================
    private void handleAudio(Gift gift, MultipartFile audio, boolean removeOldAudio) throws IOException {

        boolean hasNewAudio = audio != null && !audio.isEmpty();

        if (hasNewAudio || removeOldAudio) {
            deleteOldAudioFile(gift);
            gift.setVoiceUrl(null);
        }

        if (hasNewAudio) {
            String originalName = audio.getOriginalFilename() != null ? audio.getOriginalFilename() : "audio";
            String audioName = UUID.randomUUID() + "_" + originalName;
            Path audioPath = Paths.get(BASE_DIR, "audio", audioName);
            Files.createDirectories(audioPath.getParent());
            audio.transferTo(audioPath.toFile());
            gift.setVoiceUrl("/files/audio/" + audioName);
        }
    }

    private void deleteOldAudioFile(Gift gift) {
        if (gift.getVoiceUrl() == null) return;
        try {
            String oldAudio = gift.getVoiceUrl().replace("/files/audio/", "");
            Path oldAudioPath = Paths.get(BASE_DIR, "audio", oldAudio);
            Files.deleteIfExists(oldAudioPath);
        } catch (Exception e) {
            System.out.println("Không xóa được audio cũ: " + e.getMessage());
        }
    }

    // ================= IMAGES =================
    private void handleImages(Gift gift, MultipartFile[] images, List<String> deleteImages) throws IOException {

        // 1) Xóa các ảnh bị chọn xóa riêng lẻ (nếu có), KHÔNG đụng tới ảnh còn lại
        if (deleteImages != null && !deleteImages.isEmpty()) {
            Iterator<GiftImage> it = gift.getImages().iterator();
            while (it.hasNext()) {
                GiftImage img = it.next();
                if (deleteImages.contains(img.getImageUrl())) {
                    deleteImageFile(img.getImageUrl());
                    it.remove(); // orphanRemoval sẽ xóa row trong DB khi flush
                }
            }
        }

        // 2) Thêm ảnh mới (nếu có) — KHÔNG xóa ảnh cũ còn lại, chỉ append
        if (images != null) {
            for (MultipartFile f : images) {
                if (f == null || f.isEmpty()) continue;

                String originalName = f.getOriginalFilename() != null ? f.getOriginalFilename() : "image";
                String imgName = UUID.randomUUID() + "_" + originalName;
                Path imgPath = Paths.get(BASE_DIR, "images", imgName);
                Files.createDirectories(imgPath.getParent());
                f.transferTo(imgPath.toFile());

                GiftImage img = new GiftImage();
                img.setImageUrl("/files/images/" + imgName);
                gift.addImage(img); // dùng helper để set 2 chiều cho đúng
            }
        }
    }

    private void deleteImageFile(String imageUrl) {
        try {
            String imgName = imageUrl.replace("/files/images/", "");
            Path imgPath = Paths.get(BASE_DIR, "images", imgName);
            Files.deleteIfExists(imgPath);
        } catch (Exception e) {
            System.out.println("Không xóa được ảnh cũ: " + e.getMessage());
        }
    }
}