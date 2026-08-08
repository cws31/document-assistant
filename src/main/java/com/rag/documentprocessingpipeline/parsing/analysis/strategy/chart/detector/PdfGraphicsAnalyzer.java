package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.detector;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

@Component
public class PdfGraphicsAnalyzer {

    public PdfChartCandidate analyze(
            PDPage page,
            int pageNumber) {

        GraphicsCollector collector = new GraphicsCollector(page);

        try {

            collector.processPage(page);

        } catch (IOException ex) {

            throw new ParsingException(
                    "Unable to analyze PDF graphics on page "
                            + pageNumber,
                    ex);
        }

        return collector.buildCandidate(pageNumber);
    }

    private static class GraphicsCollector
            extends PDFGraphicsStreamEngine {

        private final List<Rectangle2D> paths = new ArrayList<>();

        private final List<Rectangle2D> rectangles = new ArrayList<>();

        private final List<Rectangle2D> images = new ArrayList<>();

        private Rectangle2D currentPathBounds;

        private Point2D currentPoint;

        private int drawingCount;

        private int textCount;

        private GraphicsCollector(
                PDPage page) {

            super(page);
        }

        // =========================================================
        // PATH OPERATIONS
        // =========================================================

        @Override
        public void moveTo(
                float x,
                float y)
                throws IOException {

            currentPoint = new Point2D.Double(x, y);

            updateCurrentPathBounds(
                    x,
                    y);
        }

        @Override
        public void lineTo(
                float x,
                float y)
                throws IOException {

            currentPoint = new Point2D.Double(x, y);

            updateCurrentPathBounds(
                    x,
                    y);
        }

        @Override
        public void curveTo(
                float x1,
                float y1,
                float x2,
                float y2,
                float x3,
                float y3)
                throws IOException {

            updateCurrentPathBounds(
                    x1,
                    y1);

            updateCurrentPathBounds(
                    x2,
                    y2);

            updateCurrentPathBounds(
                    x3,
                    y3);

            currentPoint = new Point2D.Double(
                    x3,
                    y3);
        }

        @Override
        public Point2D getCurrentPoint()
                throws IOException {

            if (currentPoint == null) {
                return null;
            }

            return (Point2D) currentPoint.clone();
        }

        @Override
        public void closePath()
                throws IOException {

            currentPoint = null;
        }

        @Override
        public void endPath()
                throws IOException {

            currentPathBounds = null;

            currentPoint = null;
        }

        // =========================================================
        // PAINT OPERATIONS
        // =========================================================

        @Override
        public void strokePath()
                throws IOException {

            registerCurrentPath();

            drawingCount++;

            resetCurrentPath();
        }

        @Override
        public void fillPath(
                int windingRule)
                throws IOException {

            registerCurrentPath();

            drawingCount++;

            resetCurrentPath();
        }

        @Override
        public void fillAndStrokePath(
                int windingRule)
                throws IOException {

            registerCurrentPath();

            drawingCount++;

            resetCurrentPath();
        }

        // =========================================================
        // RECTANGLES
        // =========================================================

        @Override
        public void appendRectangle(
                Point2D p0,
                Point2D p1,
                Point2D p2,
                Point2D p3)
                throws IOException {

            Rectangle2D rectangle = calculateRectangle(
                    p0,
                    p1,
                    p2,
                    p3);

            rectangles.add(rectangle);

            updateCurrentPathBounds(
                    rectangle);

            drawingCount++;
        }

        // =========================================================
        // CLIPPING
        // =========================================================

        @Override
        public void clip(
                int windingRule)
                throws IOException {

            /*
             * Chart detection does not require
             * maintaining the actual clipping region.
             *
             * PDFBox requires this callback.
             */
        }

        // =========================================================
        // IMAGE OPERATIONS
        // =========================================================

        @Override
        public void drawImage(
                PDImage pdImage)
                throws IOException {

            if (!(pdImage instanceof PDImageXObject)) {
                return;
            }

            Matrix matrix = getGraphicsState()
                    .getCurrentTransformationMatrix();

            double x = matrix.getTranslateX();

            double y = matrix.getTranslateY();

            double width = Math.abs(matrix.getScaleX());

            double height = Math.abs(matrix.getScaleY());

            if (width <= 0 || height <= 0) {
                return;
            }

            Rectangle2D bounds = new Rectangle2D.Double(
                    x,
                    y,
                    width,
                    height);

            images.add(bounds);
        }

        // =========================================================
        // TEXT OPERATIONS
        // =========================================================

        /**
         * Called by PDFBox whenever a glyph is rendered.
         *
         * We count rendered glyphs as text elements.
         *
         * This gives the chart classifier information about
         * labels, axis values, legends, titles, etc.
         */
        @Override
        protected void showGlyph(
                Matrix textRenderingMatrix,
                PDFont font,
                int code,
                Vector displacement)
                throws IOException {

            textCount++;
        }

        // =========================================================
        // SHADING
        // =========================================================

        @Override
        public void shadingFill(
                org.apache.pdfbox.cos.COSName shadingName)
                throws IOException {

            /*
             * Shading may represent gradients or
             * graphical regions.
             */
            drawingCount++;
        }

        // =========================================================
        // INTERNAL PATH MANAGEMENT
        // =========================================================

        private void registerCurrentPath() {

            if (currentPathBounds == null) {
                return;
            }

            if (currentPathBounds.getWidth() <= 0
                    && currentPathBounds.getHeight() <= 0) {

                return;
            }

            paths.add(
                    (Rectangle2D) currentPathBounds.clone());
        }

        private void resetCurrentPath() {

            currentPathBounds = null;

            currentPoint = null;
        }

        private void updateCurrentPathBounds(
                double x,
                double y) {

            Rectangle2D pointBounds = new Rectangle2D.Double(
                    x,
                    y,
                    0,
                    0);

            updateCurrentPathBounds(
                    pointBounds);
        }

        private void updateCurrentPathBounds(
                Rectangle2D bounds) {

            if (bounds == null) {
                return;
            }

            if (currentPathBounds == null) {

                currentPathBounds = (Rectangle2D) bounds.clone();

                return;
            }

            Rectangle2D.union(
                    currentPathBounds,
                    bounds,
                    currentPathBounds);
        }

        // =========================================================
        // GEOMETRY
        // =========================================================

        private Rectangle2D calculateRectangle(
                Point2D p0,
                Point2D p1,
                Point2D p2,
                Point2D p3) {

            double minX = Math.min(
                    Math.min(
                            p0.getX(),
                            p1.getX()),
                    Math.min(
                            p2.getX(),
                            p3.getX()));

            double maxX = Math.max(
                    Math.max(
                            p0.getX(),
                            p1.getX()),
                    Math.max(
                            p2.getX(),
                            p3.getX()));

            double minY = Math.min(
                    Math.min(
                            p0.getY(),
                            p1.getY()),
                    Math.min(
                            p2.getY(),
                            p3.getY()));

            double maxY = Math.max(
                    Math.max(
                            p0.getY(),
                            p1.getY()),
                    Math.max(
                            p2.getY(),
                            p3.getY()));

            return new Rectangle2D.Double(
                    minX,
                    minY,
                    maxX - minX,
                    maxY - minY);
        }

        // =========================================================
        // CANDIDATE
        // =========================================================

        private PdfChartCandidate buildCandidate(
                int pageNumber) {

            Rectangle2D bounds = calculateOverallBounds();

            return PdfChartCandidate.builder()
                    .pageNumber(pageNumber)
                    .bounds(bounds)
                    .drawingCount(drawingCount)
                    .textCount(textCount)
                    .imageCount(images.size())
                    .repeatedRectangles(
                            rectangles.size() >= 3)
                    .repeatedLines(
                            paths.size() >= 5)
                    .build();
        }

        private Rectangle2D calculateOverallBounds() {

            Rectangle2D result = null;

            for (Rectangle2D rectangle : paths) {

                result = union(
                        result,
                        rectangle);
            }

            for (Rectangle2D rectangle : rectangles) {

                result = union(
                        result,
                        rectangle);
            }

            for (Rectangle2D rectangle : images) {

                result = union(
                        result,
                        rectangle);
            }

            return result;
        }

        private Rectangle2D union(
                Rectangle2D first,
                Rectangle2D second) {

            if (second == null) {
                return first;
            }

            if (first == null) {

                return (Rectangle2D) second.clone();
            }

            Rectangle2D result = (Rectangle2D) first.clone();

            Rectangle2D.union(
                    result,
                    second,
                    result);

            return result;
        }
    }
}