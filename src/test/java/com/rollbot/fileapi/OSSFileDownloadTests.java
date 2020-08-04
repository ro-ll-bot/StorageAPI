package com.rollbot.fileapi;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.repositories.OSSFileRepository;
import com.rollbot.fileapi.services.OSSFileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@SpringBootTest
@ComponentScan("com")
public class OSSFileDownloadTests {

    @Autowired private OSSFileService ossFileService;
    @Autowired private OSSFileRepository ossFileRepository;
    private final int userId = 1;
    private final String filename = "icon.png";
    private final String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final String contentDisposition = "inline; filename=\"icon.png\"";
    private final HttpStatus status = HttpStatus.OK;
    private final String internalTestUploadFileDirectory = ossFileService.internalFilePath;
    private final String bucket = "bucket_"+userId+"/";

    private OSSFile ossFile = null;
    private final String path = "storage/test/icon.png";


    @BeforeEach
    public void uploadFileToTestDownload(){
        /**
         * Upload a file to the server to test the download functionality.
         */
        MultipartFile multipartFile = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(this.path);
            multipartFile = new MockMultipartFile(filename, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ossFile = ossFileService.uploadFile(multipartFile, userId, null);
    }

    @AfterEach
    public void clearTestData(){
        File uploadedFile = new File(internalTestUploadFileDirectory+bucket+filename);

        Assert.isTrue(uploadedFile.exists(), "Uploaded file not exists");
        Assert.isTrue(uploadedFile.delete(), "Uploaded file couldn't delete.");
        // Also clean db information too.
        ossFileRepository.delete(this.ossFile);
    }

    @Test
    public void fileDownloadTest(){
        /**
         * To run this test first of all, we need to upload a file.
         * We can easily upload file with the codes from OSSFileUploadTests.
         *
         * After uploading a file, we will try to download that file.
         * I mean we will get the response from our service. And we will try to use
         * that resource.
         */
        ResponseEntity<Resource> responseEntity = ossFileService.downloadFile(userId, filename);

        ByteArrayResource resource = (ByteArrayResource) responseEntity.getBody();
        assert resource.getByteArray()!=null;

        assert responseEntity.getStatusCode() == status;
        assert responseEntity.getHeaders().containsKey("content-length");
        assert responseEntity.getHeaders().containsKey("content-disposition");

        // assert String.valueOf(responseEntity.getHeaders().getContentType()).equals(contentType);
        // assert responseEntity.getHeaders().get("content-length") == resource.getByteArray().length;
        // assert responseEntity.getHeaders().get("content-disposition").equals(contentDisposition);
        System.out.println("Expected: " +contentType);
        System.out.println("Actual: " + responseEntity.getHeaders().getContentType());
        System.out.println();
        System.out.println("Expected: " + resource.getByteArray().length);
        System.out.println("Actual: "+ responseEntity.getHeaders().get("content-length"));
        System.out.println();
        System.out.println("Expected: "+ contentDisposition);
        System.out.println("Actual: "+ responseEntity.getHeaders().get("content-disposition"));
        System.out.println("\n");

        for(String key : responseEntity.getHeaders().toSingleValueMap().keySet())
            System.out.println(key+ " -> " + responseEntity.getHeaders().toSingleValueMap().get(key));


        /*
        Write more tests.
        Read file and compare the byte content.
         */


    }
}
