package com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFChart;
import org.apache.poi.xslf.usermodel.XSLFGraphicFrame;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PptxChartDetectionStrategy
        implements ChartDetectionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.PPTX;
    }

    @Override
    public List<ChartData> detect(
            AnalysisContext context) {

        Object source = context.getSourceDocument();

        if (!(source instanceof XMLSlideShow slideShow)) {

            throw new ParsingException(
                    "Invalid source document for PPTX chart detection.");
        }

        List<ChartData> charts = new ArrayList<>();

        int chartIndex = 0;

        List<XSLFSlide> slides = slideShow.getSlides();

        for (int slideIndex = 0; slideIndex < slides.size(); slideIndex++) {

            XSLFSlide slide = slides.get(slideIndex);

            for (var shape : slide.getShapes()) {

                if (!(shape instanceof XSLFGraphicFrame graphicFrame)) {
                    continue;
                }

                if (graphicFrame.getChart() == null) {
                    continue;
                }

                charts.add(
                        ChartData.builder()
                                .chartIndex(chartIndex++)
                                .title(null)
                                .chartType("OFFICE_CHART")
                                .source(
                                        "PPTX slide "
                                                + (slideIndex + 1))
                                .build());
            }
        }

        log.debug(
                "PPTX chart detection completed. " +
                        "documentId={}, chartCount={}",
                context.getDocument().getId(),
                charts.size());

        return charts;
    }
}