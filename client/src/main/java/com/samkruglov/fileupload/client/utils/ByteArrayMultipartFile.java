package com.samkruglov.fileupload.client.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ByteArrayMultipartFile implements MultipartFile {
    
    @Getter
    byte[] bytes;
    
    String paramName;
    
    String filename;
    
    @Override
    public String getName() {
        
        return paramName;
    }
    
    @Override
    public String getOriginalFilename() {
        
        return filename;
    }
    
    @Override
    public String getContentType() {
        
        return MediaType.MULTIPART_FORM_DATA_VALUE;
    }
    
    @Override
    public boolean isEmpty() {
        
        return ArrayUtils.isEmpty(bytes);
    }
    
    @Override
    public long getSize() {
        
        return bytes.length;
    }
    
    @Override
    public InputStream getInputStream() {
        
        return new ByteArrayInputStream(bytes);
    }
    
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        
        new FileOutputStream(dest).write(bytes);
    }
}