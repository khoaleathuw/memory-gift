package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

public class GiftRequest {

    private String message;

    private MultipartFile[] images;

    private MultipartFile audio;

    // getter + setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }

    public MultipartFile getAudio() {
        return audio;
    }

    public void setAudio(MultipartFile audio) {
        this.audio = audio;
    }
}