package com.example.demo.service;


import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.GiftImageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;



@Service
public class GiftService {


    private final GiftRepository giftRepository;

    private final GiftImageRepository giftImageRepository;



    private final String UPLOAD_DIR =
            System.getProperty("user.home")
            + "/memory-gift/uploads/";



    public GiftService(
            GiftRepository giftRepository,
            GiftImageRepository giftImageRepository
    ){

        this.giftRepository = giftRepository;
        this.giftImageRepository = giftImageRepository;

    }






    // =========================
    // SAVE
    // =========================

    @Transactional
    public Gift saveGift(

            String message,

            MultipartFile[] images,

            MultipartFile audio

    ) throws IOException {



        Gift gift = new Gift();


        gift.setMessage(message);



        // lưu gift trước để có id (cần cho việc tạo đường dẫn file nếu sau này cần)
        gift = giftRepository.save(gift);




        // AUDIO

        if(audio != null && !audio.isEmpty()){


            String path =
                    saveFile(
                            audio,
                            "audio"
                    );


            gift.setVoiceUrl(path);

        }





        // IMAGE

        if(images != null){


            for(MultipartFile image : images){


                if(!image.isEmpty()){


                    String path =
                            saveFile(
                                    image,
                                    "images"
                            );



                    GiftImage giftImage =
                            new GiftImage();


                    giftImage.setImageUrl(path);


                    // dùng helper 2 chiều, cascade = ALL trên Gift.images
                    // sẽ tự INSERT khi transaction commit -> KHÔNG cần
                    // gọi giftImageRepository.save(giftImage) thủ công nữa
                    gift.addImage(giftImage);


                }

            }

        }



        // Gift đang managed trong transaction này (nhờ @Transactional),
        // Hibernate sẽ tự flush thay đổi (voiceUrl, images mới) khi commit.
        // KHÔNG cần gọi giftRepository.save(gift) lần nữa ở đây.
        return gift;

    }








    // =========================
    // UPDATE
    // =========================


    @Transactional
    public Gift updateGift(

            Long id,

            String message,

            MultipartFile[] images,

            MultipartFile audio

    ) throws IOException {



        Gift gift =
                giftRepository.findById(id)
                .orElseThrow();

        System.out.println("Before: " + gift.getImages().size());

        gift.setMessage(message);



        if(audio != null && !audio.isEmpty()){


            String path =
                    saveFile(
                            audio,
                            "audio"
                    );


            gift.setVoiceUrl(path);

        }



        if(images != null){

        for(MultipartFile image : images){

                if(!image.isEmpty()){

                GiftImage img = new GiftImage();

                img.setImageUrl(
                        saveFile(
                                image,
                                "images"
                        )
                );

                // dùng helper 2 chiều thay vì set thủ công 2 dòng như trước
                gift.addImage(img);

                }

        }

        }
        System.out.println("After: " + gift.getImages().size());

        // gift đang managed trong transaction này (@Transactional),
        // Hibernate tự dirty-check + flush khi commit.
        // KHÔNG cần gọi giftRepository.save(gift) nữa -> tránh merge() và
        // các SELECT phụ mà merge() gây ra.
        return gift;

    }








    // =========================
    // GET
    // =========================


    @Transactional(readOnly = true)
    public Gift getById(Long id){


        return giftRepository
                .findById(id)
                .orElseThrow();

    }







    // =========================
    // SAVE FILE
    // =========================


    private String saveFile(

            MultipartFile file,

            String folder

    ) throws IOException {



        Path dir =
                Paths.get(
                        UPLOAD_DIR + folder
                );



        if(!Files.exists(dir)){

            Files.createDirectories(dir);

        }




        String filename =
                UUID.randomUUID()
                +"_"
                +file.getOriginalFilename();



        Files.copy(

                file.getInputStream(),

                dir.resolve(filename),

                StandardCopyOption.REPLACE_EXISTING

        );



        return "/files/"
                +folder
                +"/"
                +filename;

    }


}