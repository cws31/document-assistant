package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChartData {

    private final int chartIndex;

    private final String title;

    private final String chartType;

    private final String source;
}