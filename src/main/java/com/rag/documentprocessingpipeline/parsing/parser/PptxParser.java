package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.factory.ParsedDocumentFactory;
import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.model.MetadataKeys;
import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;
import com.rag.upload.entity.Document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PptxParser extends AbstractDocumentParser {

    private final ParsedDocumentFactory parsedDocumentFactory;

    @Override
    public FileType supportedType() {
        return FileType.PPTX;
    }

    @Override
    public ParsedDocument parse(
            Document document,
            InputStream inputStream) {

        log.info(
                "Starting PPTX parsing. documentId={}",
                document.getId());

        try (XMLSlideShow slideShow = new XMLSlideShow(inputStream)) {

            String extractedText = extractText(slideShow);

            ParsedMetadata metadata = extractMetadata(slideShow);

            ParsedDocument parsedDocument = parsedDocumentFactory.create(
                    document,
                    extractedText,
                    metadata);

            log.info(
                    "Successfully parsed PPTX document. documentId={}",
                    document.getId());

            return parsedDocument;

        } catch (IOException ex) {

            log.error(
                    "Failed to parse PPTX document. documentId={}",
                    document.getId(),
                    ex);

            throw new ParsingException(
                    "Unable to parse PPTX document.",
                    ex);

        }

    }

    private String extractText(
            XMLSlideShow slideShow) {

        StringBuilder builder = new StringBuilder();

        int slideNumber = 1;

        for (XSLFSlide slide : slideShow.getSlides()) {

            builder.append("Slide ")
                    .append(slideNumber++)
                    .append(System.lineSeparator());

            for (XSLFShape shape : slide.getShapes()) {

                if (shape instanceof XSLFTextShape textShape) {

                    String text = textShape.getText();

                    if (text != null &&
                            !text.isBlank()) {

                        builder.append(text)
                                .append(System.lineSeparator());

                    }

                }

            }

            builder.append(System.lineSeparator());

        }

        String extractedText = normalize(builder.toString());

        validateExtractedText(
                extractedText);

        return extractedText;

    }

    private ParsedMetadata extractMetadata(
            XMLSlideShow slideShow) {

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                "slideCount",
                slideShow.getSlides().size());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}