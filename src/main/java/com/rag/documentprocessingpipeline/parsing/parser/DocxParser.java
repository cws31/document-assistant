package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
public class DocxParser extends AbstractDocumentParser {

    private final ParsedDocumentFactory parsedDocumentFactory;

    @Override
    public FileType supportedType() {
        return FileType.DOCX;
    }

    @Override
    public ParsedDocument parse(
            Document document,
            InputStream inputStream) {

        log.info(
                "Starting DOCX parsing. documentId={}",
                document.getId());

        try {

            try (XWPFDocument wordDocument = new XWPFDocument(inputStream)) {

                String extractedText = extractText(wordDocument);

                ParsedMetadata metadata = extractMetadata(wordDocument);

                ParsedDocument parsedDocument = parsedDocumentFactory.create(
                        document,
                        extractedText,
                        metadata);

                log.info(
                        "Successfully parsed DOCX document. documentId={}",
                        document.getId());

                return parsedDocument;

            }

        } catch (IOException ex) {

            log.error(
                    "Failed to parse DOCX document. documentId={}",
                    document.getId(),
                    ex);

            throw new ParsingException(
                    "Unable to parse DOCX document.",
                    ex);

        }

    }

    private String extractText(
            XWPFDocument document) {

        StringBuilder builder = new StringBuilder();

        document.getParagraphs()
                .forEach(paragraph -> {

                    String text = paragraph.getText();

                    if (text != null && !text.isBlank()) {

                        builder.append(text)
                                .append(System.lineSeparator());

                    }

                });

        String extractedText = normalize(builder.toString());

        validateExtractedText(extractedText);

        return extractedText;

    }

    private ParsedMetadata extractMetadata(
            XWPFDocument document) {

        POIXMLProperties.CoreProperties properties = document.getProperties()
                .getCoreProperties();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.TITLE,
                properties.getTitle());

        attributes.put(
                MetadataKeys.AUTHOR,
                properties.getCreator());

        attributes.put(
                "paragraphCount",
                document.getParagraphs().size());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}