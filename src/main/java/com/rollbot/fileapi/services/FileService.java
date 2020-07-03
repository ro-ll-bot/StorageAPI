package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    boolean uploadFile(MultipartFile file);
    File downloadFile();

}
