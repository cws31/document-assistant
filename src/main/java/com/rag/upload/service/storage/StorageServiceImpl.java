package com.rag.upload.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rag.upload.config.UploadProperties;
import com.rag.upload.entity.Document;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final UploadProperties uploadProperties;

    @Override
    public String store(MultipartFile file, String storedFileName) throws IOException {

        LocalDate today = LocalDate.now();

        Path directory = Paths.get(
                uploadProperties.getStorageLocation(),
                String.valueOf(today.getYear()),
                String.format("%02d", today.getMonthValue()));

        Files.createDirectories(directory);

        Path destination = directory.resolve(storedFileName);

        file.transferTo(destination);

        return destination.toAbsolutePath().toString();
    }

    @Override
    public void delete(String storagePath) throws IOException {

        Path path = Paths.get(
                uploadProperties.getStorageLocation(),
                storagePath);

        Files.deleteIfExists(path);

    }

    @Override
    public InputStream read(Document document)
            throws IOException {

        Path path = Paths.get(
                uploadProperties.getStorageLocation(),
                document.getStoredFileName());

        return Files.newInputStream(path);

    }
}