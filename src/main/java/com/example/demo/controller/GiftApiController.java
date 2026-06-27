package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/gift")
public class GiftApiController {

    @Autowired
    private GiftRepository giftRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveGift(
            @RequestParam("message") String message,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) {

        try {

            Gift gift = new Gift();
            gift.setMessage(message);

            /* ================= AUDIO (SAFE) ================= */
            if (audio != null && !audio.isEmpty()) {

                String original = audio.getOriginalFilename();
                if (original == null || original.isEmpty()) {
                    original = "voice.webm";
                }

                String audioName = System.currentTimeMillis() + "_" + original;

                File audioDir = new File("/tmp/audio/");
                if (!audioDir.exists()) audioDir.mkdirs();

                File audioFile = new File(audioDir, audioName);
                audio.transferTo(audioFile);

                gift.setVoiceUrl("/uploads/audio/" + audioName);
            }

            /* ================= IMAGES (SAFE) ================= */
            List<GiftImage> imageList = new ArrayList<>();

            if (images != null && images.length > 0) {

                for (MultipartFile file : images) {

                    if (file == null || file.isEmpty()) continue;

                    String originalName = file.getOriginalFilename();
                    if (originalName == null || originalName.isEmpty()) {
                        originalName = "image.jpg";
                    }

                    String imgName = System.currentTimeMillis() + "_" + originalName;

                    File imgDir = new File("/tmp/images/");
                    if (!imgDir.exists()) imgDir.mkdirs();

                    File dest = new File(imgDir, imgName);
                    file.transferTo(dest);

                    GiftImage img = new GiftImage();
                    img.setImageUrl("/uploads/images/" + imgName);
                    img.setGift(gift);

                    imageList.add(img);
                }
            }

            gift.setImages(imageList);

            /* ================= SAVE DB ================= */
            Gift saved = giftRepository.save(gift);

            return ResponseEntity.ok(saved.getId().toString());

        } catch (Exception e) {
            e.printStackTrace(); // quan trọng để debug Railway logs
            return ResponseEntity.status(500).body("ERROR: " + e.getMessage());
        }
    }
}