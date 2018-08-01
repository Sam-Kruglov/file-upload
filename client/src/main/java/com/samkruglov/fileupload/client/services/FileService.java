package com.samkruglov.fileupload.client.services;

import com.samkruglov.fileupload.client.services.daos.FileFeignDao;
import com.samkruglov.fileupload.client.services.daos.FileTemplateDao;
import com.samkruglov.fileupload.client.utils.ByteArrayMultipartFile;
import com.samkruglov.fileupload.client.utils.MultipartInputStreamResource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class FileService {
    
    FileFeignDao feign;
    
    FileTemplateDao template;
    
    public Integer uploadFile(@NonNull byte[] fileContents, @NonNull String filename) {
        
        return feign.create(new ByteArrayMultipartFile(fileContents, "file", filename)).getBody();
    }
    
    /**
     * Uploads.
     */
    public Integer streamFileOut(@NonNull InputStream fileContents, @NonNull String filename,
                                 @NonNull long contentLength, String desc) {
        
        return template.postFile(new MultipartInputStreamResource(fileContents, filename, contentLength, desc));
    }
    
    /**
     * Downloads.
     */
    public InputStream streamFileIn(@NonNull Integer fileId) {
        
        return template.getFile(fileId);
    }
    
    public byte[] downloadFile(@NonNull Integer fileId) {
        
        return feign.downloadFile(fileId).getBody();
    }
}
