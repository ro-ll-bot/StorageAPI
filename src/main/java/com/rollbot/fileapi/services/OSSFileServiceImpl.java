package com.rollbot.fileapi.services;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.entity.OSSShared;
import com.rollbot.fileapi.repositories.OSSFileRepository;
import com.rollbot.fileapi.repositories.OSSShareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Configurable
@Service
public class OSSFileServiceImpl implements OSSFileService {

    @Autowired private OSSFileRepository fileRepository;
    @Autowired private OSSShareRepository shareRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String createBucketIfNotExits(int userId)  {
        String bucket = "bucket_"+userId;
        File file = new File(internalFilePath +bucket);
        if(!file.exists()){
            // If directory not exists, create a new one.
            file.mkdir();
        }

        return bucket;
    }

    @Override
    public OSSFile uploadFile(MultipartFile file, int userId,
                              @Nullable String description) {

        logger.debug("Original file name: " + file.getOriginalFilename());
        logger.debug("Is file empty: " + file.isEmpty());
        logger.debug("Content Type from multipart: " +  file.getContentType());
        logger.debug("Description: " + file.getResource().getDescription());
        logger.debug("Filename: " + file.getResource().getFilename());

        if(file.getOriginalFilename()==null || file.isEmpty() || file.getName()==null) return null;

        String fPath = new StringBuilder()
                .append(internalFilePath)
                .append(createBucketIfNotExits(userId))
                .append("/")
                .append(file.getName())
                .toString();

        try {
            byte[] bytes = file.getBytes();
            if(bytes==null) return null;


            logger.debug("Write file to path: "+fPath);
            Path path = Paths.get(fPath);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("File writing process couldn't completed!");
            return null;
        }

        logger.info("Writing file information to system completed successfully. Now add file informations to database.");
        // Add file information to DB via help of the fileRepository
        // If this method shows any error, we have to control file types.
        String mimeType = null;
        try {
            mimeType = Files.probeContentType(Paths.get(fPath));
            logger.info(Paths.get(fPath).toUri().toString());
            logger.info("Mime type found: "+ mimeType);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("MimeType couldn't find of the file.");
        }

        String []splitted = file.getName().split("[.]");
        String extension = "None";
        if(splitted!=null) extension = splitted[splitted.length-1];
        OSSFile ossFile = new OSSFile.Builder(file.getName())
                .userId(userId)
                .path(fPath)
                .absoluteFilePath("system:"+fPath)
                .size(file.getSize())
                .extension(extension)
                .description(description)
                .mimeType(mimeType)
                .build();

        logger.info("OSSFile built:" + ossFile.toString());

        fileRepository.save(ossFile);

        return ossFile;
    }

    private String generateFilePathFromFilename(int userId, String filename){
        return new StringBuilder()
                .append(internalFilePath)
                .append(createBucketIfNotExits(userId))
                .append("/")
                .append(filename)
                .toString();
    }

    private ByteArrayResource generateByteArrayResource(String fpath) throws IOException{
        byte[] bytes;
        File f = new File(fpath);
        // Print a response here, because the file exists in the database but it is not exists in directory.
        if(!f.exists()) throw new IOException();
        bytes = Files.readAllBytes(f.toPath());

        ByteArrayResource resource = new ByteArrayResource(bytes);

        return resource;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(int userId, String filename) {
        // find file
        String fpath = generateFilePathFromFilename(userId, filename);

        Optional<OSSFile> ossFileOptional = fileRepository.findByFilePath(fpath);
        if(!ossFileOptional.isPresent()) return ResponseEntity.notFound().build();

        OSSFile ossFile = ossFileOptional.get();

        ByteArrayResource resource = null;
        try {
            resource = generateByteArrayResource(fpath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-disposition", "inline; filename=\""+ossFile.getName()+"\"");
        httpHeaders.set("content-length", String.valueOf(ossFile.getSize()));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(ossFile.getSize())
                .contentType(MediaType.asMediaType(MimeType.valueOf(ossFile.getMimeType())))
                .body(resource);
    }

    @Override
    public ResponseEntity<Resource> downloadSharedFile(int sharedUserId, int ownerUserId, String filename) {
        return null;
    }

/*
    @Override
    public ResponseEntity<Resource> downloadSharedFile(int sharedUserId, int ownerUserId, String filename) {
        String fpath = generateFilePathFromFilename(ownerUserId, filename);
        Optional<OSSShared> optionalOSSShared = shareRepository.findBySharedUserIdAndSharedFile_FilePath(sharedUserId, fpath);
        if(!optionalOSSShared.isPresent()) return ResponseEntity.notFound().build();

        OSSShared ossShared = optionalOSSShared.get();

        ByteArrayResource resource = null;
        try {
            resource = generateByteArrayResource(fpath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        OSSFile ossFile = ossShared.getSharedFile();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-disposition", "inline; filename=\""+ ossFile.getName()+"\"");
        httpHeaders.set("content-length", String.valueOf(ossFile.getSize()));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(ossFile.getSize())
                .contentType(MediaType.asMediaType(MimeType.valueOf(ossFile.getMimeType())))
                .body(resource);
    }
*/
    @Override
    public List<OSSFile> listFiles(int userId) {
        return fileRepository.findAllByUserId(userId);
    }

    @Override
    public List<OSSShared> listSharedFiles(int sharedUserId) {
        return shareRepository.findAllBySharedUserId(sharedUserId);
    }

}
