package com.rag.documentprocessingpipeline.parsing.model;

import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ParsedMetadata {

    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

}