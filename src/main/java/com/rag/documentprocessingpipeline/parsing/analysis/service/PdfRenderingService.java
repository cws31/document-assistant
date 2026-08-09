package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PdfRenderingService {

    private static final float DPI = 300f;

    public List<BufferedImage> render(
            PDDocument document) {

        if (document == null) {

            throw new IllegalArgumentException(
                    "PDF document cannot be null.");
        }

        try {

            PDFRenderer renderer = new PDFRenderer(document);

            List<BufferedImage> images = new ArrayList<>();

            for (int page = 0; page < document.getNumberOfPages(); page++) {

                images.add(
                        renderer.renderImageWithDPI(
                                page,
                                DPI,
                                ImageType.RGB));
            }

            return images;

        } catch (IOException ex) {

            throw new ParsingException(
                    "Unable to render PDF pages.",
                    ex);
        }
    }
}