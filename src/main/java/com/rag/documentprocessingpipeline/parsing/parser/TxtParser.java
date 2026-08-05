package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.factory.ParsedDocumentFactory;
import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.model.MetadataKeys;
import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;
import com.rag.upload.entity.Document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TxtParser extends AbstractDocumentParser {

    private final ParsedDocumentFactory parsedDocumentFactory;

    @Override
    public FileType supportedType() {
        return FileType.TXT;
    }

    @Override
    public ParsedDocument parse(
            Document document,
            InputStream inputStream) {

        log.info(
                "Starting TXT parsing. documentId={}",
                document.getId());

        try {

            String extractedText = extractText(inputStream);

            ParsedMetadata metadata = extractMetadata(document);

            ParsedDocument parsedDocument = parsedDocumentFactory.create(
                    document,
                    extractedText,
                    metadata);

            log.info(
                    "Successfully parsed TXT document. documentId={}",
                    document.getId());

            return parsedDocument;

        } catch (IOException ex) {

            log.error(
                    "Failed to parse TXT document. documentId={}",
                    document.getId(),
                    ex);

            throw new ParsingException(
                    "Unable to parse TXT document.",
                    ex);

        }

    }

    private String extractText(
            InputStream inputStream)
            throws IOException {

        String extractedText = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8);

        extractedText = normalize(extractedText);

        validateExtractedText(extractedText);

        return extractedText;

    }

    private ParsedMetadata extractMetadata(
            Document document) {

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.TITLE,
                document.getOriginalFileName());

        attributes.put(
                "encoding",
                StandardCharsets.UTF_8.name());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}