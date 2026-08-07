package com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.service;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.properties.OcrProperties;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TesseractOcrService
        implements OcrService {

    private final OcrProperties properties;

    @Override
    public String extractText(
            BufferedImage image) {

        if (!properties.isEnabled()) {

            log.debug("OCR is disabled.");

            return "";

        }

        try {

            ITesseract tesseract = createTesseract();

            String text = tesseract.doOCR(image);

            return normalize(text);

        } catch (TesseractException ex) {

            throw new ParsingException(
                    "Unable to perform OCR.",
                    ex);

        }

    }

    private ITesseract createTesseract() {

        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath(
                properties.getDataPath());

        tesseract.setLanguage(
                properties.getLanguage());

        return tesseract;

    }

    private String normalize(
            String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\r", "")
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();

    }

}