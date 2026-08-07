package com.rag.documentprocessingpipeline.parsing.analysis.strategy.text;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

@Component
public class PptxTextExtractionStrategy
        implements TextExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof XMLSlideShow;

    }

    @Override
    public String extract(
            AnalysisContext context) {

        XMLSlideShow slideShow = (XMLSlideShow) context.getSourceDocument();

        StringBuilder builder = new StringBuilder();

        int slideNumber = 1;

        for (XSLFSlide slide : slideShow.getSlides()) {

            builder.append("Slide ")
                    .append(slideNumber++)
                    .append(System.lineSeparator());

            for (XSLFShape shape : slide.getShapes()) {

                if (shape instanceof XSLFTextShape textShape) {

                    String text = textShape.getText();

                    if (text != null && !text.isBlank()) {

                        builder.append(text)
                                .append(System.lineSeparator());

                    }

                }

            }

            builder.append(System.lineSeparator());

        }

        return builder.toString();

    }

}