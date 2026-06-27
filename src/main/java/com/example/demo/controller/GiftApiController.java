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
    ) throws Exception {

        Gift gift = new Gift();
        gift.setMessage(message);

        /* ================= AUDIO ================= */
        String voiceUrl = null;

        if (audio != null && !audio.isEmpty()) {

            String audioName = System.currentTimeMillis() + "_" + audio.getOriginalFilename();

            File audioDir = new File("uploads/audio/");
            if (!audioDir.exists()) audioDir.mkdirs();

            File audioFile = new File(audioDir, audioName);
            audio.transferTo(audioFile);

            voiceUrl = "/uploads/audio/" + audioName;
        }

        gift.setVoiceUrl(voiceUrl);

        /* ================= IMAGES ================= */
        List<GiftImage> imageList = new ArrayList<>();

        if (images != null) {
            for (MultipartFile file : images) {

                if (file.isEmpty()) continue;

                String imgName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                File imgDir = new File("uploads/images/");
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
    }
}