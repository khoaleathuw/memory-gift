package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gift")
public class GiftApiController {

    @Autowired
    private GiftRepository giftRepository;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveGift(
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) throws IOException {

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Gift gift = new Gift();
        gift.setMessage(message);

        // AUDIO
        if (audio != null && !audio.isEmpty()) {

            String fileName = System.currentTimeMillis()
                    + "_" + UUID.randomUUID()
                    + "_" + audio.getOriginalFilename();

            audio.transferTo(new File(uploadDir + fileName));

            gift.setVoiceUrl(fileName);
        }

        // IMAGES
        List<GiftImage> imageList = new ArrayList<>();

        if (images != null) {

            for (MultipartFile image : images) {

                if (image == null || image.isEmpty()) continue;

                String fileName = System.currentTimeMillis()
                        + "_" + UUID.randomUUID()
                        + "_" + image.getOriginalFilename();

                image.transferTo(new File(uploadDir + fileName));

                GiftImage img = new GiftImage();
                img.setImageUrl(fileName);
                img.setGift(gift);

                imageList.add(img);
            }
        }

        gift.setImages(imageList);

        // Lưu và lấy ID
        Gift savedGift = giftRepository.save(gift);

        // Trả về ID
        return ResponseEntity.ok(savedGift.getId().toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gift> getGift(@PathVariable Integer id) {

        Gift gift = giftRepository.findById(id).orElse(null);

        if (gift == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(gift);
    }

}