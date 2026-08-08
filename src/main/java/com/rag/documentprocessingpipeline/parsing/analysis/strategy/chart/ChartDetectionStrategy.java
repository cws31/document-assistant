package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart;

import java.util.List;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

public interface ChartDetectionStrategy {

    boolean supports(
            AnalysisContext context);

    List<ChartData> detect(
            AnalysisContext context);
}