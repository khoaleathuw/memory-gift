package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    private String title;


    @Column(columnDefinition = "TEXT")
    private String message;


    // đường dẫn file audio
    private String voiceUrl;


    // UID NFC
    @Column(unique = true)
    private String uid;


    @OneToMany(
            mappedBy = "gift",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<GiftImage> images = new ArrayList<>();



    public Gift() {
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


    public List<GiftImage> getImages() {
        return images;
    }


    public void setImages(List<GiftImage> images) {
        this.images = images;
    }



    public void addImage(GiftImage image){

        images.add(image);

        image.setGift(this);

    }


    public void removeImage(GiftImage image){

        images.remove(image);

        image.setGift(null);

    }
}