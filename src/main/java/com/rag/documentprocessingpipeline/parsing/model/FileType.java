package com.rag.documentprocessingpipeline.parsing.model;

public enum FileType {

    PDF("pdf"),
    DOCX("docx"),
    TXT("txt"),
    XLSX("xlsx"),
    PPTX("pptx");

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static FileType fromExtension(String extension) {

        if (extension == null) {
            throw new IllegalArgumentException(
                    "File extension cannot be null.");
        }

        for (FileType type : values()) {

            if (type.extension.equalsIgnoreCase(extension)) {
                return type;
            }

        }

        throw new IllegalArgumentException(
                "Unsupported file extension: " + extension);

    }

}