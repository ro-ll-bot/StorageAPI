package com.rollbot.fileapi;


import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.entity.OSSFileTrustedTypes;
import com.rollbot.fileapi.repositories.OSSFileRepository;
import com.rollbot.fileapi.services.OSSFileService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootTest
@ComponentScan("com")
public class OSSFileUploadTests {


  private final Logger logger = Logger.getLogger(this.getClass().getName());
  @Autowired private OSSFileService ossFileService;
  @Autowired private OSSFileRepository ossFileRepository;
  private String filename = null;
  private byte[] content = null;
  private OSSFile ossFile = null;
  private static final int userId = 1;
  private static final String description = null;
  private String bucket = "bucket_"+userId+"/";
  // Internal test upload file directory
  private String internalTestUploadFileDirectory = ossFileService.internalFilePath;

  // Absolute path for testing
  private final String path = "storage/test/icon.png";


  @BeforeEach
  public void testConfiguration(){
    this.logger.info("Test configuration started.");

    try{
      Pair<String, byte[]> fileBytes= readFileBytes(this.path);
      Assert.notNull(fileBytes, "Pair is null");
      this.filename = fileBytes.getKey();
      this.content = fileBytes.getValue();
    }catch (IOException e){
      // e.printStackTrace();
      Assert.isTrue(false, "A problem occurred while reading file.");
    }
  }

  // You can choose to use this function for testing.
  private Pair<String, byte[]> readFileBytes(String path) throws IOException{

    File file = new File(path);
    if(!file.exists()) throw new IOException();
    String fName = file.getName();
    Assert.notNull(fName, "Filename is empty");

    FileInputStream fileInputStream = null;

    byte[] bytes = new byte[(int)file.length()];
    try{
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytes);
      fileInputStream.close();
    }catch (IOException e){
      e.printStackTrace();
    }

