package com.rag.documentprocessingpipeline.parsing.analysis.strategy.matadata;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;

public interface MetadataExtractionStrategy {

    boolean supports(
            AnalysisContext context);

    ParsedMetadata extract(
            AnalysisContext context);

}