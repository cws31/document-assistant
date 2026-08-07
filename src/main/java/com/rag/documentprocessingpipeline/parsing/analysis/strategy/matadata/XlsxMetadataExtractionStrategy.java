package com.rag.documentprocessingpipeline.parsing.analysis.strategy.matadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.model.MetadataKeys;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;

@Component
public class XlsxMetadataExtractionStrategy
        implements MetadataExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof XWPFDocument;

    }

    @Override
    public ParsedMetadata extract(
            AnalysisContext context) {

        XWPFDocument document = (XWPFDocument) context.getSourceDocument();

        POIXMLProperties.CoreProperties properties = document.getProperties()
                .getCoreProperties();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.TITLE,
                properties.getTitle());

        attributes.put(
                MetadataKeys.AUTHOR,
                properties.getCreator());

        attributes.put(
                "paragraphCount",
                document.getParagraphs().size());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}