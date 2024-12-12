package org.example.crawler_api.service;

import org.example.crawler_api.model.Document;

import org.example.crawler_api.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public String addSyncDoc(int siteId, String url, String parentUrl, String status, int level, String insertDate, int nodeId) {
        return documentRepository.addSyncDocument(siteId, url, parentUrl, status, level, insertDate, nodeId);
    }

    public List<Document> getAllDocs() {
        return documentRepository.getAllDocs();
    }

    public List<Document> getDocumentsFromNode(String lastSyncDocDate) {
        return documentRepository.getDocumentsFromNode(lastSyncDocDate);
    }

    public void saveContent(int docId, String title, String content, String status, int httpStatus) {
        documentRepository.saveContent(docId, title, content, status, httpStatus);
    }


}
