package com.rag.documentprocessingpipeline.parsing.analysis.strategy.matadata;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.model.MetadataKeys;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;
import com.rag.upload.entity.Document;

@Component
public class TxtMetadataExtractionStrategy
        implements MetadataExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getFileType() == FileType.TXT;

    }

    @Override
    public ParsedMetadata extract(
            AnalysisContext context) {

        Document document = context.getDocument();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.TITLE,
                document.getOriginalFileName());

        attributes.put(
                "encoding",
                StandardCharsets.UTF_8.name());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}