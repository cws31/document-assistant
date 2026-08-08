package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class XlsxChartDetectionStrategy
        implements ChartDetectionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.XLSX;
    }

    @Override
    public List<ChartData> detect(
            AnalysisContext context) {

        Object source = context.getSourceDocument();

        if (!(source instanceof XSSFWorkbook workbook)) {

            throw new ParsingException(
                    "Invalid source document for XLSX chart detection.");
        }

        List<ChartData> charts = new ArrayList<>();

        int chartIndex = 0;

        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {

            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            if (drawing == null) {
                continue;
            }

            for (XSSFChart chart : drawing.getCharts()) {

                if (chart == null) {
                    continue;
                }

                charts.add(
                        ChartData.builder()
                                .chartIndex(chartIndex++)
                                .title(null)
                                .chartType("OFFICE_CHART")
                                .source(
                                        "XLSX sheet "
                                                + sheet.getSheetName())
                                .build());

                log.debug(
                        "XLSX chart detected. " +
                                "documentId={}, sheet={}, chartIndex={}",
                        context.getDocument().getId(),
                        sheet.getSheetName(),
                        chartIndex - 1);
            }
        }

        return charts;
    }
}