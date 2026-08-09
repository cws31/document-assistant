package com.rag.documentprocessingpipeline.parsing.analysis.strategy.text;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

@Component
public class XlsxTextExtractionStrategy
        implements TextExtractionStrategy {

    private final DataFormatter formatter = new DataFormatter();

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context != null
                && context.getSourceDocument() instanceof Workbook;
    }

    @Override
    public String extract(
            AnalysisContext context) {

        Workbook workbook = (Workbook) context.getSourceDocument();

        StringBuilder builder = new StringBuilder();

        for (Sheet sheet : workbook) {

            builder.append("Sheet: ")
                    .append(sheet.getSheetName())
                    .append(System.lineSeparator());

            for (Row row : sheet) {

                boolean hasContent = false;

                for (Cell cell : row) {

                    String value = formatter.formatCellValue(cell);

                    if (value != null
                            && !value.isBlank()) {

                        if (hasContent) {
                            builder.append(" ");
                        }

                        builder.append(value.trim());

                        hasContent = true;
                    }
                }

                if (hasContent) {
                    builder.append(
                            System.lineSeparator());
                }
            }

            builder.append(
                    System.lineSeparator());
        }

        return builder
                .toString()
                .trim();
    }
}