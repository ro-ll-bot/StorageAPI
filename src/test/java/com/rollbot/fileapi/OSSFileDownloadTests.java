package com.rollbot.fileapi;

import com.rollbot.fileapi.services.OSSFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OSSFileDownloadTests {

  @Autowired private OSSFileService ossFileService;


  @BeforeEach
  public void uploadFileToTestDownload(){

  }

  @Test
  public void fileDownloadTest(){
    ossFileService.downloadFile();
  }
}
