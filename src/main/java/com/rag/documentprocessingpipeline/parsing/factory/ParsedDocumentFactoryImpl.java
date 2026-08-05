package com.rag.documentprocessingpipeline.parsing.factory;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;
import com.rag.upload.entity.Document;

@Component
public class ParsedDocumentFactoryImpl
        implements ParsedDocumentFactory {

    @Override
    public ParsedDocument create(
            Document document,
            String content,
            ParsedMetadata metadata) {

        return ParsedDocument.builder()
                .documentId(document.getId())
                .content(content)
                .metadata(metadata)
                .build();

    }

}