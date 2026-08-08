package com.rag.upload.service.validation;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidationService {

    void validate(MultipartFile file);

}