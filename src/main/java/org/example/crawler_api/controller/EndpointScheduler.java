package org.example.crawler_api.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crawler_api.model.Document;
import org.example.crawler_api.model.Node;
import org.example.crawler_api.model.Site;
import org.example.crawler_api.service.DocumentService;
import org.example.crawler_api.service.NodeService;
import org.example.crawler_api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
public class EndpointScheduler {

    private final RestTemplate restTemplate;

    @Autowired
    DocumentService documentService;
    @Autowired
    SiteService siteService;
    @Autowired
    NodeService nodeService;

    @Autowired
    public EndpointScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void syncSites(Node node){
        String lastSyncSiteDate = node.getSyncSiteDate().toString().replace(" ", "%20");
        String url1 = "http://" + node.getIp() + ":" + node.getPort() + "/getsitesfromnode/" + lastSyncSiteDate ;
        System.out.println(url1);
        try {
            String response = restTemplate.getForObject(url1, String.class);
            //   System.out.println("Response: " + response);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Site> sites = objectMapper.readValue(response, new TypeReference<List<Site>>() {});
            Date lastDate = node.getSyncSiteDate();
            for (Site site : sites) {
                siteService.addSyncSite(site.getId(), site.getUrl(), site.getTitle(), site.getInsertDate().toString(), site.getDocumentCount(), site.getPictureCount());
                if (lastDate == null || site.getInsertDate().compareTo(lastDate) > 0){
                    lastDate = site.getInsertDate();
                }

            }
            System.out.println("lastDate" + lastDate);
            nodeService.updateSiteSyncDate(node.getId(), lastDate);

        } catch (Exception e) {
            System.err.println("Error to call endpoint: " + e.getMessage());
        }
    }

    public void syncDocs (Node node){
        String lastSyncDate = node.getSyncDocDate().toString().replace(" ", "%20");
        System.out.println(lastSyncDate + "replaced /////");
        String url = "http://" + node.getIp() + ":" + node.getPort() + "/getdocumentsfromnode/" + lastSyncDate;
        System.out.println(url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            //   System.out.println("Response: " + response);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Document> documents = objectMapper.readValue(response, new TypeReference<List<Document>>() {
            });
            Date lastDate = node.getSyncDocDate();

            for (Document document : documents) {
                //  System.out.println("document ----- " + document.getUrl());
                documentService.addSyncDoc(document.getSiteId(), document.getUrl(), document.getParentUrl(), document.getStatus(), document.getLevel(), document.getInsertDate().toString(), document.getNodeId());
                if (lastDate == null || document.getInsertDate().compareTo(lastDate) > 0) {
                    //   System.out.println("document.getInsertDate()  " + document.getInsertDate());
                    lastDate = document.getInsertDate();
                }

            }
            System.out.println("lastDate" + lastDate);
            nodeService.updateDocSyncDate(node.getId(), lastDate);

        } catch (Exception e) {
            System.err.println("Error to call endpoint: " + e.getMessage());
        }
    }

    @Async
    @Scheduled(fixedDelay = 60000)
    public void getDocumentsFromNode() {
        System.out.println("nodesync");
        List<Node> nodes  = nodeService.getAllNodes();
        for (Node node : nodes) {

            syncSites(node);

            syncDocs(node);

        }
    }


}

