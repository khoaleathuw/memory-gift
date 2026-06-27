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

        /* ================= AUDIO ================= */
        if (audio != null && !audio.isEmpty()) {

            String audioName = System.currentTimeMillis() + "_" + audio.getOriginalFilename();

            String audioPath = System.getProperty("user.dir") + "/uploads/audio/";
            File audioDir = new File(audioPath);
            if (!audioDir.exists()) audioDir.mkdirs();

            File audioFile = new File(audioDir, audioName);
            audio.transferTo(audioFile);

            gift.setVoiceUrl("/uploads/audio/" + audioName);
        }

        /* ================= IMAGES ================= */
        List<GiftImage> list = new ArrayList<>();

        if (images != null) {
            for (MultipartFile f : images) {

                if (f == null || f.isEmpty()) continue;

                String imgName = System.currentTimeMillis() + "_" + f.getOriginalFilename();

                String imgPath = System.getProperty("user.dir") + "/uploads/images/";
                File imgDir = new File(imgPath);
                if (!imgDir.exists()) imgDir.mkdirs();

                File imgFile = new File(imgDir, imgName);
                f.transferTo(imgFile);

                GiftImage img = new GiftImage();
                img.setImageUrl("/uploads/images/" + imgName);
                img.setGift(gift);

                list.add(img);
            }
        }

        gift.setImages(list);

        /* ================= SAVE DB ================= */
        Gift saved = giftRepository.save(gift);

        return ResponseEntity.ok(saved.getId().toString());

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
}