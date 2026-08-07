package com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "rag.ocr")
public class OcrProperties {

    /**
     * Enable / Disable OCR.
     */
    private boolean enabled = true;

    /**
     * Tesseract language.
     */
    private String language = "eng";

    /**
     * tessdata folder.
     */
    private String dataPath;

}