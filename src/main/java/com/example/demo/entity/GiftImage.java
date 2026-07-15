package com.example.demo.entity;

public class GiftImage {

    private Long id;

    private Long giftId;

    private String imageUrl;

    public GiftImage() {
    }

    public GiftImage(Long id,
                     Long giftId,
                     String imageUrl) {

        this.id = id;
        this.giftId = giftId;
        this.imageUrl = imageUrl;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGiftId() {
        return giftId;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}