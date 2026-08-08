package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.normalization.CompositeTextNormalizer;

import lombok.RequiredArgsConstructor;

@Component
@Order(600)
@RequiredArgsConstructor
public class TextNormalizationAnalyzer
        extends AbstractDocumentAnalyzer {

    private final CompositeTextNormalizer normalizer;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        String normalized = normalizer.normalize(
                context.getContent());

        context.setContent(normalized);

    }

}