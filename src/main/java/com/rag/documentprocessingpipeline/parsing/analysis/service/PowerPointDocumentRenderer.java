package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class PowerPointDocumentRenderer
        implements DocumentRenderer {

    @Override
    public FileType supportedType() {
        return FileType.PPTX;
    }

    @Override
    public List<BufferedImage> render(
            Object sourceDocument) {

        if (!(sourceDocument instanceof XMLSlideShow slideShow)) {

            throw new ParsingException(
                    "Invalid source document for PPTX renderer.");
        }

        /*
         * PPTX visual rendering will be implemented with a proper
         * slide rendering strategy.
         *
         * Apache POI provides slide structure but does not provide
         * a complete PowerPoint rendering engine equivalent to
         * PDFBox's PDFRenderer.
         */
        throw new ParsingException(
                "PPTX visual rendering is not implemented yet.");
    }
}