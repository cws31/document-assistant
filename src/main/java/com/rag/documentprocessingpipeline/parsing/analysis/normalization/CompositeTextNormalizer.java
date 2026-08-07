package com.rag.documentprocessingpipeline.parsing.analysis.normalization;

import java.util.List;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.normalization.rule.TextNormalizationRule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompositeTextNormalizer {

    private final List<TextNormalizationRule> rules;

    public String normalize(String text) {

        String normalized = text;

        for (TextNormalizationRule rule : rules) {

            normalized = rule.normalize(normalized);

        }

        return normalized;

    }

}