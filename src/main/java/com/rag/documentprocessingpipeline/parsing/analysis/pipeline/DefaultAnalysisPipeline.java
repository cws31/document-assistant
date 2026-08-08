package com.rag.documentprocessingpipeline.parsing.analysis.pipeline;

import java.util.List;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.analyzer.DocumentAnalyzer;
import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.model.AnalysisResult;

@Component
public class DefaultAnalysisPipeline implements AnalysisPipeline {

    private final List<DocumentAnalyzer> analyzers;

    public DefaultAnalysisPipeline(
            List<DocumentAnalyzer> analyzers) {

        AnnotationAwareOrderComparator.sort(analyzers);

        this.analyzers = List.copyOf(analyzers);

    }

    @Override
    public AnalysisResult analyze(
            AnalysisContext context) {

        for (DocumentAnalyzer analyzer : analyzers) {

            analyzer.analyze(context);

        }

        return AnalysisResult.builder()
                .content(context.getContent())
                .metadata(context.getMetadata())
                .images(context.getImages())
                .tables(context.getTables())
                .charts(context.getCharts())
                .attributes(context.getAttributes())
                .build();

    }

}