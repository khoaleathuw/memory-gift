package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "gift_image")
public class GiftImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "gift_id")
    private Gift gift;

    public Integer getId() { return id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Gift getGift() { return gift; }
    public void setGift(Gift gift) { this.gift = gift; }

    // Quan trọng: List (bag) + orphanRemoval cần equals/hashCode dựa trên id
    // để Hibernate đối chiếu chính xác phần tử cũ/mới khi merge entity detached.
    // Không dùng các field khác (như imageUrl) vì chúng có thể null lúc entity
    // chưa được persist, gây so sánh sai.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftImage)) return false;
        GiftImage that = (GiftImage) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        // Hằng số cố định (không dùng id) để hashCode không đổi khi id được gán
        // sau khi persist — tránh phá vỡ contract khi object đã nằm trong HashSet/bag.
        return getClass().hashCode();
    }
}