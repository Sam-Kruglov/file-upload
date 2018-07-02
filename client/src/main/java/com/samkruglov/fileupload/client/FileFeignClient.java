package com.samkruglov.fileupload.client;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

//https://github.com/OpenFeign/feign-form#spring-multipartfile-and-spring-cloud-netflix-feignclient-support
@FeignClient(name = "file-storage", configuration = FileFeignClient.MultipartSupportConfig.class)
public interface FileFeignClient {
    
    @PostMapping(value = "/api/v1/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Integer> create(@RequestPart("file") MultipartFile file);
    
    @GetMapping("/{fileId}")
    ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileId") Integer fileId);
    
    class MultipartSupportConfig {
        
        @Bean
        public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
            
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }
}
