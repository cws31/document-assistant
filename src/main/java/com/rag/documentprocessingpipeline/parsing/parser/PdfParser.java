package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
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
public class PdfParser extends AbstractDocumentParser {

    private final ParsedDocumentFactory parsedDocumentFactory;

    @Override
    public FileType supportedType() {
        return FileType.PDF;
    }

    @Override
    public ParsedDocument parse(
            Document document,
            InputStream inputStream) {

        log.info(
                "Starting PDF parsing. documentId={}",
                document.getId());

        try {

            PDDocument pdfDocument = loadPdf(inputStream);

            try (pdfDocument) {

                String extractedText = extractText(pdfDocument);

                ParsedMetadata metadata = extractMetadata(pdfDocument);

                ParsedDocument parsedDocument = parsedDocumentFactory.create(
                        document,
                        extractedText,
                        metadata);

                log.info(
                        "Successfully parsed PDF. documentId={}",
                        document.getId());

                return parsedDocument;

            }

        } catch (IOException ex) {

            log.error(
                    "Failed to parse PDF. documentId={}",
                    document.getId(),
                    ex);

            throw new ParsingException(
                    "Unable to parse PDF document.",
                    ex);

        }

    }

    private PDDocument loadPdf(
            InputStream inputStream)
            throws IOException {

        byte[] pdfBytes = inputStream.readAllBytes();

        return Loader.loadPDF(pdfBytes);

    }

    private String extractText(
            PDDocument pdfDocument)
            throws IOException {

        PDFTextStripper textStripper = new PDFTextStripper();

        String extractedText = normalize(textStripper.getText(pdfDocument));

        validateExtractedText(extractedText);

        return extractedText;

    }

    private ParsedMetadata extractMetadata(
            PDDocument pdfDocument) {

        PDDocumentInformation information = pdfDocument.getDocumentInformation();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.TITLE,
                information.getTitle());

        attributes.put(
                MetadataKeys.AUTHOR,
                information.getAuthor());

        attributes.put(
                MetadataKeys.PAGE_COUNT,
                pdfDocument.getNumberOfPages());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}