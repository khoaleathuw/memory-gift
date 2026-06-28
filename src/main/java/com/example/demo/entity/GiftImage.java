package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gift_image")
public class GiftImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "gift_id")
    private Gift gift;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Gift getGift() { return gift; }
    public void setGift(Gift gift) { this.gift = gift; }
}