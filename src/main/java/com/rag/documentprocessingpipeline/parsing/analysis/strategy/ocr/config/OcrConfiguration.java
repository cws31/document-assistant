package com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.rag.documentprocessingpipeline.parsing.analysis.strategy.ocr.properties.OcrProperties;

@Configuration
@EnableConfigurationProperties(OcrProperties.class)
public class OcrConfiguration {

}