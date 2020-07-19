package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.OSSFile;
import com.sun.istack.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface OSSFileService {

    String internalFilePath = "storage/oss/files/";

    OSSFile uploadFile(MultipartFile file, int userId, @Nullable String description);
    ResponseEntity<Resource> downloadFile(int userId, String filename);
    // Configure it for shared files.
    OSSFile downloadFile(String uri);
}
