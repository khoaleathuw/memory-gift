package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Path BASE_DIR =
            Paths.get(System.getProperty("user.home"), "memory-gift", "uploads");

    @GetMapping("/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getFile(

            @PathVariable String folder,

            @PathVariable String filename

    ) {

        try {

            Path file = BASE_DIR.resolve(folder).resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();

        }

    }

}