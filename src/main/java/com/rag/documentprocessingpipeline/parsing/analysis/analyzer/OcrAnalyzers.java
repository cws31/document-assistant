package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.service.DocumentRenderer;
import com.rag.documentprocessingpipeline.parsing.analysis.service.DocumentRendererFactory;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.properties.OcrProperties;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.service.OcrService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(300)
@RequiredArgsConstructor
public class OcrAnalyzers
        extends AbstractDocumentAnalyzer {

    private final DocumentRendererFactory rendererFactory;

    private final OcrService ocrService;

    private final OcrProperties properties;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        if (!properties.isEnabled()) {

            log.debug("OCR is disabled.");

            return;

        }

        DocumentRenderer renderer = rendererFactory.getRenderer(
                context.getFileType());

        List<BufferedImage> images = renderer.render(
                context.getSourceDocument());

        if (images.isEmpty()) {
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (BufferedImage image : images) {

            String text = ocrService.extractText(image);

            if (!text.isBlank()) {

                builder.append(text)
                        .append(System.lineSeparator());

            }

        }

        String ocrText = builder.toString().trim();

        if (ocrText.isBlank()) {
            return;
        }

        String existing = context.getContent();

        if (existing == null || existing.isBlank()) {

            context.setContent(ocrText);

        } else {

            context.setContent(
                    existing
                            + System.lineSeparator()
                            + ocrText);

        }

        log.info(
                "OCR completed. documentId={}",
                context.getDocument().getId());

    }

}