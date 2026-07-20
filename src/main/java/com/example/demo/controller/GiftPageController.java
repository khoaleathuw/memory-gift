package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GiftPageController {

    private final GiftService giftService;

    public GiftPageController(GiftService giftService) {
        this.giftService = giftService;
    }

    // ==================================================
    // NFC SCAN
    // ==================================================

    @GetMapping("/nfc/{uid}")
    public String openGift(
            @PathVariable String uid,
            Model model
    ) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            gift = giftService.createEmptyGift(uid);
        }

        addCommonModel(model, gift, uid);

        // Chưa chọn theme thì vào trang theme.html
        if (!isValidTheme(gift.getTheme())) {
            return "theme";
        }

        return getGiftView(gift.getTheme());
    }

    // ==================================================
    // TRANG CHỌN THEME
    // ==================================================

    @GetMapping("/gift/theme/{uid}")
    public String chooseTheme(
            @PathVariable String uid,
            Model model
    ) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            gift = giftService.createEmptyGift(uid);
        }

        addCommonModel(model, gift, uid);

        return "theme";
    }

    // ==================================================
    // INDEX SAU KHI CHỌN THEME
    // ==================================================

    @GetMapping("/gift/index/{uid}")
    public String openIndex(
            @PathVariable String uid,
            @RequestParam(required = false) String theme,
            Model model
    ) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            gift = giftService.createEmptyGift(uid);
        }

        /*
         * Ví dụ:
         * /gift/index/nfc001?theme=instagram
         */
        if (theme != null && !theme.isBlank()) {

            String selectedTheme = normalizeTheme(theme);

            if (!isValidTheme(selectedTheme)) {
                return "redirect:/gift/theme/" + uid;
            }

            /*
             * Gán theme tạm thời để mở đúng trang index.
             * Theme sẽ được lưu xuống DB khi người dùng bấm lưu.
             */
            gift.setTheme(selectedTheme);
        }

        if (!isValidTheme(gift.getTheme())) {
            return "redirect:/gift/theme/" + uid;
        }

        addCommonModel(model, gift, uid);

        model.addAttribute(
                "selectedTheme",
                normalizeTheme(gift.getTheme())
        );

        return getIndexView(gift.getTheme());
    }

    // ==================================================
    // VIEW
    // ==================================================

    @GetMapping("/gift/view/{uid}")
    public String viewGift(
            @PathVariable String uid,
            Model model
    ) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            return "not-found";
        }

        if (!isValidTheme(gift.getTheme())) {
            return "redirect:/gift/theme/" + uid;
        }

        addCommonModel(model, gift, uid);

        return getGiftView(gift.getTheme());
    }

    // ==================================================
    // EDIT
    // ==================================================

    @GetMapping("/gift/edit/{uid}")
    public String editGift(
            @PathVariable String uid,
            @RequestParam(required = false) String theme,
            Model model
    ) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            gift = giftService.createEmptyGift(uid);
        }

        /*
         * Ví dụ:
         * /gift/edit/nfc001?theme=instagram
         */
        if (theme != null && !theme.isBlank()) {

            String selectedTheme = normalizeTheme(theme);

            if (!isValidTheme(selectedTheme)) {
                return "redirect:/gift/theme/" + uid;
            }

            gift.setTheme(selectedTheme);
        }

        if (!isValidTheme(gift.getTheme())) {
            return "redirect:/gift/theme/" + uid;
        }

        addCommonModel(model, gift, uid);

        model.addAttribute(
                "selectedTheme",
                normalizeTheme(gift.getTheme())
        );

        return getEditView(gift.getTheme());
    }

    // ==================================================
    // MODEL DÙNG CHUNG
    // ==================================================

    private void addCommonModel(
            Model model,
            Gift gift,
            String uid
    ) {

        model.addAttribute("gift", gift);
        model.addAttribute("nfcUid", uid);
        model.addAttribute("uid", uid);
    }

    // ==================================================
    // INDEX THEO THEME
    // ==================================================

    private String getIndexView(String theme) {

        return switch (normalizeTheme(theme)) {

            case "sakura" -> "sakura/index";

            case "matcha" -> "matcha/index";

            case "sky" -> "sky/index";

            case "instagram" -> "instagram/index";

            default -> throw new IllegalArgumentException(
                    "Theme không hợp lệ: " + theme
            );
        };
    }

    // ==================================================
    // VIEW THEO THEME
    // ==================================================

    private String getGiftView(String theme) {

        return switch (normalizeTheme(theme)) {

            case "sakura" -> "sakura/gift";

            case "matcha" -> "matcha/gift";

            case "sky" -> "sky/gift";

            case "instagram" -> "instagram/gift";

            default -> throw new IllegalArgumentException(
                    "Theme không hợp lệ: " + theme
            );
        };
    }

    // ==================================================
    // EDIT THEO THEME
    // ==================================================

    private String getEditView(String theme) {

        return switch (normalizeTheme(theme)) {

            case "sakura" -> "sakura/gift-edit";

            case "matcha" -> "matcha/gift-edit";

            case "sky" -> "sky/gift-edit";

            case "instagram" -> "instagram/gift-edit";

            default -> throw new IllegalArgumentException(
                    "Theme không hợp lệ: " + theme
            );
        };
    }

    // ==================================================
    // VALIDATE THEME
    // ==================================================

    private boolean isValidTheme(String theme) {

        if (theme == null || theme.isBlank()) {
            return false;
        }

        String normalizedTheme = normalizeTheme(theme);

        return normalizedTheme.equals("sakura")
                || normalizedTheme.equals("sky")
                || normalizedTheme.equals("matcha")
                || normalizedTheme.equals("instagram");
    }

    // ==================================================
    // CHUẨN HÓA THEME
    // ==================================================

    private String normalizeTheme(String theme) {

        if (theme == null) {
            return "";
        }

        return theme.trim().toLowerCase();
    }
}