package com.rag.documentprocessingpipeline.parsing.analysis.strategy.text;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

@Component
public class XlsxTextExtractionStrategy
        implements TextExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof XWPFDocument;

    }

    @Override
    public String extract(
            AnalysisContext context) {

        XWPFDocument document = (XWPFDocument) context.getSourceDocument();

        StringBuilder builder = new StringBuilder();

        document.getParagraphs()
                .forEach(paragraph -> {

                    String text = paragraph.getText();

                    if (text != null && !text.isBlank()) {

                        builder.append(text)
                                .append(System.lineSeparator());

                    }

                });

        return builder.toString();

    }

}