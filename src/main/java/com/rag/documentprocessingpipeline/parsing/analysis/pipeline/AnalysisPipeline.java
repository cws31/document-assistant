package com.rag.documentprocessingpipeline.parsing.analysis.pipeline;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.model.AnalysisResult;

public interface AnalysisPipeline {

    AnalysisResult analyze(
            AnalysisContext context);

}