    return new Pair<>(fName, bytes);
  }



  @AfterEach
  public void deleteTestResources(){
    // Delete all stuff generated to test this class.
    File uploadedFile = new File(internalTestUploadFileDirectory+bucket+filename);

    Assert.isTrue(uploadedFile.exists(), "Uploaded file not exists");
    logger.info("File found at the location: "+uploadedFile.getAbsolutePath());
    Assert.isTrue(uploadedFile.delete(), "Uploaded file couldn't delete.");
    logger.info("File deleted successfully!");

    // Also clean db information too.
    ossFileRepository.delete(this.ossFile);
  }


  @Test
  public void fileUploadIOTest(){

    /*
    *
    * EXPLANATION OF THIS TEST:
    *
    * 1 - Preparation Phase
    * 2 - Test Phase
    * 3 - Cleaning Phase
    *
    * /// Preparation Phase
    * Initialize a filename with a type and a content for the file.
    * Other configuration's can be written here. Example you can import a file from your local environment.
    * And if you choose to generate your own file with the string content here. You can easily use
    * generateTxtFile function. This function will generate a file to your home directory.
    * The function will control if this file generated successfully or not!
    *
    * /// Test Phase
    * First it will control the variables we have to use for testing.
    * Then MultipartFile will be created and if any problem occurs at creation test will fail.
    * After mock file successfully created we will use fileService to upload file to the internal file system.
    * If the function returns false, then the file uploading operation fails also test fails to.
    * If file uploads successfully without problem, we will check the files directory to control if the file exists.
    * If file exists we will compare our generated file's and the uploaded file's content
    *
    * /// Cleaning Phase
    * In cleaning phase we will delete the files we generated and uploaded or delete only uploaded files.
    * Then we will control if file's removed successfully.
    * */

    Assert.notNull(this.internalTestUploadFileDirectory, "Initialize an internal file directory for testing!");
    Assert.notNull(this.filename, "BeforeAll configuration annotation didn't work");
    Assert.notNull(this.content, "BeforeAll configuration annotation didn't work");
    Assert.notNull(this.ossFileService, "File service couldn't autowired check wiring connections or initialize the file service automatically.");

    logger.info("Test filename: "+this.filename);
    String[] splitFile = this.filename.split("[.]");
    Assert.notNull(splitFile, "Extension getting operation couldn't accomplished.");
    String extension = splitFile[splitFile.length-1];
    try {
      OSSFileTrustedTypes type = OSSFileTrustedTypes.valueOf(extension.toUpperCase());
      this.logger.info("Trusted file type detected. File type: "+extension);
    }catch (IllegalArgumentException e){
      this.logger.warning( "Untrusted file type detected. File type: "+extension);
    }


    FileInputStream fileInputStream = null;
    MultipartFile multipartFile = null;
    try {
      fileInputStream = new FileInputStream(this.path);
      multipartFile = new MockMultipartFile(this.filename, fileInputStream);
    } catch (FileNotFoundException e) {
      // File input stream error
      Assert.isTrue(false, "File input stream is not working correct!");
      e.printStackTrace();
    }catch (IOException e){
      // Mock multi part error
      Assert.isTrue(false, "Mock multipart file is not working correct!");
      e.printStackTrace();
    }
    // Instead of testing upload service via http triggers.
    // Test this functionality via FileService.
    this.ossFile = this.ossFileService.uploadFile(multipartFile, userId, description);
    Assert.notNull(this.ossFile,"File couldn't upload to server.");

    // Now check the uploaded file
    File uploadedFile = new File(this.internalTestUploadFileDirectory + this.bucket+ this.filename);
    Assert.isTrue(uploadedFile.exists(), "File upload process couldn't accomplished. There is no file created at /${this.internatlTest}/$[fname]");

    // Read uploaded file.
    byte[] uploadedFileContent = null;
    String uploadedFileName = null;
    try {
      Pair<String, byte[]> uploadedFilePair = readFileBytes(this.internalTestUploadFileDirectory + this.bucket+ this.filename);
      Assert.notNull(uploadedFilePair, "Uploaded file pair is null");
      uploadedFileName = uploadedFilePair.getKey();
      logger.info("Uploaded File name: " + uploadedFileName+", Actual file name: "+this.filename );
      uploadedFileContent = uploadedFilePair.getValue();
    } catch (IOException e) {
      e.printStackTrace();
      Assert.isTrue(false, "A problem occurred while reading file.");
    }

    Assert.state(this.filename.equals(uploadedFileName), "File names are different!");
    Assert.state(this.content.length == uploadedFileContent.length, "File contents are not the same!");
    Assert.state(Arrays.equals(this.content, uploadedFileContent), "File contents are not the same!");

  }


  @Test
  public void fileUploadDBTest(){

    FileInputStream fileInputStream = null;
    MultipartFile multipartFile = null;
    try {
      fileInputStream = new FileInputStream(this.path);
      multipartFile = new MockMultipartFile(this.filename, fileInputStream);
    } catch (FileNotFoundException e) {
      // File input stream error
      Assert.isTrue(false, "File input stream is not working correct!");
      e.printStackTrace();
    }catch (IOException e){
      // Mock multi part error
      Assert.isTrue(false, "Mock multipart file is not working correct!");
      e.printStackTrace();
    }
    // Instead of testing upload service via http triggers.
    // Test this functionality via FileService.
    this.ossFile = this.ossFileService.uploadFile(multipartFile, userId, description);
    Assert.notNull(this.ossFile,"File couldn't upload to server.");

    OSSFile fromDB = null;
    Optional<OSSFile> optionalOSSFile = ossFileRepository.findByFilePath(this.ossFile.getFilePath());
    Assert.isTrue(optionalOSSFile.isPresent(), "OSSFile couldn't find at database.");
    fromDB = optionalOSSFile.get();
    logger.fine("OSSFile successfully fetched from database. File id: "+fromDB.getId());


    Assert.state(fromDB.getSize() == this.ossFile.getSize(), "File size's are not same.");
    Assert.state(Objects.equals(fromDB.getUserId(), this.ossFile.getUserId()), "File userId's are not the same.");
    Assert.state(Objects.equals(fromDB.getName(), this.ossFile.getName()), "File name's are not the same");
    Assert.state(Objects.equals(fromDB.getDescription(), this.ossFile.getDescription()), "File description's are not the sameç");
    Assert.state(Objects.equals(fromDB.getFilePath(), this.ossFile.getFilePath()), "File path's are not the same.");
    Assert.state(Objects.equals(fromDB.getAbsoluteFilePath(), this.ossFile.getAbsoluteFilePath()), "Absolute file path's are not the same.");
    Assert.state(Objects.equals(fromDB.getUploadTime().getTime(), this.ossFile.getUploadTime().getTime()), "File upload time's are not the same.");
    Assert.state(Objects.equals(fromDB.getCreateTime().getTime(), this.ossFile.getCreateTime().getTime()), "File created time's are not the same.");
    if(fromDB.getUpdateTime()==null) Assert.isNull(this.ossFile.getUpdateTime());
    else Assert.state(Objects.equals(fromDB.getUpdateTime().getTime(), this.ossFile.getUpdateTime().getTime()), "File update time's are not the same.");
    Assert.state(Objects.equals(fromDB.getExtension(), this.ossFile.getExtension()), "File extension's are not the same");
  }

}

