package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import java.util.Collections;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.constants.AnalysisAttributes;
import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.table.TableData;
import com.rag.documentprocessingpipeline.parsing.analysis.strategy.table.TableExtractionStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(400)
@RequiredArgsConstructor
public class TableAnalyzer
        extends AbstractDocumentAnalyzer {

    private final List<TableExtractionStrategy> strategies;

    @Override
    protected void doAnalyze(
            AnalysisContext context) {

        TableExtractionStrategy strategy = strategies.stream()
                .filter(candidate -> candidate.supports(context))
                .findFirst()
                .orElse(null);

        if (strategy == null) {

            context.getAttributes().put(
                    AnalysisAttributes.TABLE_FOUND,
                    false);

            context.getAttributes().put(
                    AnalysisAttributes.TABLE_COUNT,
                    0);

            context.setTables(
                    Collections.emptyList());

            log.debug(
                    "No table extraction strategy registered. " +
                            "documentId={}, fileType={}",
                    context.getDocument().getId(),
                    context.getFileType());

            return;
        }

        List<TableData> tables = strategy.extract(context);

        if (tables == null) {
            tables = Collections.emptyList();
        }

        context.setTables(tables);

        boolean tableFound = !tables.isEmpty();

        context.getAttributes().put(
                AnalysisAttributes.TABLE_FOUND,
                tableFound);

        context.getAttributes().put(
                AnalysisAttributes.TABLE_COUNT,
                tables.size());

        log.info(
                "Table analysis completed. " +
                        "documentId={}, fileType={}, " +
                        "tableFound={}, tableCount={}",
                context.getDocument().getId(),
                context.getFileType(),
                tableFound,
                tables.size());
    }
}