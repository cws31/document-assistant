package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.detector;

import org.springframework.stereotype.Component;

@Component
public class PdfChartClassifier {

    private static final int MIN_DRAWINGS = 5;

    private static final int MIN_TEXT_ELEMENTS = 2;

    public boolean isChartCandidate(
            PdfChartCandidate candidate) {

        if (candidate == null) {
            return false;
        }

        if (candidate.getBounds() == null) {
            return false;
        }

        double width = candidate.getBounds().getWidth();

        double height = candidate.getBounds().getHeight();

        /*
         * Ignore extremely small graphical regions.
         */
        if (width < 80 || height < 50) {
            return false;
        }

        /*
         * A chart normally contains some combination
         * of graphical structures and labels.
         */
        boolean enoughDrawing = candidate.getDrawingCount() >= MIN_DRAWINGS;

        boolean hasText = candidate.getTextCount() >= MIN_TEXT_ELEMENTS;

        /*
         * Strong bar-chart-like signal.
         */
        if (candidate.isRepeatedRectangles()
                && enoughDrawing
                && hasText) {

            return true;
        }

        /*
         * Strong line-chart-like signal.
         */
        if (candidate.isRepeatedLines()
                && enoughDrawing
                && hasText) {

            return true;
        }

        return false;
    }
}