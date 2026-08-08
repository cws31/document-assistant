package com.rag.documentprocessingpipeline.parsing.analysis.strategy.image;

import java.awt.image.BufferedImage;
import java.util.List;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

public interface ImageExtractionStrategy {

    boolean supports(
            AnalysisContext context);

    List<BufferedImage> extract(
            AnalysisContext context);

}