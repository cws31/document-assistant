package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.model.FileType;

@Component
public class DocumentRendererFactory {

    private final Map<FileType, DocumentRenderer> renderers = new EnumMap<>(FileType.class);

    public DocumentRendererFactory(
            List<DocumentRenderer> renderers) {

        for (DocumentRenderer renderer : renderers) {

            this.renderers.put(
                    renderer.supportedType(),
                    renderer);

        }

    }

    public DocumentRenderer getRenderer(
            FileType fileType) {

        DocumentRenderer renderer = renderers.get(fileType);

        if (renderer == null) {

            throw new IllegalArgumentException(
                    "No renderer registered for " + fileType);

        }

        return renderer;

    }

}