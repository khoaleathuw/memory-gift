package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/gift")
public class GiftApiController {

    @Autowired
    private GiftService giftService;

    @PostMapping("/save")
    public ResponseEntity<?> saveGift(
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) MultipartFile audio,
            @RequestParam(required = false) String uid
    ) {
        return handle(null, message, images, audio, uid, false, Collections.emptyList());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateGift(
            @PathVariable Long id,
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) MultipartFile audio,
            @RequestParam(required = false) String uid,
            @RequestParam(required = false, defaultValue = "false") boolean removeOldAudio,
            @RequestParam(required = false) List<String> deleteImages
    ) {
        return handle(id, message, images, audio, uid, removeOldAudio,
                deleteImages != null ? deleteImages : Collections.emptyList());
    }

    private ResponseEntity<?> handle(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio,
            String uid,
            boolean removeOldAudio,
            List<String> deleteImages
    ) {
        try {
            Gift saved = giftService.saveOrUpdate(id, message, images, audio, uid, removeOldAudio, deleteImages);
            return ResponseEntity.ok(saved.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}