package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.constants.AnalysisAttributes;
import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.image.ImageExtractionStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(200)
@RequiredArgsConstructor
public class ImageAnalyzer
                extends AbstractDocumentAnalyzer {

        private final List<ImageExtractionStrategy> strategies;

        @Override
        protected void doAnalyze(
                        AnalysisContext context) {

                ImageExtractionStrategy strategy = strategies.stream()
                                .filter(s -> s.supports(context))
                                .findFirst()
                                .orElse(null);

                if (strategy == null) {

                        context.setImages(List.of());

                        context.getAttributes().put(
                                        AnalysisAttributes.IMAGE_FOUND,
                                        false);

                        log.debug(
                                        "No image extraction strategy registered. " +
                                                        "documentId={}, fileType={}",
                                        context.getDocument().getId(),
                                        context.getFileType());

                        return;
                }

                List<BufferedImage> images = strategy.extract(context);

                if (images == null) {
                        images = List.of();
                }

                context.setImages(images);

                boolean imageFound = !images.isEmpty();

                context.getAttributes().put(
                                AnalysisAttributes.IMAGE_FOUND,
                                imageFound);

                log.info(
                                "Image analysis completed. " +
                                                "documentId={}, fileType={}, imageFound={}, imageCount={}",
                                context.getDocument().getId(),
                                context.getFileType(),
                                imageFound,
                                images.size());
        }
}