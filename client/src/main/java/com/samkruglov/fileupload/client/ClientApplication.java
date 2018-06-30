package com.samkruglov.fileupload.client;

import com.samkruglov.fileupload.client.services.FileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SpringBootApplication
@EnableFeignClients
public class ClientApplication {
    
    FileService service;
    
    public static void main(String[] args) {
        
        SpringApplication.run(ClientApplication.class, args);
    }
    
    @PostConstruct
    public void init() throws IOException, URISyntaxException {
        
        String filename = "bla.txt";
        byte[] file     = Files.readAllBytes(Paths.get(getClass().getResource("/" + filename).toURI()));
        //todo JsonMappingException: No serializer found for class java.io.ByteArrayInputStream
        Integer fileId       = service.uploadFile(file, filename);
        String  fileContents = new String(service.downloadFile(fileId));
        
        System.out.println(fileContents);
    }
}
