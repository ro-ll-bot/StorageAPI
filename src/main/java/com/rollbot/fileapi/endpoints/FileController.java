package com.rollbot.fileapi.endpoints;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.services.OSSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("api/file-service/")
public class FileController {

    @Autowired private OSSFileService ossFileService;

    @PostMapping("upload-file")
    public OSSFile uploadFile(@RequestParam MultipartFile multipartFile,
                              @RequestParam int userId,
                              @RequestParam String description){

        OSSFile file = ossFileService.uploadFile(multipartFile, userId, description);
        return file;
    }

}
