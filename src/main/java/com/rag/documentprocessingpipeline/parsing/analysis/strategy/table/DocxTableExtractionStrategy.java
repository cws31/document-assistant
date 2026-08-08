package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class DocxTableExtractionStrategy
        implements TableExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.DOCX;
    }

    @Override
    public List<TableData> extract(
            AnalysisContext context) {

        if (!(context.getSourceDocument() instanceof XWPFDocument document)) {

            throw new ParsingException(
                    "Invalid source document for DOCX table extraction.");
        }

        List<TableData> tables = new ArrayList<>();

        List<XWPFTable> sourceTables = document.getTables();

        for (int tableIndex = 0; tableIndex < sourceTables.size(); tableIndex++) {

            XWPFTable sourceTable = sourceTables.get(tableIndex);

            List<TableRow> rows = new ArrayList<>();

            List<XWPFTableRow> sourceRows = sourceTable.getRows();

            for (int rowIndex = 0; rowIndex < sourceRows.size(); rowIndex++) {

                XWPFTableRow sourceRow = sourceRows.get(rowIndex);

                List<TableCell> cells = new ArrayList<>();

                List<XWPFTableCell> sourceCells = sourceRow.getTableCells();

                for (int columnIndex = 0; columnIndex < sourceCells.size(); columnIndex++) {

                    XWPFTableCell sourceCell = sourceCells.get(columnIndex);

                    String content = normalize(sourceCell.getText());

                    cells.add(
                            TableCell.builder()
                                    .columnIndex(columnIndex)
                                    .content(content)
                                    .header(rowIndex == 0)
                                    .build());
                }

                rows.add(
                        TableRow.builder()
                                .rowIndex(rowIndex)
                                .cells(cells)
                                .build());
            }

            tables.add(
                    TableData.builder()
                            .tableIndex(tableIndex)
                            .rows(rows)
                            .source("DOCX table " + (tableIndex + 1))
                            .build());
        }

        return tables;
    }

    private String normalize(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace('\r', ' ')
                .replace('\n', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }
}