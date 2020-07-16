package com.rollbot.fileapi.endpoints;

import com.rollbot.fileapi.services.OSSFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
// @RequestMapping("")
public class FileController {

    private OSSFileService OSSFileService;

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file){


        if(file.getOriginalFilename() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try{
            byte[] bytes = file.getBytes();
            Path path = Paths.get(file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("File Name:"+ path.getFileName());

        }catch (IOException e){
            e.printStackTrace();
        }

        return new ResponseEntity<>("Good job!", HttpStatus.OK);
    }
}
