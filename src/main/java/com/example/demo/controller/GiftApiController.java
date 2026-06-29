package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.entity.NfcTag;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.NfcTagRepository;
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

    @Autowired
    private NfcTagRepository nfcTagRepository;

    private final String BASE_DIR = System.getProperty("user.home") + "/memory-gift/uploads";

    @PostMapping("/save")
    public ResponseEntity<?> saveGift(
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) MultipartFile audio,
            @RequestParam(required = false) String uid
    ) {
        return saveOrUpdate(null, message, images, audio, uid);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateGift(
            @PathVariable Long id,
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) MultipartFile audio,
            @RequestParam(required = false) String uid
    ) {
        return saveOrUpdate(id, message, images, audio, uid);
    }

    private ResponseEntity<?> saveOrUpdate(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio,
            String uid
    ) {
        try {
            Gift gift = (id != null)
                    ? giftRepository.findById(id).orElseThrow(() -> new RuntimeException("Gift not found"))
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

            // Lưu Gift trước để lấy được instance đã có ID (saved)
            Gift saved = giftRepository.save(gift);

            // ================= NFC TAG =================
            if (uid != null && !uid.isBlank()) {
                NfcTag tag = nfcTagRepository.findByUid(uid).orElse(null);
                if (tag != null) {
                    tag.setGift(saved);
                    nfcTagRepository.save(tag);
                }
            }

            return ResponseEntity.ok(saved.getId());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}