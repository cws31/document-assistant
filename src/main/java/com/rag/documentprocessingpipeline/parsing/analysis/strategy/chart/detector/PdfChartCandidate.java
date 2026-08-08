package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.detector;

import java.awt.geom.Rectangle2D;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PdfChartCandidate {

    /**
     * PDF page number.
     * One-based for human-readable source information.
     */
    private final int pageNumber;

    /**
     * Bounding box of the candidate region.
     */
    private final Rectangle2D bounds;

    /**
     * Number of vector drawing operations detected
     * inside the region.
     */
    private final int drawingCount;

    /**
     * Number of text elements detected around/inside
     * the candidate.
     */
    private final int textCount;

    /**
     * Number of image objects contained in the region.
     */
    private final int imageCount;

    /**
     * Whether the candidate contains repeated
     * rectangular/bar-like structures.
     */
    private final boolean repeatedRectangles;

    /**
     * Whether the candidate contains line-like structures.
     */
    private final boolean repeatedLines;
}