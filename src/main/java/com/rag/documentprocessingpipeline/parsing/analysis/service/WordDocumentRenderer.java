package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class WordDocumentRenderer
        implements DocumentRenderer {

    @Override
    public FileType supportedType() {
        return FileType.DOCX;
    }

    @Override
    public List<BufferedImage> render(
            Object sourceDocument) {

        if (!(sourceDocument instanceof XWPFDocument document)) {

            throw new ParsingException(
                    "Invalid source document for DOCX renderer.");
        }

        /*
         * DOCX rendering is intentionally not implemented here yet.
         *
         * Apache POI can read DOCX structure, but it does not provide
         * a native page renderer comparable to PDFBox's PDFRenderer.
         *
         * Therefore we must not pretend that this renderer can currently
         * produce accurate page images.
         */
        throw new ParsingException(
                "DOCX visual rendering is not implemented yet.");
    }
}