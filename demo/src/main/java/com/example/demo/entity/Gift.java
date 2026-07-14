package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String voiceUrl;

    // Đổi từ List sang Set: với @OneToMany + orphanRemoval=true, Hibernate quản lý
    // Set (PersistentSet) ổn định hơn nhiều so với List (PersistentBag) khi flush/merge.
    // Bag không có cách xác định phần tử nào thực sự bị xóa ngoài việc so sánh toàn bộ
    // collection theo thứ tự -> dễ gây xóa nhầm khi entity đi qua vài lần load/attach.
    @OneToMany(
            mappedBy = "gift",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<GiftImage> images = new LinkedHashSet<>();

    public void addImage(GiftImage image) {
        images.add(image);
        image.setGift(this);
    }

    public void removeImage(GiftImage image) {
        images.remove(image);
        image.setGift(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getVoiceUrl() { return voiceUrl; }
    public void setVoiceUrl(String voiceUrl) { this.voiceUrl = voiceUrl; }

    public Set<GiftImage> getImages() { return images; }
    public void setImages(Set<GiftImage> images) { this.images = images; }
}