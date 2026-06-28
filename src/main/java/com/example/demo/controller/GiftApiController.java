package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gift")
public class GiftApiController {

    @Autowired
    private GiftRepository giftRepository;

    private final String BASE_DIR = System.getProperty("user.home") + "/uploads";

    /* ================= CREATE ================= */
    @PostMapping("/save")
    public ResponseEntity<String> saveGift(
            @RequestParam("message") String message,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) {
        return saveOrUpdate(null, message, images, audio);
    }

    /* ================= UPDATE ================= */
    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateGift(
            @PathVariable Long id,
            @RequestParam("message") String message,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) {
        return saveOrUpdate(id, message, images, audio);
    }

    /* ================= CORE ================= */
    private ResponseEntity<String> saveOrUpdate(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio
    ) {
        try {

            /* ================= LOAD OR CREATE ================= */
            Gift gift;

            if (id != null) {
                gift = giftRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Gift not found"));
            } else {
                gift = new Gift();
                gift.setImages(new ArrayList<>());
            }

            gift.setMessage(message);

            /* ================= AUDIO (ONLY IF NEW UPLOAD) ================= */
            if (audio != null && !audio.isEmpty()) {

                String audioName = UUID.randomUUID() + "_" + audio.getOriginalFilename();

                Path audioDir = Paths.get(BASE_DIR, "audio");
                Files.createDirectories(audioDir);

                Path audioPath = audioDir.resolve(audioName);
                audio.transferTo(audioPath.toFile());

                gift.setVoiceUrl("/files/audio/" + audioName);
            }

            /* ================= IMAGES (APPEND NOT REPLACE) ================= */
            if (images != null && images.length > 0) {

                List<GiftImage> currentImages = gift.getImages();

                if (currentImages == null) {
                    currentImages = new ArrayList<>();
                }

                for (MultipartFile f : images) {

                    if (f == null || f.isEmpty()) continue;

                    String imgName = UUID.randomUUID() + "_" + f.getOriginalFilename();

                    Path imgDir = Paths.get(BASE_DIR, "images");
                    Files.createDirectories(imgDir);

                    Path imgPath = imgDir.resolve(imgName);
                    f.transferTo(imgPath.toFile());

                    GiftImage img = new GiftImage();
                    img.setImageUrl("/files/images/" + imgName);
                    img.setGift(gift);

                    currentImages.add(img);
                }

                gift.setImages(currentImages);
            }

            /* ================= SAVE ================= */
            Gift saved = giftRepository.save(gift);

            return ResponseEntity.ok(saved.getId().toString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}