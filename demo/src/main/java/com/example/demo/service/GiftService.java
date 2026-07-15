package com.example.demo.service;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftImageRepository;
import com.example.demo.repository.GiftRepository;
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

    private final String UPLOAD_DIR =
            System.getProperty("user.home") + "/memory-gift/uploads/";

    public GiftService(
            GiftRepository giftRepository,
            GiftImageRepository giftImageRepository
    ) {
        this.giftRepository = giftRepository;
        this.giftImageRepository = giftImageRepository;
    }

    // ==========================================
    // SAVE
    // ==========================================
    @Transactional
    public Gift saveGift(
            String message,
            MultipartFile[] images,
            MultipartFile audio,
            MultipartFile gif
    ) throws IOException {

        Gift gift = new Gift();
        gift.setMessage(message);

        // AUDIO
        if (audio != null && !audio.isEmpty()) {
            String path = saveFile(audio, "audio");
            gift.setVoiceUrl(path);
        }

        // GIF
        if (gif != null && !gif.isEmpty()) {
            String path = saveFile(gif, "gif");
            gift.setGifUrl(path);
        }

        // IMAGES
        if (images != null) {
            for (MultipartFile image : images) {

                if (image.isEmpty()) continue;

                GiftImage giftImage = new GiftImage();
                giftImage.setImageUrl(saveFile(image, "images"));

                gift.addImage(giftImage);
            }
        }

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
            MultipartFile gif,
            boolean removeOldAudio,
            List<String> deleteImages
    ) throws IOException {

        Gift gift = giftRepository.findById(id).orElseThrow();

        if (message != null) {
            gift.setMessage(message);
        }

        // AUDIO
        if (removeOldAudio) {
            gift.setVoiceUrl(null);
        }

        if (audio != null && !audio.isEmpty()) {
            String path = saveFile(audio, "audio");
            gift.setVoiceUrl(path);
        }

        // GIF
        if (gif != null && !gif.isEmpty()) {
            String path = saveFile(gif, "gif");
            gift.setGifUrl(path);
        }

        // DELETE IMAGES
        if (deleteImages != null && !deleteImages.isEmpty()) {
            gift.getImages().removeIf(
                    img -> deleteImages.contains(img.getImageUrl())
            );
        }

        // ADD NEW IMAGES
        if (images != null) {
            for (MultipartFile image : images) {

                if (image.isEmpty()) continue;

                GiftImage img = new GiftImage();
                img.setImageUrl(saveFile(image, "images"));

                gift.addImage(img);
            }
        }

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
    // SAVE FILE
    // ==========================================
    private String saveFile(MultipartFile file, String folder) throws IOException {

        Path dir = Paths.get(UPLOAD_DIR + folder);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        String filename =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                dir.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/files/" + folder + "/" + filename;
    }
}