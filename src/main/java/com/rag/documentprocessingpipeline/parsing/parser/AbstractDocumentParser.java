package com.rag.documentprocessingpipeline.parsing.parser;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

public abstract class AbstractDocumentParser
        implements DocumentParser {

    protected String normalize(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\u00A0", " ")
                .replaceAll("\\r\\n", "\n")
                .replaceAll("\\n{3,}", "\n\n")
                .replaceAll("[ \\t]+", " ")
                .trim();

    }

    protected void validateExtractedText(
            String extractedText) {

        if (extractedText == null
                || extractedText.isBlank()) {

            throw new ParsingException(
                    "Document does not contain readable text.");

        }

    }

}