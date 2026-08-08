package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TableData {

    /**
     * Zero-based table index within the document.
     */
    private final int tableIndex;

    /**
     * Extracted rows.
     */
    @Builder.Default
    private final List<TableRow> rows = new ArrayList<>();

    /**
     * Optional source information.
     *
     * Examples:
     * DOCX table
     * XLSX sheet name
     * PPTX slide number
     * PDF page number
     */
    private final String source;
}