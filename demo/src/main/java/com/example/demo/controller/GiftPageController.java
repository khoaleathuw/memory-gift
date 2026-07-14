package com.example.demo.controller;


import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class GiftPageController {


    private final GiftService giftService;


    public GiftPageController(GiftService giftService){
        this.giftService = giftService;
    }



    // VIEW GIFT
    @GetMapping("/gift/view/{id}")
    public String viewGift(

            @PathVariable Long id,
            Model model

    ){

        Gift gift = giftService.getById(id);


        model.addAttribute(
                "gift",
                gift
        );


        return "gift";
    }



    // EDIT GIFT
    @GetMapping("/gift/edit/{id}")
    public String editGift(

            @PathVariable Long id,
            Model model

    ){

        Gift gift = giftService.getById(id);


        model.addAttribute(
                "gift",
                gift
        );


        return "gift-edit";
    }
@GetMapping("/test")
@ResponseBody
public String test(){
    return "GiftPageController OK";
}

}