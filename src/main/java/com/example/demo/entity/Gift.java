package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;


public class Gift {


    private Long id;


    private String uid;


    private String title;


    private String message;


    private String voiceUrl;


    private List<GiftImage> images = new ArrayList<>();


    public Gift() {
    }



    public Gift(Long id,
                String uid,
                String title,
                String message,
                String voiceUrl) {

        this.id = id;
        this.uid = uid;
        this.title = title;
        this.message = message;
        this.voiceUrl = voiceUrl;

    }



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
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



    public List<GiftImage> getImages() {
        return images;
    }


    public void setImages(List<GiftImage> images) {
        this.images = images;
    }



    public void addImage(GiftImage image) {

        this.images.add(image);

    }

}