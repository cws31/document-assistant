package com.rag.documentprocessingpipeline.parsing.analysis.model;

import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResult {

    private final String content;

    private final ParsedMetadata metadata;

}