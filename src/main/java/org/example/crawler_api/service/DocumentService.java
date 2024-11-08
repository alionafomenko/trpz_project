package org.example.crawler_api.service;

import org.example.crawler_api.model.Document;

import org.example.crawler_api.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    public DocumentService() {

    }


    public String addDoc(int siteId, String url, String parentUrl, String status, int level) {
        return documentRepository.addDocument(siteId, url, parentUrl, status, level);
    }

    public List<Document> getAllDocs() {
        return documentRepository.getAllDocs();
    }

    public void saveContent(int docId, String title, String content, String status, int httpStatus) {
        documentRepository.saveContent(docId, title, content, status, httpStatus);
    }


}
