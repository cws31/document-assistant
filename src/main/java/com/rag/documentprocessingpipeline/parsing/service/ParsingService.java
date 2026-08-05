package com.rag.documentprocessingpipeline.parsing.service;

import com.rag.documentprocessingpipeline.parsing.model.ParsedDocument;
import com.rag.upload.entity.Document;

public interface ParsingService {

    ParsedDocument parse(Document document);

}