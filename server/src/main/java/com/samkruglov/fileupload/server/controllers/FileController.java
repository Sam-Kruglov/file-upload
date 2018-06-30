package com.samkruglov.fileupload.server.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;

@RestController
@RequestMapping("api/v1/files")
public class FileController {
    
    private Map<Integer, byte[]> files = new Hashtable<>();
    
    @PostMapping
    public ResponseEntity<Integer> uploadFile(@RequestParam("file") @Valid @NotNull MultipartFile file) throws
                                                                                                        IOException {
    
        int fileId = files.size();
        files.put(fileId, file.getBytes());
        return ResponseEntity.ok(fileId);
    }
    
    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileId") @Valid @NotNull Integer fileId)
            throws UnsupportedEncodingException {
        
        byte[] file = files.get(fileId);
        
        return ResponseEntity.ok()
                             .header("Content-Disposition", "attachment; filename="
                                                            + URLEncoder.encode("file#" + fileId, "UTF-8"))
                             .header("Content-Description", "File Transfer")
                             .header("Content-Transfer-Encoding", "binary")
                             .contentLength(file.length)
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .body(new InputStreamResource(new ByteArrayInputStream(file)));
    }
}
