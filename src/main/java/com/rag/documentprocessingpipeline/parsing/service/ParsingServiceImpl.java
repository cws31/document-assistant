package com.rag.documentprocessingpipeline.parsing.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.factory.ParserFactory;
import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.documentprocessingpipeline.parsing.parser.DocumentParser;
import com.rag.upload.entity.Document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParsingServiceImpl
        implements ParsingService {

    private final ParserFactory parserFactory;

    @Override
    public ParsedDocument parse(
            Document document) {

        validateDocument(document);

        log.info(
                "Starting parsing. documentId={}",
                document.getId());

        Path filePath = Path.of(document.getStoragePath());

        try (InputStream inputStream = Files.newInputStream(filePath)) {

            DocumentParser parser = parserFactory.getParser(
                    document.getFileExtension());

            ParsedDocument parsedDocument = parser.parse(
                    document,
                    inputStream);

            log.info(
                    "Successfully parsed document. documentId={}",
                    document.getId());

            return parsedDocument;

        } catch (IOException ex) {

            log.error(
                    "Unable to parse document. documentId={}",
                    document.getId(),
                    ex);

            throw new ParsingException(
                    "Unable to read document from storage.",
                    ex);

        }

    }

    private void validateDocument(
            Document document) {

        if (document == null) {
            throw new IllegalArgumentException(
                    "Document cannot be null.");
        }

        if (document.getStoragePath() == null
                || document.getStoragePath().isBlank()) {

            throw new IllegalArgumentException(
                    "Storage path cannot be null or blank.");
        }

        if (document.getFileExtension() == null
                || document.getFileExtension().isBlank()) {

            throw new IllegalArgumentException(
                    "File extension cannot be null or blank.");
        }

    }

}