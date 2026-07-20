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

        Gift gift = getOrCreateGift(uid);

        addCommonModel(model, gift, uid);

        // Chưa chọn giao diện
        if (!isValidTheme(gift.getTheme())) {
            return "theme";
        }

        // Đã có giao diện thì vào trang view
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

        Gift gift = getOrCreateGift(uid);

        addCommonModel(model, gift, uid);

        return "theme";
    }

    // ==================================================
    // TRANG INDEX SAU KHI CHỌN THEME
    // ==================================================

    @GetMapping("/gift/index/{uid}")
    public String openIndex(
            @PathVariable String uid,
            @RequestParam(required = false) String theme,
            Model model
    ) {

        Gift gift = getOrCreateGift(uid);

        String selectedTheme = resolveTheme(gift, theme);

        if (!isValidTheme(selectedTheme)) {
            return "redirect:/gift/theme/" + uid;
        }

        /*
         * Gán theme vào Gift để Thymeleaf nhận đúng theme.
         *
         * Theme sẽ được lưu xuống database khi người dùng
         * gửi form lưu Gift.
         */
        gift.setTheme(selectedTheme);

        addCommonModel(model, gift, uid);
        model.addAttribute("selectedTheme", selectedTheme);

        return getIndexView(selectedTheme);
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

        String selectedTheme = normalizeTheme(gift.getTheme());

        if (!isValidTheme(selectedTheme)) {
            return "redirect:/gift/theme/" + uid;
        }

        gift.setTheme(selectedTheme);

        addCommonModel(model, gift, uid);
        model.addAttribute("selectedTheme", selectedTheme);

        return getGiftView(selectedTheme);
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

        Gift gift = getOrCreateGift(uid);

        String selectedTheme = resolveTheme(gift, theme);

        if (!isValidTheme(selectedTheme)) {
            return "redirect:/gift/theme/" + uid;
        }

        gift.setTheme(selectedTheme);

        addCommonModel(model, gift, uid);
        model.addAttribute("selectedTheme", selectedTheme);

        return getEditView(selectedTheme);
    }

    // ==================================================
    // LẤY HOẶC TẠO GIFT
    // ==================================================

    private Gift getOrCreateGift(String uid) {

        Gift gift = giftService.getByUid(uid);

        if (gift == null) {
            gift = giftService.createEmptyGift(uid);
        }

        return gift;
    }

    // ==================================================
    // XÁC ĐỊNH THEME
    // ==================================================

    private String resolveTheme(
            Gift gift,
            String requestTheme
    ) {

        // Ưu tiên theme trên URL
        if (requestTheme != null && !requestTheme.isBlank()) {

            String normalizedRequestTheme =
                    normalizeTheme(requestTheme);

            if (isValidTheme(normalizedRequestTheme)) {
                return normalizedRequestTheme;
            }

            return "";
        }

        // Nếu URL không truyền theme thì lấy theme trong DB
        return normalizeTheme(gift.getTheme());
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

        // Giữ cả hai tên để các giao diện cũ vẫn hoạt động
        model.addAttribute("nfcUid", uid);
        model.addAttribute("uid", uid);
    }

    // ==================================================
    // INDEX THEO THEME
    // ==================================================

    private String getIndexView(String theme) {

        return switch (normalizeTheme(theme)) {

            case "sakura" ->
                    "sakura/index";

            case "matcha" ->
                    "matcha/index";

            case "sky" ->
                    "sky/index";

            case "instagram" ->
                    "instagram/index";

            default ->
                    throw new IllegalArgumentException(
                            "Theme không hợp lệ: " + theme
                    );
        };
    }

    // ==================================================
    // VIEW THEO THEME
    // ==================================================

    private String getGiftView(String theme) {

        return switch (normalizeTheme(theme)) {

            case "sakura" ->
                    "sakura/gift";

            case "matcha" ->
                    "matcha/gift";

            case "sky" ->
                    "sky/gift";

            case "instagram" ->
                    "instagram/gift";

            default ->
                    throw new IllegalArgumentException(
                            "Theme không hợp lệ: " + theme
                    );
        };
    }

    // ==================================================
    // EDIT THEO THEME
    // ==================================================

    private String getEditView(String theme) {

        return switch (normalizeTheme(theme)) {

            case "sakura" ->
                    "sakura/gift-edit";

            case "matcha" ->
                    "matcha/gift-edit";

            case "sky" ->
                    "sky/gift-edit";

            case "instagram" ->
                    "instagram/gift-edit";

            default ->
                    throw new IllegalArgumentException(
                            "Theme không hợp lệ: " + theme
                    );
        };
    }

    // ==================================================
    // KIỂM TRA THEME
    // ==================================================

    private boolean isValidTheme(String theme) {

        String normalizedTheme =
                normalizeTheme(theme);

        return switch (normalizedTheme) {

            case "sakura",
                 "matcha",
                 "sky",
                 "instagram" -> true;

            default -> false;
        };
    }

    // ==================================================
    // CHUẨN HÓA THEME
    // ==================================================

    private String normalizeTheme(String theme) {

        if (theme == null) {
            return "";
        }

        return theme
                .trim()
                .toLowerCase();
    }
}