package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "voice_url")
    private String voiceUrl;

    @Column(unique = true)
    private String uid;

    @OneToMany(
            mappedBy = "gift",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<GiftImage> images = new ArrayList<>();

    public Gift() {
    }

    //==========================
    // Getter & Setter
    //==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<GiftImage> getImages() {
        return images;
    }

    /**
     * Không thay thế List bằng List mới trực tiếp để tránh Hibernate mất tracking.
     * Xóa các phần tử cũ và thêm phần tử mới thông qua helper methods.
     */
    public void setImages(List<GiftImage> images) {
        this.images.clear();
        if (images != null) {
            for (GiftImage image : images) {
                addImage(image);
            }
        }
    }

    //==========================
    // Helper Methods (Đồng bộ quan hệ 2 chiều)
    //==========================

    public void addImage(GiftImage image) {
        images.add(image);
        image.setGift(this);
    }

    public void removeImage(GiftImage image) {
        images.remove(image);
        image.setGift(null);
    }
}