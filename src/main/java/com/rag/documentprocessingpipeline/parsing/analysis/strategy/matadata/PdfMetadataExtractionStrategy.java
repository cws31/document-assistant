package com.rag.documentprocessingpipeline.parsing.analysis.strategy.matadata;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.model.MetadataKeys;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;

@Component
public class PdfMetadataExtractionStrategy
        implements MetadataExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof PDDocument;

    }

    @Override
    public ParsedMetadata extract(
            AnalysisContext context) {

        PDDocument pdfDocument = (PDDocument) context.getSourceDocument();

        PDDocumentInformation information = pdfDocument.getDocumentInformation();

        Map<String, Object> attributes = new LinkedHashMap<>();

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