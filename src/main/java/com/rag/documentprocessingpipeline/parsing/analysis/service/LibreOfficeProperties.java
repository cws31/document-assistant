package com.rag.documentprocessingpipeline.parsing.analysis.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rag.libreoffice")
public class LibreOfficeProperties {

    /**
     * Path to LibreOffice executable.
     */
    private String executablePath;

    /**
     * Maximum time allowed for one conversion.
     */
    private long timeoutSeconds = 60;
}