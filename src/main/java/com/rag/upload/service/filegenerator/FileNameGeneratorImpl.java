package com.rag.upload.service.filegenerator;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileNameGeneratorImpl implements FileNameGenerator {

    @Override
    public String generate(MultipartFile file) {

        String originalName = file.getOriginalFilename();

        String extension = originalName.substring(
                originalName.lastIndexOf('.') + 1);

        return UUID.randomUUID()
                + "."
                + extension.toLowerCase();

    }
}
