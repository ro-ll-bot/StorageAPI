package com.rollbot.fileapi;

import com.rollbot.fileapi.entity.OSSFile;
import com.rollbot.fileapi.entity.OSSFileShareInfo;
import com.rollbot.fileapi.entity.OSSShared;
import com.rollbot.fileapi.repositories.OSSFileRepository;
import com.rollbot.fileapi.repositories.OSSShareRepository;
import com.rollbot.fileapi.services.OSSFileService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
@ComponentScan("fileapi")
public class OSSSharedFileTests {


    private final String pathExtension = "storage/test/";
    private final String[] filenameList1 = new String[]{"file1.txt", "file2.txt", "file3.txt", "file4.txt"};
    private final String[] filenameList2 = new String[]{"file5.txt", "file6.txt", "file7.txt", "file8.txt"};
    private final int[] userIdList = new int[]{1, 2, 3, 4};

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<OSSFile> ossFileList = new ArrayList<>();

    /*
     *
     * ----- First Test -----
     * Share a file with a user.
     * ---- Second Test ----
     * Try to reach a shared file from another user.
     * ---- Third Test -----
     * Try to reach a shared file that you're not allowed.
     * ---- Fourth Test -----
     * Download shared file.
     *
     *  */
    @Autowired private OSSFileService ossFileService;
    @Autowired private OSSFileRepository ossFileRepository;
    @Autowired private OSSShareRepository ossShareRepository;

    @BeforeAll
    public void prepareTestData() {


        try {
            // Create files inside for loop.
            OSSFile f = null;
            for (int i = 0; i < this.filenameList1.length; ++i) {
                FileInputStream fileInputStream1 = new FileInputStream(this.pathExtension + this.filenameList1[0]);
                MultipartFile multipartFile1 = new MockMultipartFile(this.filenameList1[i], fileInputStream1);
                String sampleText1 = String.format("file= %s, user= %s", this.filenameList1[i], this.userIdList[i]);
                f = ossFileService.uploadFile(multipartFile1, this.userIdList[i], sampleText1);
                logger.info(sampleText1);
                ossFileList.add(f);

                FileInputStream fileInputStream2 = new FileInputStream(this.pathExtension + this.filenameList2[0]);
                MultipartFile multipartFile2 = new MockMultipartFile(this.filenameList2[i], fileInputStream2);
                String sampleText2 = String.format("file= %s, user= %s", this.filenameList2[i], this.userIdList[i]);
                f = ossFileService.uploadFile(multipartFile2, this.userIdList[i], "");
                logger.info(sampleText2);
                ossFileList.add(f);
            }
            // Files uploaded.
            // Maybe control files..


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String filenameCreator(int userId, String filename){
        return new StringBuilder()
                .append("storage/oss/files/bucket_")
                .append(userId)
                .append("/")
                .append(filename).toString();
    }

    @AfterAll
    public void clearTestData(){
        for(OSSFile ossF : ossFileList){
            // Clear file from the system
            // Clear file from the db.
            File file = new File(this.filenameCreator(ossF.getUserId(), ossF.getName()));
            logger.info("File deletion status= " + file.delete());
            ossFileRepository.delete(ossF);
            logger.info("File deleted from database..");
        }
    }


    @Test
    public void shareFileTest() {
        /*
        * User 1 has files;
        * file1.txt and file5.txt
        * fileElement.get(0), fileElement.get(1)
        *
        * User 4 has files;
        * file4.txt and file8.txt
        * fileElement.get(fileElement.size()-1), fileElement.get(fileElement.size()-2);
        *
        * The scenario is--
        * User 1 will share file1.txt with the User4
        * And we will control if all info is OK.
        * */
        OSSFileShareInfo.Builder ossFileShareInfoBuilder = new OSSFileShareInfo.Builder(1, 4, "file1.txt");
        ossFileShareInfoBuilder.addTitle("File Sharing")
                .addDescription("Checkout this new file...")
                .sendMail(false)
                .setExpireTime(null);

        OSSFileShareInfo ossFileShareInfo = ossFileShareInfoBuilder.build();

        Assert.notNull(ossFileShareInfo, "OSSFileShareInfo couldn't created...");
        // Maybe check ossFileShareInfo fields.

        ResponseEntity<OSSShared> ossSharedResponseEntity = ossFileService.shareFile(ossFileShareInfo);
        Assert.notNull(ossSharedResponseEntity, "OSSShared Entity returned null.");

        Assert.state(ossSharedResponseEntity.getStatusCode() == HttpStatus.OK, "Response entity :: HttpStatus.OK Expected but "+
                ossSharedResponseEntity.getStatusCode()+" returrned.");

        OSSShared sharedObjectFromResponseEntity = ossSharedResponseEntity.getBody();

        // Get the shared file information from database
        Optional<OSSShared> sharedOptionalObjectFromDb = ossShareRepository.findBySharedUserIdAndSharedFile_FilePath(
                sharedObjectFromResponseEntity.getSharedUserId(),
                sharedObjectFromResponseEntity.getSharedFile().getFilePath()
        );

        Assert.isTrue(sharedOptionalObjectFromDb.isPresent(), "Share information couldn't find on database.");

        OSSShared sharedObjectFromDb = sharedOptionalObjectFromDb.get();

        Assert.state(sharedObjectFromDb.equals(sharedObjectFromResponseEntity), "They're not the same object.");

    }

    @Test
    public void accessSharedFileAllowed() {

    }

    @Test
    public void accessSharedFileNonAllowed() {

    }

    @Test
    public void downloadSharedFile() {

    }

    @Test
    public void updateSharedFileNonAllowed() {

    }

    @Test
    public void deleteSharedFileNonAllowed() {

    }

    @Test
    public void deleteSharedFileFromSharedUserAllowed() {

    }

    @Test
    public void downloadDateExpiredSharedFile() {

    }


    // Allowed operation... To check history - but it shouldn'T allow you to download files.
    @Test
    public void listDateExpiredSharedFile() {

    }
}
