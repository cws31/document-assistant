package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.matadata.MetadataExtractionStrategy;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

import lombok.RequiredArgsConstructor;

@Component
@Order(2)
@RequiredArgsConstructor
public class MetadataAnalyzer
        extends AbstractDocumentAnalyzer {

    private final List<MetadataExtractionStrategy> strategies;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        MetadataExtractionStrategy strategy = strategies.stream()
                .filter(s -> s.supports(context))
                .findFirst()
                .orElseThrow(() -> new ParsingException(
                        "No metadata extraction strategy found."));

        context.setMetadata(
                strategy.extract(context));

    }

}