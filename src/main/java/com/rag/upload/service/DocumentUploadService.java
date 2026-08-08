package com.rag.upload.service;

import java.io.IOException;

import com.rag.upload.dto.DocumentUploadRequest;
import com.rag.upload.dto.DocumentUploadResponse;

public interface DocumentUploadService {

    DocumentUploadResponse upload(DocumentUploadRequest request)
            throws IOException;

}