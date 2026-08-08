package com.rag.upload.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.documentprocessingpipeline.parsing.service.ParsingService;
import com.rag.upload.dto.DocumentUploadRequest;
import com.rag.upload.dto.DocumentUploadResponse;
import com.rag.upload.entity.Document;
import com.rag.upload.repository.DocumentRepository;
import com.rag.upload.service.filegenerator.FileNameGenerator;
import com.rag.upload.service.mapper.DocumentMapper;
import com.rag.upload.service.storage.StorageService;
import com.rag.upload.service.validation.FileValidationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    private final ParsingService parsingService;

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

            ParsedDocument parsedDocument = parsingService.parse(savedDocument);

            log.info("========================================");
            log.info("Document parsed successfully.");
            log.info("Document Id : {}", savedDocument.getId());

            log.info("------------ Parsed Content ------------");
            log.info("\n{}", parsedDocument.getContent());

            log.info("------------ Parsed Metadata -----------");
            log.info("{}", parsedDocument.getMetadata());

            log.info("========================================");

            return documentMapper.toUploadResponse(savedDocument);

        } catch (Exception ex) {

            storageService.delete(storagePath);

            log.error(
                    "Document upload failed. Stored file has been deleted.",
                    ex);

            throw ex;

        }

    }

}