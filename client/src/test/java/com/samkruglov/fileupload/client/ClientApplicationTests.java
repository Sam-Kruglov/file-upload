package com.samkruglov.fileupload.client;

import com.samkruglov.fileupload.client.ClientApplicationTests.TestApp;
import com.samkruglov.fileupload.client.services.FileService;
import com.samkruglov.fileupload.server.controllers.FileController;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class,
        properties = { "file-storage.ribbon.listOfServers=localhost:8080", "server.port=8080" },
        webEnvironment = WebEnvironment.DEFINED_PORT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientApplicationTests {
    
    @Autowired
    FileService service;
    
    @Autowired
    FileController controller;
    
    @Test
    public void bytesUpload() {
        
        // ----- given -----
        String originalString = "bla";
        byte[] originalBytes  = originalString.getBytes(StandardCharsets.UTF_8);
        
        // ----- when -----
        Integer fileId = service.uploadFile(originalBytes, "bla.txt");
        
        // ----- then -----
        byte[] actualBytes = controller.files.get(fileId);
        assertThat(actualBytes).isEqualTo(originalBytes);
    }

    @Test
    public void bytesDownload() {
    
        // ----- given -----
        String originalString = "bla";
        byte[] originalBytes  = originalString.getBytes(StandardCharsets.UTF_8);
        int    fileId         = 0;
        controller.files.put(fileId, originalBytes);
    
        byte[] actualBytes = service.downloadFile(fileId);
        assertThat(actualBytes).isEqualTo(originalBytes);
    }
    
    @Test
    public void streamingUpload() throws IOException {
        
        // ----- given -----
        String originalString = "bla";
        byte[] originalBytes  = originalString.getBytes(StandardCharsets.UTF_8);
        
        // ----- when -----
        Integer fileId;
        try ( ByteArrayInputStream in = new ByteArrayInputStream(originalBytes) ) {
            fileId = service.streamFileOut(in, "bla.txt", originalBytes.length, null);
        }
        
        // ----- then -----
        byte[] actualBytes = controller.files.get(fileId);
        assertThat(actualBytes).isEqualTo(originalBytes);
    }
    
    @Test
    public void streamingDownload() throws IOException {
        
        // ----- given -----
        String originalString = "bla";
        byte[] originalBytes  = originalString.getBytes(StandardCharsets.UTF_8);
        int    fileId         = 0;
        controller.files.put(fileId, originalBytes);
        
        byte[] actualBytes = IOUtils.toByteArray(service.streamFileIn(fileId));
        assertThat(actualBytes).isEqualTo(originalString);
    }
    
    @SpringBootApplication(scanBasePackageClasses = { ClientApplication.class, FileController.class })
    @EnableFeignClients
    public static class TestApp {}
}
