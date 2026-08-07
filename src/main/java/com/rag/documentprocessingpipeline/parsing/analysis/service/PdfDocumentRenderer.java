package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class PdfDocumentRenderer
        implements DocumentRenderer {

    private static final float DPI = 300f;

    @Override
    public FileType supportedType() {
        return FileType.PDF;
    }

    @Override
    public List<BufferedImage> render(
            Object sourceDocument) {

        PDDocument document = (PDDocument) sourceDocument;

        try {

            PDFRenderer renderer = new PDFRenderer(document);

            List<BufferedImage> pages = new ArrayList<>();

            for (int page = 0; page < document.getNumberOfPages(); page++) {

                pages.add(
                        renderer.renderImageWithDPI(
                                page,
                                DPI,
                                ImageType.RGB));

            }

            return pages;

        } catch (IOException ex) {

            throw new ParsingException(
                    "Unable to render PDF.",
                    ex);

        }

    }

}