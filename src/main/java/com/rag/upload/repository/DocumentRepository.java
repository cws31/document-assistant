package com.rag.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rag.upload.entity.Document;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {
}
