package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.awt.image.BufferedImage;
import java.util.List;

import com.rag.documentprocessingpipeline.parsing.model.FileType;

public interface DocumentRenderer {

    FileType supportedType();

    List<BufferedImage> render(
            Object sourceDocument);

}