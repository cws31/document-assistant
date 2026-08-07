package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
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
public class PdfParser extends AbstractDocumentParser {

        private final ParsedDocumentFactory parsedDocumentFactory;

        private final AnalysisPipeline analysisPipeline;

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

                                AnalysisContext context = AnalysisContext.builder()
                                                .document(document)
                                                .fileType(FileType.PDF)
                                                .sourceDocument(pdfDocument)
                                                .build();

                                AnalysisResult analysisResult = analysisPipeline.analyze(
                                                context);

                                ParsedDocument parsedDocument = parsedDocumentFactory.create(
                                                document,
                                                analysisResult.getContent(),
                                                analysisResult.getMetadata());

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

}