package com.example.demo.dto;

import java.util.List;

public class GiftResponseDTO {

    private Long id;
    private String title;
    private String message;
    private String voiceUrl;
    private List<String> images;

    public GiftResponseDTO() {
    }

    public GiftResponseDTO(Long id,
                           String title,
                           String message,
                           String voiceUrl,
                           List<String> images) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.voiceUrl = voiceUrl;
        this.images = images;
    }

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}