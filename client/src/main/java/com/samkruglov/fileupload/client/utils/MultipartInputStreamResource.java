package com.samkruglov.fileupload.client.utils;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.Objects;

/**
 * Similar to {@link InputStreamResource} but is able to return the stream any number of
 * times. Also filename and content length are explicit.
 */
public class MultipartInputStreamResource extends AbstractResource {
    
    private final InputStream inputStream;
    
    private final String description;
    
    private final String filename;
    
    private final long length;
    
    /**
     * Create a new InputStreamResource.
     *
     * @param inputStream the InputStream to use. Expected to be already opened.
     *
     * @see InputStreamResource#InputStreamResource(InputStream)
     */
    public MultipartInputStreamResource(InputStream inputStream, String filename, long length) {
        
        this(inputStream, filename, length, "resource loaded through InputStream");
    }
    
    /**
     * Create a new InputStreamResource.
     *
     * @param inputStream the InputStream to use. Expected to be already opened.
     * @param description where the InputStream comes from
     *
     * @see InputStreamResource#InputStreamResource(InputStream, String)
     */
    public MultipartInputStreamResource(InputStream inputStream, String filename, long length, String description) {
        
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(filename, "filename");
        
        this.inputStream = inputStream;
        this.filename = filename;
        this.length = length;
        this.description = (description != null ? description : "");
    }
    
    /**
     * This implementation always returns {@code true}.
     *
     * @see InputStreamResource#exists()
     */
    @Override
    public boolean exists() {
        
        return true;
    }
    
    /**
     * This implementation always returns {@code true}.
     *
     * @see InputStreamResource#isOpen()
     */
    @Override
    public boolean isOpen() {
        
        return true;
    }
    
    @Override
    public InputStream getInputStream() {
        
        return this.inputStream;
    }
    
    /**
     * This implementation returns a description that includes the passed-in description, if any.
     *
     * @see InputStreamResource#getDescription()
     */
    @Override
    public String getDescription() {
        
        return "InputStream resource [" + this.description + "]";
    }
    
    /**
     * @see AbstractResource#contentLength()
     */
    @Override
    public long contentLength() {
        
        return length;
    }
    
    @Override
    public String getFilename() {
        
        return filename;
    }
    
    /**
     * This implementation compares the underlying InputStream.
     *
     * @see InputStreamResource#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        
        return (obj == this
                || (obj instanceof MultipartInputStreamResource
                    && ((MultipartInputStreamResource) obj).inputStream.equals(this.inputStream)));
    }
    
    /**
     * This implementation returns the hash code of the underlying InputStream.
     *
     * @see InputStreamResource#hashCode()
     */
    @Override
    public int hashCode() {
        
        return this.inputStream.hashCode();
    }
}
