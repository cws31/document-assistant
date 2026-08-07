package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
public class TxtParser extends AbstractDocumentParser {

        private final ParsedDocumentFactory parsedDocumentFactory;

        private final AnalysisPipeline analysisPipeline;

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

                        String text = new String(
                                        inputStream.readAllBytes(),
                                        StandardCharsets.UTF_8);

                        AnalysisContext context = AnalysisContext.builder()
                                        .document(document)
                                        .fileType(FileType.TXT)
                                        .sourceDocument(text)
                                        .build();

                        AnalysisResult result = analysisPipeline.analyze(context);

                        return parsedDocumentFactory.create(
                                        document,
                                        result.getContent(),
                                        result.getMetadata());

                } catch (IOException ex) {

                        throw new ParsingException(
                                        "Unable to parse TXT document.",
                                        ex);

                }

        }

}