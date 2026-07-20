package com.example.demo.dto;


import java.util.List;



public class GiftResponse {


    private Long id;


    private String title;


    private String message;


    private String voiceUrl;


    private String uid;


    private List<String> images;


    private String theme;



    public GiftResponse() {

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




    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }




    public List<String> getImages() {
        return images;
    }


    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
}
}