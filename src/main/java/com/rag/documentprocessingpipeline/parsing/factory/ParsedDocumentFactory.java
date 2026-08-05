package com.rag.documentprocessingpipeline.parsing.factory;

import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;
import com.rag.upload.entity.Document;

public interface ParsedDocumentFactory {

    ParsedDocument create(
            Document document,
            String content,
            ParsedMetadata metadata);

}