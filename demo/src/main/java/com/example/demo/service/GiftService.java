package com.example.demo.service;


import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.GiftImageRepository;

import org.springframework.stereotype.Service;
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

    public Gift saveGift(

            String message,

            MultipartFile[] images,

            MultipartFile audio

    ) throws IOException {



        Gift gift = new Gift();


        gift.setMessage(message);



        // lưu gift trước
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


                    giftImage.setGift(gift);



                    giftImageRepository.save(
                            giftImage
                    );


                }

            }

        }



        return giftRepository.save(gift);

    }








    // =========================
    // UPDATE
    // =========================


    public Gift updateGift(

            Long id,

            String message,

            MultipartFile[] images,

            MultipartFile audio

    ) throws IOException {



        Gift gift =
                giftRepository.findById(id)
                .orElseThrow();



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


                    GiftImage img =
                            new GiftImage();


                    img.setImageUrl(
                            saveFile(
                                    image,
                                    "images"
                            )
                    );


                    img.setGift(gift);



                    giftImageRepository.save(img);


                }

            }

        }



        return giftRepository.save(gift);

    }








    // =========================
    // GET
    // =========================


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