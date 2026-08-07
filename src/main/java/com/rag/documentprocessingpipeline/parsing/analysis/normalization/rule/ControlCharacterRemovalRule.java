package com.rag.documentprocessingpipeline.parsing.analysis.normalization.rule;

import org.springframework.stereotype.Component;

@Component
public class ControlCharacterRemovalRule
        implements TextNormalizationRule {

    @Override
    public String normalize(String text) {

        return text.replaceAll("[\\p{Cntrl}&&[^\n\t]]", "");

    }

}