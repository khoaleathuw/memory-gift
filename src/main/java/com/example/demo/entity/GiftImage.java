package com.example.demo.entity;

import jakarta.persistence.*;

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

    // ================= GETTER =================

    public Integer getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Gift getGift() {
        return gift;
    }

    // ================= SETTER (PHẢI CÓ) =================

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }
}