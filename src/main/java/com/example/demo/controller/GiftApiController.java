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

    private final String BASE_DIR =
            System.getProperty("user.home") + "/memory-gift/uploads";

    @PostMapping("/save")
    public ResponseEntity<?> saveGift(
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) MultipartFile audio
    ) {
        return saveOrUpdate(null, message, images, audio);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateGift(
            @PathVariable Long id,
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) MultipartFile audio
    ) {
        return saveOrUpdate(id, message, images, audio);
    }

    private ResponseEntity<?> saveOrUpdate(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio
    ) {
        try {

            Gift gift = (id != null)
                    ? giftRepository.findById(id).orElseThrow()
                    : new Gift();

            gift.setMessage(message);

            // ================= AUDIO =================
            if (audio != null && !audio.isEmpty()) {

                String audioName = UUID.randomUUID() + "_" + audio.getOriginalFilename();

                Path audioPath = Paths.get(BASE_DIR, "audio", audioName);
                Files.createDirectories(audioPath.getParent());

                audio.transferTo(audioPath.toFile());

                gift.setVoiceUrl("/files/audio/" + audioName);
            }

            // ================= IMAGES =================
            List<GiftImage> imgs = gift.getImages();
            if (imgs == null) imgs = new ArrayList<>();

            if (images != null) {
                for (MultipartFile f : images) {

                    if (f.isEmpty()) continue;

                    String imgName = UUID.randomUUID() + "_" + f.getOriginalFilename();

                    Path imgPath = Paths.get(BASE_DIR, "images", imgName);
                    Files.createDirectories(imgPath.getParent());

                    f.transferTo(imgPath.toFile());

                    GiftImage img = new GiftImage();
                    img.setImageUrl("/files/images/" + imgName);
                    img.setGift(gift);

                    imgs.add(img);
                }
            }

            gift.setImages(imgs);

            Gift saved = giftRepository.save(gift);

            return ResponseEntity.ok(saved.getId());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}