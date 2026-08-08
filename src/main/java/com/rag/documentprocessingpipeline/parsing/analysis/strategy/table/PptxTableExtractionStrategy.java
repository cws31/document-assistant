package com.rag.documentprocessingpipeline.parsing.analysis.strategy.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class PptxTableExtractionStrategy
        implements TableExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.PPTX;
    }

    @Override
    public List<TableData> extract(
            AnalysisContext context) {

        if (!(context.getSourceDocument() instanceof XMLSlideShow slideShow)) {

            throw new ParsingException(
                    "Invalid source document for PPTX table extraction.");
        }

        List<TableData> tables = new ArrayList<>();

        int tableIndex = 0;
        int slideIndex = 0;

        for (var slide : slideShow.getSlides()) {

            for (XSLFShape shape : slide.getShapes()) {

                if (!(shape instanceof XSLFTable table)) {
                    continue;
                }

                List<TableRow> rows = new ArrayList<>();

                List<XSLFTableRow> sourceRows = table.getRows();

                for (int rowIndex = 0; rowIndex < sourceRows.size(); rowIndex++) {

                    XSLFTableRow sourceRow = sourceRows.get(rowIndex);

                    List<TableCell> cells = new ArrayList<>();

                    List<XSLFTableCell> sourceCells = sourceRow.getCells();

                    for (int columnIndex = 0; columnIndex < sourceCells.size(); columnIndex++) {

                        XSLFTableCell sourceCell = sourceCells.get(columnIndex);

                        String content = normalize(
                                sourceCell.getText());

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
                                .tableIndex(tableIndex++)
                                .rows(rows)
                                .source(
                                        "PPTX slide "
                                                + (slideIndex + 1))
                                .build());
            }

            slideIndex++;
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