package com.rag.documentprocessingpipeline.parsing.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParsedDocument {

    private final Long documentId;

    private final String content;

    private final ParsedMetadata metadata;

}