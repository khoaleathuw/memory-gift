package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    private final String BASE_DIR = "uploads";

    @PostMapping("/save")
    public ResponseEntity<String> saveGift(
            @RequestParam("message") String message,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) {

        try {

            Gift gift = new Gift();
            gift.setMessage(message);

            /* ================= AUDIO ================= */
            if (audio != null && !audio.isEmpty()) {

                String audioName = UUID.randomUUID() + "_" + audio.getOriginalFilename();

                Path audioDir = Paths.get(BASE_DIR, "audio");
                Files.createDirectories(audioDir);

                Path audioPath = audioDir.resolve(audioName);
                audio.transferTo(audioPath);

                gift.setVoiceUrl("/files/audio/" + audioName);
            }

            /* ================= IMAGES ================= */
            List<GiftImage> list = new ArrayList<>();

            if (images != null) {
                for (MultipartFile f : images) {

                    if (f == null || f.isEmpty()) continue;

                    String imgName = UUID.randomUUID() + "_" + f.getOriginalFilename();

                    Path imgDir = Paths.get(BASE_DIR, "images");
                    Files.createDirectories(imgDir);

                    Path imgPath = imgDir.resolve(imgName);
                    f.transferTo(imgPath);

                    GiftImage img = new GiftImage();
                    img.setImageUrl("/files/images/" + imgName);
                    img.setGift(gift);

                    list.add(img);
                }
            }

            gift.setImages(list);

            Gift saved = giftRepository.save(gift);

            return ResponseEntity.ok(saved.getId().toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}