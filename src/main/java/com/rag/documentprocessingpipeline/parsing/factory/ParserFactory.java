package com.rag.documentprocessingpipeline.parsing.factory;

import com.rag.documentprocessingpipeline.parsing.parser.DocumentParser;
import com.rag.upload.entity.Document;

public interface ParserFactory {

    DocumentParser getParser(
            String extension);

}