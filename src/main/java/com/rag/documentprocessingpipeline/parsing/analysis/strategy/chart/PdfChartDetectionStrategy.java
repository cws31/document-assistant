package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.detector.PdfChartCandidate;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.detector.PdfChartClassifier;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.detector.PdfGraphicsAnalyzer;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PdfChartDetectionStrategy
        implements ChartDetectionStrategy {

    private final PdfGraphicsAnalyzer graphicsAnalyzer;

    private final PdfChartClassifier classifier;

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.PDF;
    }

    @Override
    public List<ChartData> detect(
            AnalysisContext context) {

        Object source = context.getSourceDocument();

        if (!(source instanceof PDDocument document)) {

            throw new ParsingException(
                    "Invalid source document for PDF chart detection.");
        }

        List<ChartData> charts = new ArrayList<>();

        int chartIndex = 0;

        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {

            int pageNumber = pageIndex + 1;

            PdfChartCandidate candidate = graphicsAnalyzer.analyze(
                    document.getPage(pageIndex),
                    pageNumber);

            if (!classifier.isChartCandidate(
                    candidate)) {

                continue;
            }

            charts.add(
                    ChartData.builder()
                            .chartIndex(chartIndex++)
                            .title(null)
                            .chartType("UNKNOWN")
                            .source(
                                    "PDF page "
                                            + pageNumber)
                            .build());

            log.debug(
                    "PDF chart candidate detected. " +
                            "documentId={}, page={}, " +
                            "drawingCount={}, imageCount={}",
                    context.getDocument().getId(),
                    pageNumber,
                    candidate.getDrawingCount(),
                    candidate.getImageCount());
        }

        return charts;
    }
}