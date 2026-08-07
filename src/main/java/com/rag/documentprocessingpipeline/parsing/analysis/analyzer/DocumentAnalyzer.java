package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

public interface DocumentAnalyzer {

    void analyze(
            AnalysisContext context);

}