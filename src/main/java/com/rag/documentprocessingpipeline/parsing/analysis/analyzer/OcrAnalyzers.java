package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.constants.AnalysisAttributes;
import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
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

    private final OcrService ocrService;

    private final OcrProperties properties;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        Long documentId =
                context.getDocument().getId();

        /*
         * OCR can be globally disabled.
         */
        if (!properties.isEnabled()) {

            log.debug(
                    "OCR is disabled. documentId={}",
                    documentId);

            context.getAttributes().put(
                    AnalysisAttributes.OCR_REQUIRED,
                    false);

            return;
        }

        /*
         * Images must already have been extracted
         * by ImageAnalyzer.
         */
        List<BufferedImage> images =
                context.getImages();

        if (images == null || images.isEmpty()) {

            log.debug(
                    "No images available for OCR. documentId={}",
                    documentId);

            context.getAttributes().put(
                    AnalysisAttributes.OCR_REQUIRED,
                    false);

            return;
        }

        StringBuilder ocrBuilder =
                new StringBuilder();

        int processedImages = 0;
        int successfulImages = 0;

        for (int index = 0;
             index < images.size();
             index++) {

            BufferedImage image =
                    images.get(index);

            if (!isUsableImage(image)) {

                log.debug(
                        "Skipping unusable image. documentId={}, imageIndex={}",
                        documentId,
                        index);

                continue;
            }

            processedImages++;

            try {

                String text =
                        ocrService.extractText(image);

                if (text == null ||
                        text.isBlank()) {

                    continue;
                }

                if (!ocrBuilder.isEmpty()) {

                    ocrBuilder.append(
                            System.lineSeparator())
                            .append(System.lineSeparator());
                }

                ocrBuilder.append(
                        text.trim());

                successfulImages++;

            } catch (RuntimeException ex) {

                /*
                 * One bad image should not destroy
                * the entire document parsing operation.
                 */
                log.warn(
                        "OCR failed for image. documentId={}, imageIndex={}",
                        documentId,
                        index,
                        ex);
            }
        }

        String ocrText =
                ocrBuilder
                        .toString()
                        .trim();

        /*
         * OCR was attempted, but nothing readable
         * was produced.
         */
        if (ocrText.isBlank()) {

            log.info(
                    "OCR completed but produced no readable text. " +
                    "documentId={}, totalImages={}, processedImages={}",
                    documentId,
                    images.size(),
                    processedImages);

            context.getAttributes().put(
                    AnalysisAttributes.OCR_REQUIRED,
                    false);

            return;
        }

        /*
         * Append OCR output to existing machine-readable
         * text instead of replacing it.
         */
        appendOcrText(
                context,
                ocrText);

        /*
         * Store OCR-related analysis information.
         */
        context.getAttributes().put(
                AnalysisAttributes.OCR_REQUIRED,
                true);

        log.info(
                "OCR completed. documentId={}, " +
                "totalImages={}, " +
                "processedImages={}, " +
                "successfulImages={}, " +
                "ocrCharacters={}",
                documentId,
                images.size(),
                processedImages,
                successfulImages,
                ocrText.length());
    }

    /**
     * Prevent obviously invalid or tiny images
     * from reaching Tesseract.
     */
    private boolean isUsableImage(
            BufferedImage image) {

        if (image == null) {
            return false;
        }

        return image.getWidth() >= 10
                && image.getHeight() >= 10;
    }

    /**
     * Append OCR text without destroying
     * already extracted machine-readable text.
     */
    private void appendOcrText(
            AnalysisContext context,
            String ocrText) {

        String existing =
                context.getContent();

        if (existing == null ||
                existing.isBlank()) {

            context.setContent(
                    ocrText);

            return;
        }

        context.setContent(
                existing
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + ocrText);
    }
}