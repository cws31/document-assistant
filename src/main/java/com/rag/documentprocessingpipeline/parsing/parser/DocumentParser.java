package com.rag.documentprocessingpipeline.parsing.parser;

import java.io.InputStream;

import com.rag.documentprocessingpipeline.parsing.model.FileType;
import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.upload.entity.Document;

public interface DocumentParser {

    FileType supportedType();

    ParsedDocument parse(
            Document document,
            InputStream inputStream);

}