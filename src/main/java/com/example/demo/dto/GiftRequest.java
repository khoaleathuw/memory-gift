package com.example.demo.dto;


import org.springframework.web.multipart.MultipartFile;


public class GiftRequest {


    private String title;


    private String message;


    private String uid;


    private MultipartFile[] images;


    private MultipartFile audio;

    private String theme;

    public GiftRequest() {
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




    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
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

    public String getTheme() {
    return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
}
}