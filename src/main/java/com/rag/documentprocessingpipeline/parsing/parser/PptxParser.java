package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
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
public class PptxParser extends AbstractDocumentParser {

        private final ParsedDocumentFactory parsedDocumentFactory;

        private final AnalysisPipeline analysisPipeline;

        @Override
        public FileType supportedType() {
                return FileType.PPTX;
        }

        @Override
        public ParsedDocument parse(
                        Document document,
                        InputStream inputStream) {

                log.info(
                                "Starting PPTX parsing. documentId={}",
                                document.getId());

                try (XMLSlideShow slideShow = new XMLSlideShow(inputStream)) {

                        AnalysisContext context = AnalysisContext.builder()
                                        .document(document)
                                        .fileType(FileType.PPTX)
                                        .sourceDocument(slideShow)
                                        .build();

                        AnalysisResult result = analysisPipeline.analyze(context);

                        ParsedDocument parsedDocument = parsedDocumentFactory.create(
                                        document,
                                        result.getContent(),
                                        result.getMetadata());

                        log.info(
                                        "Successfully parsed PPTX document. documentId={}",
                                        document.getId());

                        return parsedDocument;

                } catch (IOException ex) {

                        throw new ParsingException(
                                        "Unable to parse PPTX document.",
                                        ex);

                }

        }

}