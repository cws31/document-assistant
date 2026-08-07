package com.rag.documentprocessingpipeline.parsing.analysis.analyzer;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;

public abstract class AbstractDocumentAnalyzer
        implements DocumentAnalyzer {

    @Override
    public final void analyze(
            AnalysisContext context) {

        validate(context);

        doAnalyze(context);

    }

    /**
     * Executes analyzer-specific logic.
     */
    protected abstract void doAnalyze(
            AnalysisContext context);

    /**
     * Framework-level validation.
     *
     * This validates only the minimum requirements needed
     * for an analyzer to execute safely.
     *
     * Business validation (text extracted, OCR success,
     * metadata completeness, etc.) belongs to
     * ValidationAnalyzer.
     */
    protected void validate(
            AnalysisContext context) {

        if (context == null) {
            throw new IllegalArgumentException(
                    "AnalysisContext cannot be null.");
        }

        if (context.getDocument() == null) {
            throw new IllegalArgumentException(
                    "Document cannot be null.");
        }

        if (context.getFileType() == null) {
            throw new IllegalArgumentException(
                    "FileType cannot be null.");
        }

    }

}