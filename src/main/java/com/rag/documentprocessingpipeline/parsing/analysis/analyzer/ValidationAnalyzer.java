package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

@Component
@Order(1000)
public class ValidationAnalyzer
        extends AbstractDocumentAnalyzer {

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        if (context.getDocument() == null) {
            throw new ParsingException(
                    "Document is missing.");
        }

        if (context.getMetadata() == null) {
            throw new ParsingException(
                    "Metadata extraction failed.");
        }

        String text = context.getContent();

        if (text == null || text.isBlank()) {
            throw new ParsingException(
                    "Document does not contain readable text.");
        }

    }

}