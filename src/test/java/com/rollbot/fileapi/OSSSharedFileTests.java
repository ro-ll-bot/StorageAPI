package com.rollbot.fileapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan("com")
public class OSSSharedFileTests {

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


    @Test
    public void shareFileTest(){

    }

    @Test
    public void accessSharedFileAllowed(){

    }

    @Test
    public void accessSharedFileNonAllowed(){

    }

    @Test
    public void downloadSharedFile(){

    }

    @Test
    public void updateSharedFileNonAllowed(){

    }

    @Test
    public void deleteSharedFileNonAllowed(){

    }

    @Test
    public void deleteSharedFileFromSharedUserAllowed(){

    }

    @Test
    public void downloadDateExpiredSharedFile(){

    }


    // Allowed operation... To check history - but it shouldn'T allow you to download files.
    @Test
    public void listDateExpiredSharedFile(){

    }
}
