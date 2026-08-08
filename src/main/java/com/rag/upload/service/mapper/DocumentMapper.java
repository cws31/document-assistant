package com.rag.upload.service.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.rag.upload.dto.DocumentUploadResponse;
import com.rag.upload.entity.Document;
import com.rag.upload.entity.STATUS;

@Component
public class DocumentMapper {

    public Document toEntity(
            MultipartFile file,
            String storedFileName,
            String storagePath) {

        String originalFileName = file.getOriginalFilename();

        return Document.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .fileExtension(extractExtension(originalFileName))
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .storagePath(storagePath)
                .checksum(null)
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(null)
                .deleted(false)
                .status(STATUS.UPLOADED)
                .build();
    }

    public DocumentUploadResponse toUploadResponse(Document document) {

        return DocumentUploadResponse.builder()
                .id(document.getId())
                .originalFileName(document.getOriginalFileName())
                .contentType(document.getContentType())
                .fileSize(document.getFileSize())
                .status(document.getStatus())
                .uploadedAt(document.getUploadedAt())
                .message("Document uploaded successfully.")
                .build();
    }

    private String extractExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf('.') + 1)
                .toLowerCase();
    }
}