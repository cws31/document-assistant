package com.rag.upload.service.storage;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.rag.upload.entity.Document;

@Component
public interface StorageService {

    String store(
            MultipartFile file,
            String storedFileName)
            throws IOException;

    InputStream read(Document document)
            throws IOException;

    void delete(String storagePath)
            throws IOException;

}