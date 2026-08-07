package com.rag.documentprocessingpipeline.parsing.analysis.strategy.text;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

public interface TextExtractionStrategy {

    boolean supports(
            AnalysisContext context);

    String extract(
            AnalysisContext context);

}