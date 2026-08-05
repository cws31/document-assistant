package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
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
public class XlsxParser extends AbstractDocumentParser {

    private final ParsedDocumentFactory parsedDocumentFactory;

    @Override
    public FileType supportedType() {
        return FileType.XLSX;
    }

    @Override
    public ParsedDocument parse(
            Document document,
            InputStream inputStream) {

        log.info(
                "Starting XLSX parsing. documentId={}",
                document.getId());

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            String extractedText = extractText(workbook);

            ParsedMetadata metadata = extractMetadata(workbook);

            ParsedDocument parsedDocument = parsedDocumentFactory.create(
                    document,
                    extractedText,
                    metadata);

            log.info(
                    "Successfully parsed XLSX document. documentId={}",
                    document.getId());

            return parsedDocument;

        } catch (Exception ex) {

            log.error(
                    "Failed to parse XLSX document. documentId={}",
                    document.getId(),
                    ex);

            throw new ParsingException(
                    "Unable to parse XLSX document.",
                    ex);

        }

    }

    private String extractText(
            Workbook workbook) {

        StringBuilder builder = new StringBuilder();

        DataFormatter formatter = new DataFormatter();

        for (Sheet sheet : workbook) {

            builder.append("Sheet: ")
                    .append(sheet.getSheetName())
                    .append(System.lineSeparator());

            for (Row row : sheet) {

                for (Cell cell : row) {

                    builder.append(
                            formatter.formatCellValue(cell))
                            .append(" ");

                }

                builder.append(System.lineSeparator());

            }

            builder.append(System.lineSeparator());

        }

        String extractedText = normalize(builder.toString());

        validateExtractedText(extractedText);

        return extractedText;

    }

    private ParsedMetadata extractMetadata(
            Workbook workbook) {

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.TITLE,
                workbook.getSheetName(0));

        attributes.put(
                "sheetCount",
                workbook.getNumberOfSheets());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}