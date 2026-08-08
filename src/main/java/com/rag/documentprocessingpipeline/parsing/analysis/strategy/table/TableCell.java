package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TableCell {

    /**
     * Zero-based column index.
     */
    private final int columnIndex;

    /**
     * Cell textual content.
     */
    private final String content;

    /**
     * Whether this cell is considered a header.
     */
    private final boolean header;
}