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
                ? giftRepository.findById(id).orElseThrow(() -> new RuntimeException("Gift not found"))
                : new Gift();

        System.out.println("==================================");
        System.out.println("Loaded images = " + gift.getImages().size());

        gift.setMessage(message);

        handleAudio(gift, audio, removeOldAudio);

        System.out.println("Before handleImages = " + gift.getImages().size());

        handleImages(gift, images, deleteImages);

        System.out.println("After handleImages = " + gift.getImages().size());

        Gift saved = giftRepository.save(gift);

        System.out.println("After save = " + saved.getImages().size());

        if (uid != null && !uid.isBlank()) {
            NfcTag tag = nfcTagRepository.findByUid(uid).orElse(null);
            if (tag != null) {
                tag.setGift(saved);
                nfcTagRepository.save(tag);
            }
        }

        System.out.println("==================================");

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
            String audioName = UUID.randomUUID() + "_" + audio.getOriginalFilename();
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

        System.out.println("deleteImages = " + deleteImages);
        System.out.println("images = " + (images == null ? "null" : images.length));
        System.out.println("Current images before = " + gift.getImages().size());

        // Xóa ảnh
        if (deleteImages != null && !deleteImages.isEmpty()) {

            Iterator<GiftImage> it = gift.getImages().iterator();

            while (it.hasNext()) {
                GiftImage img = it.next();

                if (deleteImages.contains(img.getImageUrl())) {

                    System.out.println("Delete image: " + img.getImageUrl());

                    deleteImageFile(img.getImageUrl());
                    it.remove();
                }
            }
        }

        // Thêm ảnh
        if (images != null) {

            for (MultipartFile f : images) {

                if (f == null || f.isEmpty()) {
                    continue;
                }

                String imgName = UUID.randomUUID() + "_" + f.getOriginalFilename();
                Path imgPath = Paths.get(BASE_DIR, "images", imgName);

                Files.createDirectories(imgPath.getParent());
                f.transferTo(imgPath.toFile());

                GiftImage img = new GiftImage();
                img.setImageUrl("/files/images/" + imgName);

                gift.addImage(img);

                System.out.println("Add image: " + img.getImageUrl());
            }
        }

        System.out.println("Current images after = " + gift.getImages().size());
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