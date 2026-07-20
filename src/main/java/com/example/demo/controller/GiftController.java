package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gift")
@CrossOrigin("*")
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    // ==================================================
    // SAVE GIFT
    // ==================================================

    @PostMapping("/save/{uid}")
    public ResponseEntity<?> saveGift(

            @PathVariable String uid,

            @RequestParam(required = false)
            String message,

            @RequestParam(required = false)
            String theme,

            @RequestParam(required = false)
            MultipartFile[] images,

            @RequestParam(required = false)
            MultipartFile audio

    ) {

        try {

            Gift gift = giftService.saveGift(
                    uid,
                    theme,
                    message,
                    images,
                    audio
            );

            return ResponseEntity.ok(gift);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==================================================
    // UPDATE GIFT BY UID
    // ==================================================

    @PostMapping("/update/{uid}")
    public ResponseEntity<?> updateGift(

            @PathVariable String uid,

            @RequestParam(required = false)
            String title,

            @RequestParam(required = false)
            String message,

            @RequestParam(required = false)
            String theme,

            @RequestParam(required = false)
            MultipartFile[] images,

            @RequestParam(required = false)
            MultipartFile audio,

            @RequestParam(defaultValue = "false")
            boolean removeOldAudio,

            @RequestParam(required = false)
            List<String> deleteImages

    ) {

        try {

            Gift gift = giftService.updateGiftByUid(
                    uid,
                    title,
                    message,
                    theme,
                    images,
                    audio,
                    removeOldAudio,
                    deleteImages
            );

            return ResponseEntity.ok(gift);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==================================================
    // GET GIFT BY UID
    // ==================================================

    @GetMapping("/uid/{uid}")
    public ResponseEntity<?> getByUid(
            @PathVariable String uid
    ) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(gift);
    }
}