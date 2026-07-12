package com.example.demo.controller;

import com.example.demo.entity.NfcTag;
import com.example.demo.repository.NfcTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nfc")
public class NfcController {

    @Autowired
    private NfcTagRepository nfcTagRepository;

    @GetMapping("/{uid}")
    public String scan(@PathVariable String uid) {

        NfcTag tag = nfcTagRepository.findByUid(uid).orElse(null);

        if (tag == null) {
            return "redirect:/";
        }

        // Chưa có quà
        if (tag.getGift() == null) {
            return "redirect:/?uid=" + uid;
        }

        // Đã có quà
        return "redirect:/gift/view/" + tag.getGift().getId();
    }

}