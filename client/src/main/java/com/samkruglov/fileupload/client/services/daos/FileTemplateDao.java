package com.samkruglov.fileupload.client.services.daos;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

/**
 * Overcomes Feign's limitation for streaming data. Feign cannot do proper streaming of data because it hardcodes its
 * {@link HttpOutputMessage} to {@link org.springframework.cloud.netflix.feign.support.SpringEncoder.FeignOutputMessage}
 * while internally Spring distinguishes this output message class by whether it is an instance of {@link
 * StreamingHttpOutputMessage} or not.
 */
@Component
public class FileTemplateDao {
    
    public static final String LOAD_BALANCED_FILE_STORAGE_BASE_URL = "http://file-storage/api/v1/files/";
    
    private final RestTemplate template;
    
    private final URI postUrl;
    
    private final Function<Integer, URI> putUrl;
    
    public FileTemplateDao(@Qualifier("loadBalancedStreamingRestTemplate") RestTemplate template) {
        
        postUrl = URI.create(LOAD_BALANCED_FILE_STORAGE_BASE_URL);
        
        putUrl = fileId -> UriComponentsBuilder.fromUriString(LOAD_BALANCED_FILE_STORAGE_BASE_URL + "/{fileId}")
                                               .buildAndExpand(fileId).toUri();
        
        this.template = template;
    }
    
    public Integer postFile(Resource resource) {
        
        return sendMultipartForInt(RequestEntity.post(postUrl), resource);
    }
    
    public Integer putFile(Integer fileId, Resource resource) {
        
        return sendMultipartForInt(RequestEntity.put(putUrl.apply(fileId)), resource);
    }
    
    public InputStream getFile(Integer fileId) {
        
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    private Integer sendMultipartForInt(BodyBuilder bodyBuilder, Resource resource) {
        
        RequestEntity<MultiValueMap<String, Object>> request = bodyBuilder
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(wrapInMultiValueMap(resource));
        
        return template.exchange(request, Integer.class).getBody();
    }
    
    private MultiValueMap<String, Object> wrapInMultiValueMap(Resource resource) {
        
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", resource);
        return bodyMap;
    }
}
