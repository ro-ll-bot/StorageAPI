package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.entity.OSSFileShareInfo;
import com.rollbot.fileapi.entity.OSSShared;
import com.sun.istack.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OSSFileService {

    String internalFilePath = "storage/oss/files/";

    OSSFile uploadFile(MultipartFile file, int userId, @Nullable String description);
    ResponseEntity<Resource> downloadFile(int userId, String filename);
    ResponseEntity<Resource> downloadSharedFile(int sharedUserId, int ownerUserId, String filename);

    // File link
    ResponseEntity<OSSShared> shareFile(OSSFileShareInfo shareInfo);

    List<OSSFile> listFiles(int userId);
    List<OSSShared> listSharedFiles(int userId); // Check expired date.
}
