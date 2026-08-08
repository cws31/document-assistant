package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.util.Collections;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.constants.AnalysisAttributes;
import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.ChartData;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.chart.ChartDetectionStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(500)
@RequiredArgsConstructor
public class ChartAnalyzer
        extends AbstractDocumentAnalyzer {

    private final List<ChartDetectionStrategy> strategies;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        ChartDetectionStrategy strategy = strategies.stream()
                .filter(s -> s.supports(context))
                .findFirst()
                .orElse(null);

        if (strategy == null) {

            context.getAttributes().put(
                    AnalysisAttributes.CHART_FOUND,
                    false);

            context.getAttributes().put(
                    AnalysisAttributes.CHART_COUNT,
                    0);

            context.setCharts(
                    Collections.emptyList());

            log.debug(
                    "No chart detection strategy registered. " +
                            "documentId={}, fileType={}",
                    context.getDocument().getId(),
                    context.getFileType());

            return;
        }

        List<ChartData> charts = strategy.detect(context);

        if (charts == null) {
            charts = Collections.emptyList();
        }

        context.setCharts(charts);

        boolean chartFound = !charts.isEmpty();

        context.getAttributes().put(
                AnalysisAttributes.CHART_FOUND,
                chartFound);

        context.getAttributes().put(
                AnalysisAttributes.CHART_COUNT,
                charts.size());

        log.info(
                "Chart analysis completed. " +
                        "documentId={}, fileType={}, " +
                        "chartFound={}, chartCount={}",
                context.getDocument().getId(),
                context.getFileType(),
                chartFound,
                charts.size());
    }
}