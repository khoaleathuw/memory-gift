package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gift")
public class GiftViewController {

    @Autowired
    private GiftRepository giftRepository;

    // ===================== VIEW GIFT =====================
    @GetMapping("/view/{id}")
    public String viewGift(@PathVariable Integer id, Model model) {

        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gift not found"));

        // FIX URL audio nếu null
        if (gift.getVoiceUrl() == null) {
            gift.setVoiceUrl("");
        }

        // đảm bảo images không null
        if (gift.getImages() == null) {
            gift.setImages(new java.util.ArrayList<>());
        }

        model.addAttribute("gift", gift);

        return "gift";
    }

    // ===================== EDIT PAGE =====================
    @GetMapping("/edit/{id}")
    public String editGift(@PathVariable Integer id, Model model) {

        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gift not found"));

        if (gift.getImages() == null) {
            gift.setImages(new java.util.ArrayList<>());
        }

        model.addAttribute("gift", gift);

        return "gift-edit";
    }

    // ===================== DEBUG =====================
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "OK";
    }
}