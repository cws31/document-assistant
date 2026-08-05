package com.rag.upload.service.storage;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface StorageService {

    String store(
            MultipartFile file,
            String storedFileName) throws IOException;

    void delete(String storagePath) throws IOException;

}