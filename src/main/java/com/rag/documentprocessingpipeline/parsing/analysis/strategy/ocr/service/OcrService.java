package com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.service;

import java.awt.image.BufferedImage;

public interface OcrService {

    String extractText(
            BufferedImage image);

}