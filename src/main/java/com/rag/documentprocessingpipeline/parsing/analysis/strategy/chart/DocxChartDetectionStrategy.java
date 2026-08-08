package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DocxChartDetectionStrategy
        implements ChartDetectionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.DOCX;
    }

    @Override
    public List<ChartData> detect(
            AnalysisContext context) {

        Object source = context.getSourceDocument();

        if (!(source instanceof XWPFDocument document)) {

            throw new ParsingException(
                    "Invalid source document for DOCX chart detection.");
        }

        List<ChartData> charts = new ArrayList<>();

        int chartIndex = 0;

        for (XWPFChart chart : document.getCharts()) {

            if (chart == null) {
                continue;
            }

            charts.add(
                    ChartData.builder()
                            .chartIndex(chartIndex++)
                            .title(null)
                            .chartType("OFFICE_CHART")
                            .source("DOCX embedded chart")
                            .build());
        }

        log.debug(
                "DOCX chart detection completed. " +
                        "documentId={}, chartCount={}",
                context.getDocument().getId(),
                charts.size());

        return charts;
    }
}