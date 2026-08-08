package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class XlsxTableExtractionStrategy
        implements TableExtractionStrategy {

    private final DataFormatter formatter = new DataFormatter();

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.XLSX;
    }

    @Override
    public List<TableData> extract(
            AnalysisContext context) {

        if (!(context.getSourceDocument() instanceof Workbook workbook)) {

            throw new ParsingException(
                    "Invalid source document for XLSX table extraction.");
        }

        List<TableData> tables = new ArrayList<>();

        int tableIndex = 0;

        for (Sheet sheet : workbook) {

            if (sheet.getPhysicalNumberOfRows() == 0) {
                continue;
            }

            List<TableRow> rows = new ArrayList<>();

            int firstRow = sheet.getFirstRowNum();

            int lastRow = sheet.getLastRowNum();

            for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {

                Row sourceRow = sheet.getRow(rowIndex);

                if (sourceRow == null) {
                    continue;
                }

                List<TableCell> cells = new ArrayList<>();

                int firstColumn = sourceRow.getFirstCellNum();

                int lastColumn = sourceRow.getLastCellNum();

                if (firstColumn < 0) {
                    continue;
                }

                for (int columnIndex = firstColumn; columnIndex < lastColumn; columnIndex++) {

                    Cell sourceCell = sourceRow.getCell(columnIndex);

                    String content = formatCell(sourceCell);

                    cells.add(
                            TableCell.builder()
                                    .columnIndex(columnIndex)
                                    .content(content)
                                    .header(rowIndex == firstRow)
                                    .build());
                }

                if (!cells.isEmpty()) {

                    rows.add(
                            TableRow.builder()
                                    .rowIndex(rowIndex)
                                    .cells(cells)
                                    .build());
                }
            }

            if (!rows.isEmpty()) {

                tables.add(
                        TableData.builder()
                                .tableIndex(tableIndex++)
                                .rows(rows)
                                .source(
                                        "XLSX sheet: "
                                                + sheet.getSheetName())
                                .build());
            }
        }

        return tables;
    }

    private String formatCell(
            Cell cell) {

        if (cell == null) {
            return "";
        }

        return formatter
                .formatCellValue(cell)
                .trim();
    }
}