package com.rag.documentprocessingpipeline.parsing.factory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.parser.DocumentParser;
import com.rag.upload.entity.Document;

@Component
public class ParserFactoryImpl implements ParserFactory {

    private final Map<FileType, DocumentParser> parserMap = new EnumMap<>(FileType.class);

    public ParserFactoryImpl(
            List<DocumentParser> parsers) {

        for (DocumentParser parser : parsers) {

            parserMap.put(
                    parser.supportedType(),
                    parser);

        }

    }

    @Override
    public DocumentParser getParser(
            String extension) {

        FileType type = FileType.fromExtension(extension);

        DocumentParser parser = parserMap.get(type);

        if (parser == null) {

            throw new IllegalArgumentException(
                    "No parser registered for "
                            + extension);

        }

        return parser;

    }

}