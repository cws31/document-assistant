package com.rag.upload.dto;

import java.time.LocalDateTime;

import com.rag.upload.entity.STATUS;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DocumentUploadResponse {

    private Long id;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private STATUS status;

    private LocalDateTime uploadedAt;

    private String message;

}