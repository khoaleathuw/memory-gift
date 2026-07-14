package com.example.demo.controller;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;


@RestController
public class FileController {


    private final String UPLOAD_DIR =
            System.getProperty("user.home")
            + "/memory-gift/uploads/";



    @GetMapping("/files/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getFile(

            @PathVariable String folder,

            @PathVariable String filename

    ){

        try{


            Path filePath =
                    Paths.get(
                        UPLOAD_DIR,
                        folder,
                        filename
                    ).normalize();



            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );



            if(!resource.exists()){
                return ResponseEntity.notFound().build();
            }



            String contentType =
                    Files.probeContentType(filePath);



            if(contentType == null){

                contentType =
                    folder.equals("audio")
                    ?
                    "audio/webm"
                    :
                    "image/jpeg";

            }



            return ResponseEntity.ok()
                    .contentType(
                        MediaType.parseMediaType(contentType)
                    )
                    .body(resource);



        }catch(Exception e){

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();

        }

    }

}