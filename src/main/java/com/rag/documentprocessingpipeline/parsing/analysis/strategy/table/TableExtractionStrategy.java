package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.List;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

public interface TableExtractionStrategy {

    boolean supports(
            AnalysisContext context);

    List<TableData> extract(
            AnalysisContext context);
}