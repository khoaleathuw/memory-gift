package com.example.demo.controller;


import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class GiftPageController {


    private final GiftService giftService;


    public GiftPageController(
            GiftService giftService
    ){

        this.giftService = giftService;

    }



    // VIEW

    @GetMapping("/gift/view/{uid}")
    public String viewGift(

            @PathVariable String uid,

            Model model

    ){


        Gift gift =
                giftService.getByUid(uid);



        if(gift == null){

            return "not-found";

        }


        model.addAttribute("gift",gift);


        return "gift";

    }



    // EDIT

        @GetMapping("/gift/edit/{uid}")
    public String editGift(
            @PathVariable String uid,
            Model model
    ){

        Gift gift = giftService.getByUid(uid);

        if(gift == null){
            return "not-found";
        }


        model.addAttribute("gift", gift);

        // truyền NFC uid xuống html
        model.addAttribute("nfcUid", uid);


        return "gift-edit";
    }

}