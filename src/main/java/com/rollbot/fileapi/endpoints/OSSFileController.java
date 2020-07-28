
package com.rollbot.fileapi.endpoints;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.entity.OSSShared;
import com.rollbot.fileapi.services.OSSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/file-service/")
public class OSSFileController {

    @Autowired private OSSFileService ossFileService;

    @PostMapping("upload-file")
    public OSSFile uploadFile(@RequestParam MultipartFile multipartFile,
                              @RequestParam int userId,
                              @RequestParam(defaultValue = "null") String description){
        // Make this request body
        if(description.equals("null")){}

        return ossFileService.uploadFile(multipartFile, userId, description);
    }

    @GetMapping("download-file/{filename:.+}")
    //@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename){
        return ossFileService.downloadFile(1, filename);
    }

    @GetMapping("files")
    public List<OSSFile> getAllFiles(@RequestParam int userId){
        return ossFileService.listFiles(userId);
    }

    @GetMapping("shared-files/")
    public List<OSSShared> getAllSharedFiles(@RequestParam int userId){
        return ossFileService.listSharedFiles(userId);
    }

    @PutMapping("update-file")
    public void updateFileData(@RequestParam int userId, @RequestParam String filename,
                               @RequestBody Map<String, Object> body){

        // Update file according to given data.
        // Instead of editing files real names we can create a mock filename
        // to represent a file.
        OSSFile file = null;
        for(String key: body.keySet()){

        }
    }

}
