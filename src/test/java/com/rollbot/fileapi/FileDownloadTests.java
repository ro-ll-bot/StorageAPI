package com.rollbot.fileapi;
import static org.assertj.core.api.Assertions.assertThat;

import com.rollbot.fileapi.entity.File;
import com.rollbot.fileapi.services.FileService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

@SpringBootTest
public class FileDownloadTests {

    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders validHeader= new HttpHeaders();
    private FileService fileService;
    private String validHeaderName,validHeaderValue;
    protected  File exceptedFile;

    @BeforeAll
    public void testEnvironment(){

        validHeaderName = "X-RollBot-Token";
        validHeaderValue = "e3225b7e-bf1a-11ea-b3de-0242ac130004;
        validHeader.set(validHeaderName,validHeaderValue);

        //eceptedFile setter no setter methods
        exceptedFile = new File();
    }

    @Test
    public void existDownloadFile(){

        HttpEntity<String> entity = new HttpEntity<>(null, validHeader);

        ResponseEntity<Resource> response = restTemplate.exchange(
                localhostUrl("/file/214"),
                HttpMethod.GET, entity, Resource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response).isEqualToComparingFieldByField(exceptedFile);

        assertThat(response.getHeaders().getContentType()).isEqualTo(exceptedFile.fileType());

        assertThat(response.getHeaders().getContentDisposition()).isEqualTo("attachment; filename=\"" + exceptedFile.getName() + "\"");
    }

    @Test
    public void deosntExistDownloadFile(){

        HttpEntity<String> entity = new HttpEntity<>(null, validHeader);
        //we try to call doesn’t exist file
        ResponseEntity<Resource> response = restTemplate.exchange(
                localhostUrl("/file/noexistfile"),
                HttpMethod.GET, entity, Resource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String localhostUrl(String uri) {
        return "http://localhost:"+port+uri;
    }
}
