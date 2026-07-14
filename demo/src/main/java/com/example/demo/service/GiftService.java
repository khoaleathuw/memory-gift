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
import java.util.List;
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


                    gift.addImage(giftImage);


                }

            }

        }



        // gift dang managed trong transaction -> Hibernate tu flush khi commit
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

            MultipartFile audio,

            boolean removeOldAudio,

            List<String> deleteImages

    ) throws IOException {



        // CHI load Gift dung 1 LAN cho toan bo thao tac update
        Gift gift =
                giftRepository.findById(id)
                .orElseThrow();


        if(message != null){

            gift.setMessage(message);

        }



        // =========================
        // XOA AUDIO CU (neu client yeu cau)
        // =========================

        if(removeOldAudio){

            gift.setVoiceUrl(null);

        }



        // =========================
        // AUDIO MOI (ghi de, khong can select them)
        // =========================

        if(audio != null && !audio.isEmpty()){

            String path =
                    saveFile(
                            audio,
                            "audio"
                    );

            gift.setVoiceUrl(path);

        }



        // =========================
        // XOA ANH CU
        // Thao tac thang tren collection da load san (gift.getImages()),
        // KHONG loop goi lai giftRepository.findById() cho tung anh.
        // Nho orphanRemoval = true tren Gift.images, Hibernate se tu
        // DELETE cac dong bi remove khoi collection khi flush.
        // =========================

        if(deleteImages != null && !deleteImages.isEmpty()){

            gift.getImages().removeIf(
                    img -> deleteImages.contains(img.getImageUrl())
            );

        }



        // =========================
        // THEM ANH MOI
        // =========================

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

                    gift.addImage(img);

                }

            }

        }



        // gift dang managed trong transaction -> Hibernate tu dirty-check
        // va flush (UPDATE gift, DELETE anh bi xoa, INSERT anh moi) khi commit.
        // KHONG can goi giftRepository.save(gift) nua.
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