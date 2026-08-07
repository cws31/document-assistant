package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.model.AnalysisResult;
import com.rag.documentprocessingpipeline.parsing.analysis.pipeline.AnalysisPipeline;
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

        private final AnalysisPipeline analysisPipeline;

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

                        AnalysisContext context = AnalysisContext.builder()
                                        .document(document)
                                        .fileType(FileType.XLSX)
                                        .sourceDocument(workbook)
                                        .build();

                        AnalysisResult result = analysisPipeline.analyze(context);

                        return parsedDocumentFactory.create(
                                        document,
                                        result.getContent(),
                                        result.getMetadata());

                } catch (Exception ex) {

                        throw new ParsingException(
                                        "Unable to parse XLSX document.",
                                        ex);

                }

        }

}