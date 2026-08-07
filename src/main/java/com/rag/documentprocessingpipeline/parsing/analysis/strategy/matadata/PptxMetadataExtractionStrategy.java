package com.rag.documentprocessingpipeline.parsing.analysis.strategy.matadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.model.MetadataKeys;
import com.rag.documentprocessingpipeline.parsing.model.ParsedMetadata;

@Component
public class PptxMetadataExtractionStrategy
        implements MetadataExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context.getSourceDocument() instanceof XMLSlideShow;

    }

    @Override
    public ParsedMetadata extract(
            AnalysisContext context) {

        XMLSlideShow slideShow = (XMLSlideShow) context.getSourceDocument();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(
                MetadataKeys.SLIDE_COUNT,
                slideShow.getSlides().size());

        return ParsedMetadata.builder()
                .attributes(attributes)
                .build();

    }

}