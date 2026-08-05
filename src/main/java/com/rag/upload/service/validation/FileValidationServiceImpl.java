package com.rag.upload.service.validation;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rag.upload.config.UploadProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileValidationServiceImpl
        implements FileValidationService {

    private final UploadProperties uploadProperties;

    @Override
    public void validate(MultipartFile file) {

        validateNull(file);

        validateEmpty(file);

        validateSize(file);

        validateExtension(file);

        validateContentType(file);

    }

    private void validateContentType(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException(
                    "Content type could not be determined.");
        }

        if (!uploadProperties
                .getAllowedContentTypes()
                .contains(contentType)) {

            throw new IllegalArgumentException(
                    "Unsupported content type: " + contentType);

        }

    }

    private void validateExtension(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException(
                    "File extension could not be determined.");
        }

        String extension = originalFilename
                .substring(originalFilename.lastIndexOf('.') + 1)
                .toLowerCase();

        if (!uploadProperties.getAllowedExtensions().contains(extension)) {

            throw new IllegalArgumentException(
                    "Unsupported file extension: " + extension);

        }

    }

    private void validateSize(MultipartFile file) {

        long maxSize = uploadProperties
                .getMaxFileSize()
                .toBytes();

        if (file.getSize() > maxSize) {

            throw new IllegalArgumentException(
                    String.format(
                            "Maximum allowed file size is %s.",
                            uploadProperties.getMaxFileSize()));

        }

    }

    private void validateEmpty(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

    }

    private void validateNull(MultipartFile file) {

        if (file == null) {
            throw new IllegalArgumentException("Uploaded file must not be null.");
        }

    }

}