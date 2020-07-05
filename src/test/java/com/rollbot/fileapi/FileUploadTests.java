package com.rollbot.fileapi;


import com.rollbot.fileapi.services.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUploadTests {


  @Autowired private FileService fileService;
  private String filename = null;
  private String content = null;


  @BeforeAll
  public void configureTestResources(){

    filename = "";
    content = "";

    try {
      generateTxtFileToTest(filename, content);
    } catch (IOException e) {
      assert false: e.getMessage();
    }


  }

  private String generateTxtFileToTest(String filename, String data) throws IOException {
    /*
    * This method will generate a file to home directory. To test upload
    * services. You can generate this file with any data you want to.
    * And you can generate any file type.
    * */
    String path = filename; // You can configure the path here.
    File file = new File(path);
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write(data);
    fileWriter.close();

    // After writing operation done.
    // Test the file if not exists assert.
    File generatedFile = new File(path);
    assert generatedFile.exists(): "File couldn't generate.";

    return data;
  }


  @AfterEach
  public void deleteTestResources(){
    // Delete all stuff generated to test this class.
    File originalFile = new File(filename);
    File uploadedFile = new File("/files/"+filename);

    Assert.isTrue(originalFile.exists(), "Original file not exists");
    Assert.isTrue(uploadedFile.exists(), "Uploaded file not exists");

    Assert.isTrue(originalFile.delete(), "Original file couldn't delete.");
    Assert.isTrue(uploadedFile.delete(), "Uploaded file couldn't delete.");

  }


  @Test
  public void fileUploadIOTest(){

    Assert.notNull(filename, "BeforeAll configuration annotation didn't work");
    Assert.notNull(content, "BeforeAll configuration annotation didn't work");
    Assert.notNull(fileService, "File service couldn't autowired check wiring connections or initialize the file service automatically.");


    FileInputStream fileInputStream = null;
    MultipartFile multipartFile = null;
    try {
      fileInputStream = new FileInputStream(filename);
      multipartFile = new MockMultipartFile(filename, fileInputStream);
    } catch (FileNotFoundException e) {
      // File input stream error
      assert false: "File input stream is not working correct!";
      e.printStackTrace();
    }catch (IOException e){
      // Mock multi part error
      assert false: "Mock multipart file is not working correct!";
      e.printStackTrace();
    }
    // Instead of testing upload service via http triggers.
    // Test this functionality via FileService.
    assert fileService.uploadFile(multipartFile): "File couldn't upload to server.";

    // Now check the uploaded file
    File uploadedFile = new File("/files/"+filename);
    assert uploadedFile.exists(): "File upload process couldn't accomplished. There is no file created at /files/$[fname]";

    // Read uploaded file.
    FileReader fileReader = null;
    char[] uploadedFileContent = null;
    try {
      fileReader = new FileReader(uploadedFile);
      fileReader.read(uploadedFileContent);

    } catch (FileNotFoundException e) {
      assert false: "A problem occurred while opening the uploaded file.";
    }catch (IOException e){
      assert false: "A problem occurred while reading the uploaded file.";
    }

    // Check file contents here.
    char[] originalContent = content.toCharArray();
    Assert.notNull(originalContent, "File original content shouldn't be null.");

    assert originalContent==uploadedFileContent: "File contents are not the same.";

  }

  @Test
  public void fileUploadDBTest(){

  }
}

