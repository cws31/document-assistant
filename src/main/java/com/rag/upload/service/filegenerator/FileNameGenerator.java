package com.rag.upload.service.filegenerator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public interface FileNameGenerator {

    String generate(MultipartFile file);

}