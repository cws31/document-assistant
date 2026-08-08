package com.rag.upload.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentUploadRequest {

    @NotNull(message = "File is required.")
    private MultipartFile file;

}