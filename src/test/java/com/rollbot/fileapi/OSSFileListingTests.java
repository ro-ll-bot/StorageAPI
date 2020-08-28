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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ComponentScan("fileapi")
public class OSSFileListingTests {

    @Autowired
    private OSSFileService ossFileService;
    @Autowired private OSSFileRepository ossFileRepository;
    private final int userId = 1;
    private final String filename = "icon.png";
    private final HttpStatus status = HttpStatus.OK;
    private OSSFile ossFile = null;
    private final String path = "storage/test/icon.png";
    // Add more files here. Maybe add a list of files.
    private final String internalTestUploadFileDirectory = ossFileService.internalFilePath;
    private final String bucket = "bucket_"+userId+"/";

    @BeforeEach
    public void prepareTestCase(){
        // Here add some files to the system.
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
    public void listFilesTest(){

        Assert.notNull(ossFileService, "File service null");

        List<OSSFile> files = ossFileService.listFiles(userId);
        // We only added one file.
        Assert.isTrue(files.size() == 1, "Only one file added but, "+files.size() + " file found.");

        Assert.notNull(files.get(0));
        OSSFile actualFile = files.get(0);
        Optional<OSSFile> optionalExpectedFile = ossFileRepository.findByFilePath(internalTestUploadFileDirectory+bucket+filename);
        Assert.isTrue(optionalExpectedFile.isPresent(), "File should be in the database, but can't found!");

        OSSFile expectedFile = optionalExpectedFile.get();

        Assert.state(actualFile.equals(expectedFile), "Actual file= "+actualFile.toString()+"\n, Expected File= "+expectedFile.toString());
    }



}
