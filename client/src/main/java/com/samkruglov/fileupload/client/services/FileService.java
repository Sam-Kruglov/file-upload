package com.samkruglov.fileupload.client.services;

import com.samkruglov.fileupload.client.FileFeignClient;
import com.samkruglov.fileupload.client.utils.ByteArrayMultipartFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class FileService {
    
    FileFeignClient fileClient;
    
    public Integer uploadFile(byte[] fileContents, String filename) {
        
        return fileClient.create(new ByteArrayMultipartFile(fileContents, "file", filename)).getBody();
    }
    
    public byte[] downloadFile(Integer fileId) throws IOException {
        
        try ( InputStream in = fileClient.downloadFile(fileId).getBody().getInputStream() ) {
            return IOUtils.toByteArray(in);
        }
    }
}
