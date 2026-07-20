package com.example.demo.service;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.NfcRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class GiftService {


    private final GiftRepository giftRepository;
    private final NfcRepository nfcRepository;


    private static final Path BASE =
            Paths.get(
                    System.getProperty("user.home"),
                    "memory-gift",
                    "uploads"
            );


    private static final Path IMAGE_DIR =
            BASE.resolve("images");


    private static final Path AUDIO_DIR =
            BASE.resolve("audio");


    public GiftService(
            GiftRepository giftRepository,
            NfcRepository nfcRepository
    ) {

        this.giftRepository = giftRepository;
        this.nfcRepository = nfcRepository;


        try {

            Files.createDirectories(IMAGE_DIR);
            Files.createDirectories(AUDIO_DIR);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Không thể tạo thư mục upload",
                    e
            );

        }

    }


    // ==================================================
    // CREATE EMPTY GIFT
    // Chỉ tạo khi UID chưa có Gift
    // ==================================================

    @Transactional
    public Gift createEmptyGift(String uid) {


        if (uid == null || uid.isBlank()) {

            throw new IllegalArgumentException(
                    "UID không được để trống"
            );

        }


        // Kiểm tra UID đã có Gift chưa
        Gift existingGift = getByUid(uid);


        if (existingGift != null) {

            return existingGift;

        }


        Gift gift = new Gift();
        gift.setUid(uid);

        gift.setTitle("");
        gift.setMessage("");
        gift.setTheme("default");
        gift.setVoiceUrl(null);
        gift.setImages(new ArrayList<>());


        Long giftId =
                giftRepository.insertGift(gift);


        gift.setId(giftId);


        // Gắn UID NFC với Gift vừa tạo
        nfcRepository.bindGift(
                uid,
                giftId
        );


        return gift;

    }


    // ==================================================
    // SAVE GIFT
    // Dùng khi khách lưu lần đầu
    // Không tạo Gift mới nếu UID đã có Gift
    // ==================================================

    @Transactional
    public Gift saveGift(

            String uid,

            String theme,

            String message,

            MultipartFile[] images,

            MultipartFile audio

    ) throws IOException {


        if (uid == null || uid.isBlank()) {

            throw new IllegalArgumentException(
                    "UID không được để trống"
            );

        }


        // Lấy Gift đã được tạo lúc quét NFC
        Gift gift =
                getByUid(uid);


        // Trường hợp UID chưa có Gift thì mới tạo
        if (gift == null) {

            gift =
                    createEmptyGift(uid);

        }


        Long giftId =
                gift.getId();


        gift.setTitle("");


        gift.setTheme(
                theme == null || theme.isBlank()
                        ? "default"
                        : theme
        );


        gift.setMessage(
                message == null
                        ? ""
                        : message
        );


        // ==================================================
        // AUDIO
        // ==================================================

        if (audio != null && !audio.isEmpty()) {


            String oldAudio =
                    gift.getVoiceUrl();


            String audioName =
                    createSafeFileName(audio);


            Path audioTarget =
                    AUDIO_DIR.resolve(audioName);


            Files.copy(
                    audio.getInputStream(),
                    audioTarget,
                    StandardCopyOption.REPLACE_EXISTING
            );


            gift.setVoiceUrl(audioName);


            // Update DB trước
            giftRepository.updateGift(gift);


            // Sau khi update thành công mới xóa audio cũ
            if (oldAudio != null && !oldAudio.isBlank()) {

                Files.deleteIfExists(
                        AUDIO_DIR.resolve(oldAudio)
                );

            }

        } else {

            // Không có audio mới thì vẫn update text/theme
            giftRepository.updateGift(gift);

        }


        // ==================================================
        // THÊM ẢNH
        // ==================================================

        if (images != null) {


            for (MultipartFile file : images) {


                if (file == null || file.isEmpty()) {

                    continue;

                }


                String imageName =
                        createSafeFileName(file);


                Path imageTarget =
                        IMAGE_DIR.resolve(imageName);


                Files.copy(
                        file.getInputStream(),
                        imageTarget,
                        StandardCopyOption.REPLACE_EXISTING
                );


                giftRepository.insertImage(
                        giftId,
                        imageName
                );

            }

        }


        return giftRepository.findById(giftId);

    }


    // ==================================================
    // GET BY ID
    // ==================================================

    public Gift getById(Long id) {


        if (id == null) {

            return null;

        }


        return giftRepository.findById(id);

    }


    // ==================================================
    // GET BY UID
    // ==================================================

    public Gift getByUid(String uid) {


        if (uid == null || uid.isBlank()) {

            return null;

        }


        Long giftId =
                nfcRepository.findGiftIdByUid(uid);


        if (giftId == null) {

            return null;

        }


        return giftRepository.findById(giftId);

    }


    // ==================================================
    // UPDATE GIFT BY ID
    // ==================================================

    @Transactional
    public Gift updateGift(

            Long id,

            String title,

            String message,

            String theme,

            MultipartFile[] images,

            MultipartFile audio,

            boolean removeOldAudio,

            List<String> deleteImages

    ) throws IOException {


        Gift gift =
                giftRepository.findById(id);


        if (gift == null) {

            throw new RuntimeException(
                    "Không tìm thấy Gift ID: " + id
            );

        }


        if (gift.getImages() == null) {

            gift.setImages(
                    new ArrayList<>()
            );

        }


        gift.setTitle(
                title == null
                        ? ""
                        : title
        );


        gift.setMessage(
                message == null
                        ? ""
                        : message
        );


        gift.setTheme(
                theme == null || theme.isBlank()
                        ? "default"
                        : theme
        );


        String oldAudio =
                gift.getVoiceUrl();


        String newAudioName =
                null;


        // ==================================================
        // UPLOAD AUDIO MỚI
        // ==================================================

        if (audio != null && !audio.isEmpty()) {


            newAudioName =
                    createSafeFileName(audio);


            Path audioTarget =
                    AUDIO_DIR.resolve(newAudioName);


            Files.copy(
                    audio.getInputStream(),
                    audioTarget,
                    StandardCopyOption.REPLACE_EXISTING
            );


            gift.setVoiceUrl(
                    newAudioName
            );

        } else if (removeOldAudio) {


            gift.setVoiceUrl(null);

        }


        // Update thông tin Gift
        giftRepository.updateGift(gift);


        // ==================================================
        // XÓA AUDIO CŨ
        // ==================================================

        boolean hasNewAudio =
                newAudioName != null;


        if (
                oldAudio != null
                        && !oldAudio.isBlank()
                        && (removeOldAudio || hasNewAudio)
        ) {


            Files.deleteIfExists(
                    AUDIO_DIR.resolve(oldAudio)
            );

        }


        // ==================================================
        // XÓA ẢNH CŨ
        // ==================================================

        if (
                deleteImages != null
                        && !deleteImages.isEmpty()
        ) {


            for (String imageName : deleteImages) {


                if (
                        imageName == null
                                || imageName.isBlank()
                ) {

                    continue;

                }


                // Xóa dòng trong DB
                giftRepository.deleteImageByUrl(
                        id,
                        imageName
                );


                // Xóa file vật lý
                Files.deleteIfExists(
                        IMAGE_DIR.resolve(imageName)
                );

            }

        }


        // ==================================================
        // THÊM ẢNH MỚI
        // ==================================================

        if (images != null) {


            for (MultipartFile file : images) {


                if (
                        file == null
                                || file.isEmpty()
                ) {

                    continue;

                }


                String imageName =
                        createSafeFileName(file);


                Path imageTarget =
                        IMAGE_DIR.resolve(imageName);


                Files.copy(
                        file.getInputStream(),
                        imageTarget,
                        StandardCopyOption.REPLACE_EXISTING
                );


                giftRepository.insertImage(
                        id,
                        imageName
                );

            }

        }


        return giftRepository.findById(id);

    }


    // ==================================================
    // UPDATE GIFT BY UID
    // ==================================================

    @Transactional
    public Gift updateGiftByUid(

            String uid,

            String title,

            String message,

            String theme,

            MultipartFile[] images,

            MultipartFile audio,

            boolean removeOldAudio,

            List<String> deleteImages

    ) throws IOException {


        Gift gift =
                getByUid(uid);


        if (gift == null) {

            throw new RuntimeException(
                    "Không tìm thấy Gift với UID: " + uid
            );

        }


        return updateGift(

                gift.getId(),

                title,

                message,

                theme,

                images,

                audio,

                removeOldAudio,

                deleteImages

        );

    }


    // ==================================================
    // TẠO TÊN FILE AN TOÀN
    // ==================================================

    private String createSafeFileName(
            MultipartFile file
    ) {


        String originalName =
                file.getOriginalFilename();


        if (
                originalName == null
                        || originalName.isBlank()
        ) {

            originalName = "file";

        }


        // Loại bỏ đường dẫn như C:\fakepath\...
        originalName =
                Paths.get(originalName)
                        .getFileName()
                        .toString();


        return UUID.randomUUID()
                + "_"
                + originalName;

    }

}