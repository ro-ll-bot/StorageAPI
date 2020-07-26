
package com.rollbot.fileapi.endpoints;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.services.OSSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("api/file-service/")
public class FileController {

    @Autowired private OSSFileService ossFileService;

    @PostMapping("upload-file")
    public OSSFile uploadFile(@RequestParam MultipartFile multipartFile,
                              @RequestParam int userId,
                              @RequestParam(defaultValue = "null") String description){
        if(description.equals("null")){}

        OSSFile file = ossFileService.uploadFile(multipartFile, userId, description);
        return file;
    }

    @GetMapping("download-file/{filename:.+}")
    //@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename){
        return ossFileService.downloadFile(1, filename);
    }

}
