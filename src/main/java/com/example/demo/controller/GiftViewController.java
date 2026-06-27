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

    // ===================== NFC VIEW =====================
    @GetMapping("/view/{id}")
    public String viewGift(@PathVariable Integer id, Model model) {

        Gift gift = giftRepository.findById(id).orElse(null);

        if (gift == null) {
            return "error"; // hoặc redirect /gift/not-found
        }

        model.addAttribute("gift", gift);

        return "gift"; // templates/gift.html
    }

    // ===================== EDIT PAGE =====================
    @GetMapping("/edit/{id}")
    public String editGift(@PathVariable Integer id, Model model) {

        Gift gift = giftRepository.findById(id).orElse(null);

        if (gift == null) {
            return "error";
        }

        model.addAttribute("gift", gift);

        return "gift-edit"; // templates/gift-edit.html
    }

    // ===================== OPTIONAL: DEBUG =====================
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "OK";
    }
}