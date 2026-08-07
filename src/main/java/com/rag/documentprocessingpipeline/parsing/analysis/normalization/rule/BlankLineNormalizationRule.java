package com.rag.documentprocessingpipeline.parsing.analysis.normalization.rule;

import org.springframework.stereotype.Component;

@Component
public class BlankLineNormalizationRule
        implements TextNormalizationRule {

    @Override
    public String normalize(String text) {

        return text.replaceAll("\\n{3,}", "\n\n");

    }

}