package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.constants.AnalysisAttributes;
import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.text.TextExtractionStrategy;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class TextAnalyzer
        extends AbstractDocumentAnalyzer {

    private final List<TextExtractionStrategy> strategies;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        TextExtractionStrategy strategy = strategies.stream()
                .filter(s -> s.supports(context))
                .findFirst()
                .orElseThrow(() -> new ParsingException(
                        "No text extraction strategy found."));

        context.setContent(
                strategy.extract(context));

        String extractedText = strategy.extract(context);

        context.setContent(extractedText);

        context.getAttributes().put(
                AnalysisAttributes.TEXT_FOUND,
                !extractedText.isBlank());

    }

}