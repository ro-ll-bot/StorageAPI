package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.OSSFile;
import org.springframework.web.multipart.MultipartFile;

public interface OSSFileService {

    String internalFilePath = "storage/oss/files/";

    OSSFile uploadFile(MultipartFile file);
    OSSFile downloadFile();

}
