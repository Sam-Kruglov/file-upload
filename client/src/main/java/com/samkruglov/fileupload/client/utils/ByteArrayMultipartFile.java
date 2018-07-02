package com.samkruglov.fileupload.client.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * todo there may a better way that works out of the box using
 * {@link feign.form.spring.converter.ByteArrayMultipartFile}
 */
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ByteArrayMultipartFile implements MultipartFile {
    
    @NotNull
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
        
        return bytes.length == 0;
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
        
        try ( FileOutputStream out = new FileOutputStream(dest) ) {
            out.write(bytes);
        }
    }
}