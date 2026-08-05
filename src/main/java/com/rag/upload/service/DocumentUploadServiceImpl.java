package com.rag.upload.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rag.upload.dto.DocumentUploadRequest;
import com.rag.upload.dto.DocumentUploadResponse;
import com.rag.upload.entity.Document;
import com.rag.upload.entity.STATUS;
import com.rag.upload.repository.DocumentRepository;
import com.rag.upload.service.filegenerator.FileNameGenerator;
import com.rag.upload.service.storage.StorageService;
import com.rag.upload.service.validation.FileValidationService;
import com.rag.upload.service.mapper.DocumentMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentUploadServiceImpl
        implements DocumentUploadService {

    private final FileValidationService validationService;
    private final FileNameGenerator fileNameGenerator;
    private final StorageService storageService;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public DocumentUploadResponse upload(
            DocumentUploadRequest request)
            throws IOException {

        MultipartFile file = request.getFile();

        validationService.validate(file);

        String storedFileName = fileNameGenerator.generate(file);

        String storagePath = storageService.store(file, storedFileName);

        try {

            Document document = documentMapper.toEntity(
                    file,
                    storedFileName,
                    storagePath);

            Document savedDocument = documentRepository.save(document);

            return documentMapper.toUploadResponse(savedDocument);

        } catch (Exception ex) {

            storageService.delete(storagePath);

            throw ex;

        }

    }

}