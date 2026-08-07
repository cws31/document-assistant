package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.model.AnalysisResult;
import com.rag.documentprocessingpipeline.parsing.analysis.pipeline.AnalysisPipeline;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.factory.ParsedDocumentFactory;
import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.upload.entity.Document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocxParser extends AbstractDocumentParser {

        private final ParsedDocumentFactory parsedDocumentFactory;

        private final AnalysisPipeline analysisPipeline;

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

                try (XWPFDocument wordDocument = new XWPFDocument(inputStream)) {

                        AnalysisContext context = AnalysisContext.builder()
                                        .document(document)
                                        .fileType(FileType.DOCX)
                                        .sourceDocument(wordDocument)
                                        .build();

                        AnalysisResult result = analysisPipeline.analyze(context);

                        ParsedDocument parsedDocument = parsedDocumentFactory.create(
                                        document,
                                        result.getContent(),
                                        result.getMetadata());

                        log.info(
                                        "Successfully parsed DOCX document. documentId={}",
                                        document.getId());

                        return parsedDocument;

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

}