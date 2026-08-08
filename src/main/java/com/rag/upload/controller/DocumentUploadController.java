package com.rag.upload.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.rag.upload.dto.DocumentUploadRequest;
import com.rag.upload.dto.DocumentUploadResponse;
import com.rag.upload.service.DocumentUploadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Validated
public class DocumentUploadController {

    private final DocumentUploadService documentUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @Valid @ModelAttribute DocumentUploadRequest request)
            throws IOException {

        DocumentUploadResponse response = documentUploadService.upload(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}