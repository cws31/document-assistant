package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class ExcelDocumentRenderer
        implements DocumentRenderer {

    @Override
    public FileType supportedType() {
        return FileType.XLSX;
    }

    @Override
    public List<BufferedImage> render(
            Object sourceDocument) {

        if (!(sourceDocument instanceof Workbook workbook)) {

            throw new ParsingException(
                    "Invalid source document for XLSX renderer.");
        }

        /*
         * XLSX visual rendering is intentionally not implemented yet.
         *
         * Excel requires spreadsheet-aware rendering because
         * worksheets can contain:
         * - cells
         * - tables
         * - charts
         * - drawings
         * - images
         * - merged cells
         * - formulas
         * - formatting
         *
         * We will implement this separately rather than generating
         * an inaccurate screenshot-like representation.
         */
        throw new ParsingException(
                "XLSX visual rendering is not implemented yet.");
    }
}