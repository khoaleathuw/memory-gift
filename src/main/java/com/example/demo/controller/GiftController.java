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


    public GiftController(GiftService giftService){

        this.giftService = giftService;

    }



    // =========================
    // SAVE
    // =========================

    @PostMapping("/save/{uid}")
    public ResponseEntity<?> saveGift(

            @PathVariable String uid,

            @RequestParam(required=false)
            String message,

            @RequestParam(required=false)
            MultipartFile[] images,

            @RequestParam(required=false)
            MultipartFile audio

    ){

        try {


            Gift gift =
                    giftService.saveGift(
                            uid,
                            null,
                            message,
                            images,
                            audio
                    );


            return ResponseEntity.ok(gift);


        }catch(Exception e){

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        }

    }



    // =========================
    // UPDATE UID
    // =========================

    @PutMapping("/update/{uid}")
    public ResponseEntity<?> updateGift(

            @PathVariable String uid,

            @RequestParam(required=false)
            String message,

            @RequestParam(required=false)
            MultipartFile[] images,

            @RequestParam(required=false)
            MultipartFile audio,

            @RequestParam(defaultValue="false")
            boolean removeOldAudio,

            @RequestParam(required=false)
            List<String> deleteImages

    ){

        try{


            Gift gift =
                    giftService.updateGiftByUid(

                            uid,

                            null,

                            message,

                            images,

                            audio,

                            removeOldAudio,

                            deleteImages

                    );


            return ResponseEntity.ok(gift);



        }catch(Exception e){

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        }

    }



    // =========================
    // GET UID
    // =========================

    @GetMapping("/uid/{uid}")
    public ResponseEntity<?> getByUid(

            @PathVariable String uid

    ){


        Gift gift =
                giftService.getByUid(uid);



        if(gift == null){

            return ResponseEntity.notFound().build();

        }


        return ResponseEntity.ok(gift);

    }

}