package com.rag.upload.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rag.upload")
public class UploadProperties {

    private DataSize maxFileSize;

    private List<String> allowedExtensions;

    private List<String> allowedContentTypes;

    private String storageLocation;

    private boolean overwriteExisting = false;

}