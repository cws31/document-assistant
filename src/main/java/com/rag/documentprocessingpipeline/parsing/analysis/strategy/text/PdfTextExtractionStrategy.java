package com.rag.documentprocessingpipeline.parsing.analysis.strategy.text;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

@Component
public class PdfTextExtractionStrategy
        implements TextExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof PDDocument;

    }

    @Override
    public String extract(
            AnalysisContext context) {

        PDDocument pdfDocument = (PDDocument) context.getSourceDocument();

        try {

            PDFTextStripper stripper = new PDFTextStripper();

            String extractedText = stripper.getText(pdfDocument);

            return extractedText == null
                    ? ""
                    : extractedText.trim();

        } catch (IOException ex) {

            throw new ParsingException(
                    "Unable to extract text from PDF.",
                    ex);

        }

    }

}