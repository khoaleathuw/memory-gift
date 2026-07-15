package com.example.demo.service;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.NfcRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GiftService {

    private final GiftRepository giftRepository;
    private final NfcRepository nfcRepository;

    private static final Path BASE =
            Paths.get(System.getProperty("user.home"),
                    "memory-gift",
                    "uploads");

    private static final Path IMAGE_DIR = BASE.resolve("images");
    private static final Path AUDIO_DIR = BASE.resolve("audio");
        public GiftService(
                GiftRepository giftRepository,
                NfcRepository nfcRepository
        ){

        this.giftRepository = giftRepository;
        this.nfcRepository = nfcRepository;


        try {

                Files.createDirectories(IMAGE_DIR);
                Files.createDirectories(AUDIO_DIR);

        } catch(IOException e){

                throw new RuntimeException(e);

        }

        }
         @Transactional
        public Gift createEmptyGift(String uid){


        // tạo gift rỗng
        Gift gift = new Gift();

        gift.setTitle("");
        gift.setMessage("");


        Long giftId =
                giftRepository.insertGift(gift);


        // tạo nfc_tag
        nfcRepository.bindGift(uid, giftId);


        return gift;
        }
    // ==================================
    // SAVE
    // ==================================

    public Gift saveGift(

            String uid,

            String title,

            String message,

            MultipartFile[] images,

            MultipartFile audio

    ) throws IOException {

        Gift gift = new Gift();

        gift.setTitle(title);
        gift.setMessage(message);

        // ---------- audio ----------

        if (audio != null && !audio.isEmpty()) {

            String audioName =
                    UUID.randomUUID() + "_" + audio.getOriginalFilename();

            Path target =
                    AUDIO_DIR.resolve(audioName);

            Files.copy(
                    audio.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            gift.setVoiceUrl(audioName);

        }

        Long giftId =
                giftRepository.insertGift(gift);

        gift.setId(giftId);

        List<GiftImage> imageList =
                new ArrayList<>();

        // ---------- images ----------

        if (images != null) {

            for (MultipartFile file : images) {

                if (file.isEmpty())
                    continue;

                String imageName =
                        UUID.randomUUID() + "_"
                                + file.getOriginalFilename();

                Path target =
                        IMAGE_DIR.resolve(imageName);

                Files.copy(
                        file.getInputStream(),
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );

                giftRepository.insertImage(
                        giftId,
                        imageName
                );
                GiftImage img =
                        new GiftImage();

                img.setGiftId(giftId);
                img.setImageUrl(imageName);

                imageList.add(img);

            }

        }

        gift.setImages(imageList);

        nfcRepository.bindGift(uid, giftId);

        return gift;

    }

    // ==================================
    // GET BY ID
    // ==================================

    public Gift getById(Long id) {

        return giftRepository.findById(id);

    }

    // ==================================
    // GET BY UID
    // ==================================

        public Gift getByUid(String uid) {


        Long id =
                nfcRepository.findGiftIdByUid(uid);


        if(id == null)
                return null;


        return giftRepository.findById(id);

        }
// ==================================
// UPDATE
// ==================================

public Gift updateGift(

        Long id,

        String title,

        String message,

        MultipartFile[] images,

        MultipartFile audio,

        boolean removeOldAudio,

        List<String> deleteImages

) throws IOException {

    Gift gift = giftRepository.findById(id);

    if (gift == null) {
        throw new RuntimeException("Gift not found");
    }

    gift.setTitle(title);
    gift.setMessage(message);

    // -----------------------------
    // Xóa audio cũ
    // -----------------------------
    if (removeOldAudio && gift.getVoiceUrl() != null) {

        Files.deleteIfExists(
                AUDIO_DIR.resolve(gift.getVoiceUrl())
        );

        gift.setVoiceUrl(null);
    }

    // -----------------------------
    // Upload audio mới
    // -----------------------------
    if (audio != null && !audio.isEmpty()) {

        if (gift.getVoiceUrl() != null) {

            Files.deleteIfExists(
                    AUDIO_DIR.resolve(gift.getVoiceUrl())
            );

        }

        String audioName =
                UUID.randomUUID() + "_" + audio.getOriginalFilename();

        Files.copy(
                audio.getInputStream(),
                AUDIO_DIR.resolve(audioName),
                StandardCopyOption.REPLACE_EXISTING
        );

        gift.setVoiceUrl(audioName);

    }

    // -----------------------------
    // Xóa ảnh cũ
    // -----------------------------
    if (deleteImages != null) {

        for (String img : deleteImages) {

            Files.deleteIfExists(
                    IMAGE_DIR.resolve(img)
            );

            giftRepository.deleteImageByUrl(id, img);

        }

    }

    // -----------------------------
    // Thêm ảnh mới
    // -----------------------------
    if (images != null) {

        for (MultipartFile file : images) {

            if (file.isEmpty())
                continue;

            String imageName =
                    UUID.randomUUID() + "_"
                            + file.getOriginalFilename();

            Files.copy(
                    file.getInputStream(),
                    IMAGE_DIR.resolve(imageName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            giftRepository.insertImage(
                    id,
                    imageName
            );

        }

    }

    giftRepository.updateGift(gift);

    return giftRepository.findById(id);

}

// ==================================
// DELETE
// ==================================

public void deleteGift(Long id) throws IOException {

    Gift gift = giftRepository.findById(id);

    if (gift == null) {
        return;
    }

    // Xóa audio
    if (gift.getVoiceUrl() != null) {

        Files.deleteIfExists(
                AUDIO_DIR.resolve(gift.getVoiceUrl())
        );

    }

    // Xóa ảnh
    if (gift.getImages() != null) {

        for (GiftImage img : gift.getImages()) {

            Files.deleteIfExists(
                    IMAGE_DIR.resolve(img.getImageUrl())
            );

        }

    }

    giftRepository.deleteGift(id);


}
@Transactional
public Gift updateGiftByUid(

        String uid,

        String title,

        String message,

        MultipartFile[] images,

        MultipartFile audio,

        boolean removeOldAudio,

        List<String> deleteImages

) throws IOException {


    Gift gift = getByUid(uid);


    if(gift == null){

        throw new RuntimeException("Không tìm thấy gift với uid: " + uid);

    }


    return updateGift(

            gift.getId(),

            title,

            message,

            images,

            audio,

            removeOldAudio,

            deleteImages

    );

}
}