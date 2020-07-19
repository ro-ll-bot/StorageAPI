package com.rollbot.fileapi;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.services.OSSFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@SpringBootTest
@ComponentScan("com")
public class OSSFileDownloadTests {

  @Autowired private OSSFileService ossFileService;
  private final int userId = 1;
  private final String filename = "questions.xlsx";
  private OSSFile ossFile = null;
  private final String path = "C:\\Users\\k84167261\\Desktop\\personal\\questions.xlsx";


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
    System.out.println(responseEntity.toString());
  }
}
