package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.extern.slf4j.Slf4j;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

@Slf4j
@Component
public class PdfTableExtractionStrategy
        implements TableExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context != null
                && context.getFileType() == FileType.PDF;
    }

    @Override
    public List<TableData> extract(
            AnalysisContext context) {

        if (context == null) {
            return Collections.emptyList();
        }

        Object sourceDocument = context.getSourceDocument();

        if (!(sourceDocument instanceof PDDocument document)) {

            throw new ParsingException(
                    "Invalid source document for PDF table extraction.");
        }

        List<TableData> extractedTables = new ArrayList<>();

        int tableIndex = 0;

        try (ObjectExtractor extractor = new ObjectExtractor(document)) {

            SpreadsheetExtractionAlgorithm spreadsheetAlgorithm = new SpreadsheetExtractionAlgorithm();

            BasicExtractionAlgorithm basicAlgorithm = new BasicExtractionAlgorithm();

            int pageCount = document.getNumberOfPages();

            for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {

                try {

                    Page page = extractor.extract(pageNumber);

                    List<Table> tables = extractSpreadsheetTables(
                            page,
                            spreadsheetAlgorithm);

                    /*
                     * If spreadsheet extraction did not find
                     * anything, try text-position based extraction.
                     */
                    if (tables.isEmpty()) {

                        tables = extractBasicTables(
                                page,
                                basicAlgorithm);
                    }

                    for (Table table : tables) {

                        TableData tableData = convertTable(
                                table,
                                tableIndex,
                                pageNumber);

                        if (tableData != null) {

                            extractedTables.add(
                                    tableData);

                            tableIndex++;
                        }
                    }

                } catch (Exception ex) {

                    /*
                     * A malformed page must not terminate
                     * extraction for the complete PDF.
                     */
                    log.warn(
                            "Unable to extract tables from PDF page. " +
                                    "documentId={}, page={}",
                            context.getDocument().getId(),
                            pageNumber,
                            ex);
                }
            }

        } catch (Exception ex) {

            throw new ParsingException(
                    "Unable to extract tables from PDF.",
                    ex);
        }

        log.info(
                "PDF table extraction completed. " +
                        "documentId={}, pages={}, tables={}",
                context.getDocument().getId(),
                document.getNumberOfPages(),
                extractedTables.size());

        return extractedTables;
    }

    private List<Table> extractSpreadsheetTables(
            Page page,
            SpreadsheetExtractionAlgorithm algorithm) {

        try {

            return algorithm.extract(page);

        } catch (Exception ex) {

            log.debug(
                    "Spreadsheet table extraction failed for page.",
                    ex);

            return Collections.emptyList();
        }
    }

    private List<Table> extractBasicTables(
            Page page,
            BasicExtractionAlgorithm algorithm) {

        try {

            return algorithm.extract(page);

        } catch (Exception ex) {

            log.debug(
                    "Basic table extraction failed for page.",
                    ex);

            return Collections.emptyList();
        }
    }

    private TableData convertTable(
            Table table,
            int tableIndex,
            int pageNumber) {

        if (table == null
                || table.getRows() == null
                || table.getRows().isEmpty()) {

            return null;
        }

        List<TableRow> rows = new ArrayList<>();

        int rowIndex = 0;

        for (List<RectangularTextContainer> sourceRow : table.getRows()) {

            if (sourceRow == null
                    || sourceRow.isEmpty()) {

                continue;
            }

            List<TableCell> cells = new ArrayList<>();

            int columnIndex = 0;

            for (RectangularTextContainer cell : sourceRow) {

                String content = extractCellText(cell);

                boolean header = rowIndex == 0;

                cells.add(
                        TableCell.builder()
                                .columnIndex(columnIndex)
                                .content(content)
                                .header(header)
                                .build());

                columnIndex++;
            }

            if (!cells.isEmpty()) {

                rows.add(
                        TableRow.builder()
                                .rowIndex(rowIndex)
                                .cells(cells)
                                .build());

                rowIndex++;
            }
        }

        if (rows.isEmpty()) {
            return null;
        }

        return TableData.builder()
                .tableIndex(tableIndex)
                .rows(rows)
                .source(
                        "PDF page " + pageNumber)
                .build();
    }

    private String extractCellText(
            RectangularTextContainer cell) {

        if (cell == null) {
            return "";
        }

        String text = cell.getText();

        if (text == null) {
            return "";
        }

        return text
                .replace('\r', ' ')
                .replace('\n', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }
}