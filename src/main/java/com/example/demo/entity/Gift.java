package com.example.demo.entity;
import java.util.ArrayList;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String voiceUrl;

    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiftImage> images = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getVoiceUrl() { return voiceUrl; }
    public void setVoiceUrl(String voiceUrl) { this.voiceUrl = voiceUrl; }

    public List<GiftImage> getImages() { return images; }
    public void setImages(List<GiftImage> images) { this.images = images; }

}