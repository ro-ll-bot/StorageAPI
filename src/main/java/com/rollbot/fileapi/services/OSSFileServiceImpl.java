package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.repositories.OSSFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Configurable
@Service
public class OSSFileServiceImpl implements OSSFileService {

    @Autowired private OSSFileRepository fileRepository;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public OSSFile uploadFile(MultipartFile file) {

        String log = new StringBuilder()
                .append("Original file name: ")
                .append(file.getOriginalFilename())
                .append(".\nIs file empty: ")
                .append(file.isEmpty())
                .append(".\nFile name: ")
                .append(file.getName())
                .append(".\nContent type: ")
                .append(file.getContentType())
                .toString();
        logger.info(log);

        if(file.getOriginalFilename()==null || file.isEmpty() || file.getName()==null) return null;

        try {
            byte[] bytes = file.getBytes();
            if(bytes==null) return null;

            logger.info("Write file to path: "+internalFilePath+file.getName());
            Path path = Paths.get(internalFilePath + file.getName());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warning("File writing process couldn't completed!");
            return null;
        }

        logger.info("Writing file information to system completed successfully. Now add file informations to database.");
        // Add file information to DB via help of the fileRepository
        // If this method shows any error, we have to control file types.
        String []splitted = file.getName().split("[.]");
        String extension = "None";
        if(splitted!=null) extension = splitted[splitted.length-1];
        OSSFile ossFile = new OSSFile.Builder(file.getName())
                .userId(1)
                .path(internalFilePath + file.getName())
                .absoluteFilePath("system:"+internalFilePath+file.getOriginalFilename())
                .size(file.getSize())
                .extension(extension)
                .description("description")
                .build();

        fileRepository.save(ossFile);

        return ossFile;
    }

    @Override
    public OSSFile downloadFile() {
        return null;
    }
}
