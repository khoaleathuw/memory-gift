package com.example.demo.controller;


import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/gift")
public class GiftController {


    private final GiftService giftService;


    public GiftController(GiftService giftService){
        this.giftService = giftService;
    }



    // ============================
    // CREATE
    // ============================

    @PostMapping(
            value = "/save",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> saveGift(


            @RequestParam(value = "message", required = false)
            String message,


            @RequestParam(value = "images", required = false)
            MultipartFile[] images,


            @RequestParam(value = "audio", required = false)
            MultipartFile audio

    ){


        System.out.println("======================");
        System.out.println("SAVE GIFT");

        System.out.println("MESSAGE = " + message);



        if(images != null){

            System.out.println(
                    "IMAGE COUNT = " + images.length
            );

        }


        if(audio != null){

            System.out.println(
                    "AUDIO = " + audio.getOriginalFilename()
            );

        }



        try{


            Gift gift =
                    giftService.saveGift(
                            message,
                            images,
                            audio
                    );



            System.out.println(
                    "SAVE ID = " + gift.getId()
            );


            return ResponseEntity.ok(
                     String.valueOf(gift.getId())
            );



        }catch(Exception e){


            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(
                            "Save lỗi: "
                            + e.getMessage()
                    );

        }

    }







    // ============================
    // UPDATE
    // ============================

    @PostMapping(
            value="/update/{id}",
            consumes="multipart/form-data"
    )
    public ResponseEntity<?> updateGift(


            @PathVariable Long id,


            @RequestParam(value="message")
            String message,


            @RequestParam(
                    value="images",
                    required=false
            )
            MultipartFile[] images,


            @RequestParam(
                    value="audio",
                    required=false
            )
            MultipartFile audio


    ){


        try{


            Gift gift =
                    giftService.updateGift(
                            id,
                            message,
                            images,
                            audio
                    );


            return ResponseEntity.ok(
                   String.valueOf(gift.getId())
            );


        }catch(Exception e){


            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(
                            "Update lỗi: "
                            + e.getMessage()
                    );

        }

    }







    // ============================
    // GET BY ID API
    // ============================

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(

            @PathVariable Long id

    ){

        return ResponseEntity.ok(
                giftService.getById(id)
        );

    }





    // ============================
    // NFC (để sau)
    // ============================

    @GetMapping("/uid/{uid}")
    public ResponseEntity<?> getByUid(

            @PathVariable String uid

    ){

        return ResponseEntity.ok(
                giftService.getByUid(uid)
        );

    }

}