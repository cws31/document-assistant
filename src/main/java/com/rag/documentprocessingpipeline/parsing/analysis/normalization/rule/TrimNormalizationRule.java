package com.rag.documentprocessingpipeline.parsing.analysis.normalization.rule;

import org.springframework.stereotype.Component;

@Component
public class TrimNormalizationRule
        implements TextNormalizationRule {

    @Override
    public String normalize(String text) {

        return text.trim();

    }

}