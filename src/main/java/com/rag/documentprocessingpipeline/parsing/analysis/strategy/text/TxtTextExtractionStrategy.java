package com.rag.documentprocessingpipeline.parsing.analysis.strategy.text;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

@Component
public class TxtTextExtractionStrategy
        implements TextExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof String;

    }

    @Override
    public String extract(
            AnalysisContext context) {

        return (String) context.getSourceDocument();

    }

}