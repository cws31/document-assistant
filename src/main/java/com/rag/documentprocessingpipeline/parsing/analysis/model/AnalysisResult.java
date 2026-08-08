package com.rag.documentprocessingpipeline.parsing.analysis.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.ChartData;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.table.TableData;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResult {

    private final String content;

    private final ParsedMetadata metadata;

    @Builder.Default
    private final List<BufferedImage> images = new ArrayList<>();

    @Builder.Default
    private final List<TableData> tables = new ArrayList<>();

    @Builder.Default
    private final List<ChartData> charts = new ArrayList<>();

    @Builder.Default
    private final Map<String, Object> attributes = new HashMap<>();
}