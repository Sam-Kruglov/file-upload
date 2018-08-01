package com.samkruglov.fileupload.client.configs;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    
    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedStreamingRestTemplate() {
        
        RestTemplate restTemplate = new RestTemplate();
        
        //this factory is the only place where an instance of StreamingHttpOutputMessage is created
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // the StreamingHttpOutputMessage is only created if this is false
        requestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(requestFactory);
        
        return restTemplate;
    }
}