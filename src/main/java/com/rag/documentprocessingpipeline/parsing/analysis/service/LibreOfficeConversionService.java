package com.rag.documentprocessingpipeline.parsing.analysis.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibreOfficeConversionService {

    private final LibreOfficeProperties properties;

    public Path convertToPdf(
            Path sourceFile,
            Path outputDirectory) {

        validateInput(sourceFile);

        try {

            Files.createDirectories(outputDirectory);

            ProcessBuilder processBuilder = new ProcessBuilder(
                    properties.getExecutablePath(),
                    "--headless",
                    "--convert-to",
                    "pdf",
                    "--outdir",
                    outputDirectory.toAbsolutePath().toString(),
                    sourceFile.toAbsolutePath().toString());

            processBuilder.redirectErrorStream(true);

            log.debug(
                    "Starting LibreOffice conversion. source={}",
                    sourceFile);

            Process process = processBuilder.start();

            String output = new String(
                    process.getInputStream().readAllBytes());

            boolean finished = process.waitFor(
                    properties.getTimeoutSeconds(),
                    TimeUnit.SECONDS);

            if (!finished) {

                process.destroyForcibly();

                throw new ParsingException(
                        "LibreOffice conversion timed out.");
            }

            if (process.exitValue() != 0) {

                throw new ParsingException(
                        "LibreOffice conversion failed. " +
                                "exitCode=" + process.exitValue() +
                                ", output=" + output);
            }

            String fileName = sourceFile.getFileName()
                    .toString();

            int extensionIndex = fileName.lastIndexOf('.');

            String pdfFileName = extensionIndex > 0
                    ? fileName.substring(
                            0,
                            extensionIndex)
                            + ".pdf"
                    : fileName + ".pdf";

            Path pdfPath = outputDirectory.resolve(pdfFileName);

            if (!Files.exists(pdfPath)) {

                throw new ParsingException(
                        "LibreOffice completed but PDF was not created.");
            }

            log.debug(
                    "LibreOffice conversion completed. pdf={}",
                    pdfPath);

            return pdfPath;

        } catch (IOException ex) {

            throw new ParsingException(
                    "Unable to execute LibreOffice.",
                    ex);

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            throw new ParsingException(
                    "LibreOffice conversion was interrupted.",
                    ex);
        }
    }

    private void validateInput(
            Path sourceFile) {

        if (sourceFile == null) {

            throw new IllegalArgumentException(
                    "Source file cannot be null.");
        }

        if (!Files.exists(sourceFile)) {

            throw new ParsingException(
                    "Source file does not exist: "
                            + sourceFile);
        }

        if (!Files.isRegularFile(sourceFile)) {

            throw new ParsingException(
                    "Source path is not a regular file: "
                            + sourceFile);
        }
    }
}