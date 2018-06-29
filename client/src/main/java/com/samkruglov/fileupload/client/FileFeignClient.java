package com.samkruglov.fileupload.client;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

//https://github.com/OpenFeign/feign-form#spring-multipartfile-and-spring-cloud-netflix-feignclient-support
@FeignClient(name = "file-storage", configuration = FileFeignClient.MultipartSupportConfig.class)
public interface FileFeignClient {
    
    @PostMapping("/api/v1/file")
    ResponseEntity create(@RequestParam("file") MultipartFile file);
    
    @PutMapping("/api/v1/file/{fileId}")
    ResponseEntity<InputStreamResource> update(@RequestParam("file") MultipartFile file,
                                               @PathVariable("fileId") String fileId);
    
    class MultipartSupportConfig {
        
        @SuppressWarnings("SpringJavaAutowiredMembersInspection")
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;
        
        @Bean
        public Encoder feignFormEncoder() {
            
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }
}
