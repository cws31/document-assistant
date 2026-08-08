package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TableRow {

    /**
     * Zero-based row index.
     */
    private final int rowIndex;

    /**
     * Cells belonging to this row.
     */
    @Builder.Default
    private final List<TableCell> cells = new ArrayList<>();
}