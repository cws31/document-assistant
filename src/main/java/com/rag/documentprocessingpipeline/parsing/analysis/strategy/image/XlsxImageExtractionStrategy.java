package com.rag.documentprocessingpipeline.parsing.analysis.strategy.image;

import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.analysis.service.DocumentRenderer;
import com.rag.documentprocessingpipeline.parsing.analysis.service.DocumentRendererFactory;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class XlsxImageExtractionStrategy
        implements ImageExtractionStrategy {

    private final DocumentRendererFactory rendererFactory;

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.XLSX;
    }

    @Override
    public List<BufferedImage> extract(
            AnalysisContext context) {

        DocumentRenderer renderer = rendererFactory.getRenderer(FileType.XLSX);

        return renderer.render(
                context.getSourceDocument());
    }
}