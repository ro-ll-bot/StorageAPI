package com.rollbot.fileapi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationTests {
    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders validHeader,invalidHeaderByName,invalidHeaderByKey = new HttpHeaders();

    private String validHeaderName,validHeaderValue,invalidHeaderName,invalidHeaderValue;

    @BeforeAll
    public void testEnvironment(){

        validHeaderName = "X-RollBot-Token";
        validHeaderValue = "e3225b7e-bf1a-11ea-b3de-0242ac130004;
        validHeader.set(validHeaderName,validHeaderValue);
        invalidHeaderName = "X-FaulBot-Token";
        invalidHeaderValue = "e9d0bfe2-bf1a-11ea-b3de-0242ac130004";
        invalidHeaderByName.set(invalidHeaderName,validHeaderValue);
        invalidHeaderByKey.set(validHeaderName,invalidHeaderValue);

    }
    @Test

    public void invalidHeaderName(){
        HttpEntity<String> entity = new HttpEntity<>(null, invalidHeaderByName);

        ResponseEntity<Resource> response = restTemplate.exchange(
                localhostUrl("/file/214"),
                HttpMethod.GET, entity, Resource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void invalidHeaderKey(){
        HttpEntity<String> entity = new HttpEntity<>(null, invalidHeaderByKey);

        ResponseEntity<Resource> response = restTemplate.exchange(
                localhostUrl("/file/214"),
                HttpMethod.GET, entity, Resource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void validHeader(){
        HttpEntity<String> entity = new HttpEntity<>(null, validHeader);

        ResponseEntity<Resource> response = restTemplate.exchange(
                localhostUrl("/file/214"),
                HttpMethod.GET, entity, Resource.class);

        assertThat(response.getStatusCode()).isNotEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    }

    private String localhostUrl(String uri) {
        return "http://localhost:"+port+uri;
    }
}
