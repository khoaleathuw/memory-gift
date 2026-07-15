package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.service.GiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/gift")
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService){
        this.giftService = giftService;
    }

    // ==========================================
    // SAVE (Tạo mới hoàn toàn - Chỉ nhận File mới)
    // ==========================================
    @PostMapping(
            value = "/save",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> saveGift(
            @RequestParam(value="message", required=false) String message,
            // Đảm bảo Frontend CHỈ gửi các file ảnh vật lý lên đây
            @RequestParam(value="images", required=false) MultipartFile[] images,
            @RequestParam(value="audio", required=false) MultipartFile audio
    ){
        try {
            Gift gift = giftService.saveGift(message, images, audio);
            
            // Chỉ trả về ID để tránh kích hoạt Lazy Loading
            return ResponseEntity.ok(gift.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Save lỗi: " + e.getMessage());
        }
    }

    // ==========================================
    // UPDATE (Cập nhật - Nhận File mới & danh sách URL cần xóa)
    // ==========================================
    @PostMapping(
            value="/update/{id}",
            consumes="multipart/form-data"
    )
    public ResponseEntity<?> updateGift(
            @PathVariable Long id,
            @RequestParam(value="message", required=false) String message,
            @RequestParam(value="images", required=false) MultipartFile[] images,
            @RequestParam(value="audio", required=false) MultipartFile audio,
            @RequestParam(value="removeOldAudio", required=false, defaultValue="false") boolean removeOldAudio,
            // Danh sách URL dạng String của các ảnh muốn xóa
            @RequestParam(value="deleteImages", required=false) List<String> deleteImages
    ){
        try {
            Gift gift = giftService.updateGift(
                    id,
                    message,
                    images,
                    audio,
                    removeOldAudio,
                    deleteImages
            );

            return ResponseEntity.ok(gift.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Update lỗi: " + e.getMessage());
        }
    }

    // ==========================================
    // GET ID (Sử dụng Projection hoặc DTO đơn giản để tránh SELECT vòng lặp)
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        // Để tránh log SELECT thừa từ Jackson serializer, bạn nên map Gift sang DTO 
        // hoặc tạm thời trả về đối tượng đã được bỏ qua quan hệ vòng lặp ở entity GiftImage.
        return ResponseEntity.ok(giftService.getById(id));
    }
}