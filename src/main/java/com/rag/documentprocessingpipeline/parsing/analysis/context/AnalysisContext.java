package com.rag.documentprocessingpipeline.parsing.analysis.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.ChartData;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.table.TableData;
import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;
import com.rag.upload.entity.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnalysisContext {

    /**
     * Original uploaded document entity.
     */
    private final Document document;

    /**
     * Document type.
     */
    private final FileType fileType;

    /**
     * Native parser object.
     *
     * Examples:
     * PDDocument
     * XWPFDocument
     * XMLSlideShow
     * Workbook
     */
    private final Object sourceDocument;

    /**
     * Shared attributes between analyzers.
     */
    @Builder.Default
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * Extracted textual content.
     */
    private String content;

    /**
     * Extracted metadata.
     */
    private ParsedMetadata metadata;

    /**
     * Extracted/rendered images.
     */
    @Builder.Default
    private List<java.awt.image.BufferedImage> images = new ArrayList<>();

    /**
     * Extracted structured tables.
     */
    @Builder.Default
    private List<TableData> tables = new ArrayList<>();

    /**
     * Detected charts.
     */
    @Builder.Default
    private List<ChartData> charts = new ArrayList<>();
}