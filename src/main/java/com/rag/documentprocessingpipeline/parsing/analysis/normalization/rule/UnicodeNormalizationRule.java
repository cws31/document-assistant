package com.rag.documentprocessingpipeline.parsing.analysis.normalization.rule;

import org.springframework.stereotype.Component;

@Component
public class UnicodeNormalizationRule
        implements TextNormalizationRule {

    @Override
    public String normalize(String text) {

        if (text == null) {
            return "";
        }

        return text.replace('\u00A0', ' ');

    }

}