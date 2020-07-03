package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.File;
import com.rollbot.fileapi.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public boolean uploadFile(MultipartFile file) {
        // First upload file from rest API.
        // Get file informations for local storage.
        File f = null;
        // fileRepository.saveAndFlush(f);
        return false;
    }

    @Override
    public File downloadFile() {
        return null;
    }
}
