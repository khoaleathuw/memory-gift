package com.example.demo.service;


import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;
import com.example.demo.repository.GiftImageRepository;
import com.example.demo.repository.GiftRepository;

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




    // ==========================
    // SAVE
    // ==========================

    public Gift saveGift(
            String message,
            MultipartFile[] images,
            MultipartFile audio
    ) throws IOException {



        Gift gift = new Gift();


        gift.setMessage(message);



        // lưu gift trước để có id

        gift = giftRepository.save(gift);



        System.out.println(
                "SAVE GIFT ID = "
                + gift.getId()
        );




        // AUDIO

        if(audio != null && !audio.isEmpty()){


            String audioPath =
                    saveFile(
                            audio,
                            "audio"
                    );


            gift.setVoiceUrl(audioPath);

        }





        // IMAGE

        if(images != null){


            for(MultipartFile image : images){


                if(image != null && !image.isEmpty()){


                    String imagePath =
                            saveFile(
                                    image,
                                    "images"
                            );



                    GiftImage giftImage =
                            new GiftImage();



                    giftImage.setImageUrl(
                            imagePath
                    );


                    giftImage.setGift(
                            gift
                    );



                    giftImageRepository.save(
                            giftImage
                    );


                    gift.getImages()
                            .add(giftImage);


                }

            }

        }




        return giftRepository.save(gift);

    }







    // ==========================
    // UPDATE
    // ==========================

    public Gift updateGift(
            Long id,
            String message,
            MultipartFile[] images,
            MultipartFile audio
    ) throws IOException {



        Gift gift =
                giftRepository.findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Không tìm thấy gift id: "
                        + id
                    )
                );



        gift.setMessage(message);




        if(audio != null && !audio.isEmpty()){


            gift.setVoiceUrl(
                    saveFile(
                            audio,
                            "audio"
                    )
            );

        }




        if(images != null){


            for(MultipartFile image : images){


                if(!image.isEmpty()){


                    GiftImage giftImage =
                            new GiftImage();



                    giftImage.setImageUrl(
                            saveFile(
                                    image,
                                    "images"
                            )
                    );


                    giftImage.setGift(
                            gift
                    );



                    giftImageRepository.save(
                            giftImage
                    );


                    gift.getImages()
                            .add(giftImage);

                }

            }

        }



        return giftRepository.save(gift);

    }






    // ==========================
    // GET ID
    // ==========================

    public Gift getById(Long id){


        return giftRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Không tìm thấy gift id: "
                        + id
                    )
                );

    }






    // ==========================
    // GET UID (sau dùng NFC)
    // ==========================

    public Gift getByUid(String uid){


        return giftRepository
                .findByUid(uid)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Không tìm thấy gift"
                    )
                );

    }







    // ==========================
    // SAVE FILE
    // ==========================

    private String saveFile(
            MultipartFile file,
            String folder
    ) throws IOException {



        Path directory =
                Paths.get(
                        UPLOAD_DIR + folder
                );



        if(!Files.exists(directory)){

            Files.createDirectories(directory);

        }




        String filename =
                UUID.randomUUID()
                + "_"
                + file.getOriginalFilename();




        Files.copy(
                file.getInputStream(),
                directory.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
        );



        return "/files/"
                + folder
                + "/"
                + filename;

    }

}