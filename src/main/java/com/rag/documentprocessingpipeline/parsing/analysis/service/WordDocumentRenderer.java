package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WordDocumentRenderer
        implements DocumentRenderer {

    private final LibreOfficeConversionService conversionService;

    private final PdfRenderingService pdfRenderingService;

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

        Path temporaryDirectory = null;

        try {

            temporaryDirectory = Files.createTempDirectory(
                    "rag-docx-render-");

            Path sourceFile = temporaryDirectory.resolve(
                    "document.docx");

            try (var output = Files.newOutputStream(sourceFile)) {

                document.write(output);
            }

            Path pdf = conversionService.convertToPdf(
                    sourceFile,
                    temporaryDirectory);

            try (var pdfDocument = org.apache.pdfbox.Loader.loadPDF(
                    pdf.toFile())) {

                return pdfRenderingService.render(
                        pdfDocument);
            }

        } catch (IOException ex) {

            throw new ParsingException(
                    "Unable to render DOCX document.",
                    ex);

        } finally {

            deleteTemporaryDirectory(
                    temporaryDirectory);
        }
    }

    private void deleteTemporaryDirectory(
            Path directory) {

        if (directory == null) {
            return;
        }

        try {

            if (Files.exists(directory)) {

                Files.walk(directory)
                        .sorted(
                                java.util.Comparator
                                        .reverseOrder())
                        .forEach(path -> {

                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException ignored) {
                                // Cleanup failure must not
                                // hide the original result.
                            }
                        });
            }

        } catch (IOException ignored) {
            // Cleanup failure intentionally ignored.
        }
    }
}