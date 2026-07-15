package com.example.demo.service;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.GiftImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class GiftService {

    private final GiftRepository giftRepository;
    private final GiftImageRepository giftImageRepository;

    private final String UPLOAD_DIR = System.getProperty("user.home") + "/memory-gift/uploads/";

    public GiftService(
            GiftRepository giftRepository,
            GiftImageRepository giftImageRepository
    ) {
        this.giftRepository = giftRepository;
        this.giftImageRepository = giftImageRepository;
    }

    // ==========================================
    // SAVE (Đã tối ưu hóa: Chỉ save 1 lần duy nhất)
    // ==========================================
    @Transactional
    public Gift saveGift(
            String message,
            MultipartFile[] images,
            MultipartFile audio
    ) throws IOException {

        // 1. Tạo thực thể Gift mới (Chưa lưu DB)
        Gift gift = new Gift();
        gift.setMessage(message);

        // 2. Xử lý AUDIO nếu có
        if (audio != null && !audio.isEmpty()) {
            String path = saveFile(audio, "audio");
            gift.setVoiceUrl(path);
        }

        // 3. Xử lý IMAGES nếu có
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String path = saveFile(image, "images");

                    GiftImage giftImage = new GiftImage();
                    giftImage.setImageUrl(path);

                    // Thêm trực tiếp vào collection khi Gift chưa được lưu.
                    // CascadeType.ALL sẽ tự động cascade lưu cả GiftImage khi lưu Gift ở bước sau.
                    gift.addImage(giftImage);
                }
            }
        }

        // 4. Lưu một lần duy nhất.
        // Hibernate tự sinh: 1 câu INSERT Gift -> lấy ID -> n câu INSERT GiftImage.
        // Hoàn toàn KHÔNG có câu SELECT hoặc DELETE dư thừa nào phát sinh.
        return giftRepository.save(gift);
    }

    // ==========================================
    // UPDATE
    // ==========================================
    @Transactional
    public Gift updateGift(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio,
            boolean removeOldAudio,
            List<String> deleteImages
    ) throws IOException {

        // Chỉ load Gift đúng 1 lần cho toàn bộ thao tác update
        Gift gift = giftRepository.findById(id).orElseThrow();

        if (message != null) {
            gift.setMessage(message);
        }

        // XÓA AUDIO CŨ (nếu client yêu cầu)
        if (removeOldAudio) {
            gift.setVoiceUrl(null);
        }

        // AUDIO MỚI (ghi đè trực tiếp)
        if (audio != null && !audio.isEmpty()) {
            String path = saveFile(audio, "audio");
            gift.setVoiceUrl(path);
        }

        // XÓA ẢNH CŨ
        // Nhờ cấu hình orphanRemoval = true trên Gift.images,
        // Hibernate sẽ tự sinh câu DELETE các dòng bị remove khỏi collection khi flush transaction.
        if (deleteImages != null && !deleteImages.isEmpty()) {
            gift.getImages().removeIf(
                    img -> deleteImages.contains(img.getImageUrl())
            );
        }

        // THÊM ẢNH MỚI
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    GiftImage img = new GiftImage();
                    img.setImageUrl(saveFile(image, "images"));
                    gift.addImage(img);
                }
            }
        }

        // Thực thể "gift" đang được Hibernate quản lý (managed) trong Transaction.
        // Hibernate sẽ tự động so sánh trạng thái (Dirty-Checking) và đồng bộ (Flush) xuống DB khi commit.
        // Không cần thiết phải gọi giftRepository.save(gift) một lần nữa.
        return gift;
    }

    // ==========================================
    // GET
    // ==========================================
    @Transactional(readOnly = true)
    public Gift getById(Long id) {
        return giftRepository.findById(id).orElseThrow();
    }

    // ==========================================
    // SAVE FILE TO STORAGE
    // ==========================================
    private String saveFile(MultipartFile file, String folder) throws IOException {
        Path dir = Paths.get(UPLOAD_DIR + folder);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                dir.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/files/" + folder + "/" + filename;
    }
}