package com.rag.documentprocessingpipeline.parsing.model;

import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParsedMetadata {

    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

}