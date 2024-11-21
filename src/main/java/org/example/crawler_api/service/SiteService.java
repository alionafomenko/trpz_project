package org.example.crawler_api.service;

import netscape.javascript.JSObject;
import org.example.crawler_api.model.Site;
import org.example.crawler_api.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SiteService {

    @Autowired
    SiteRepository siteRepository;

    public SiteService(){

    }

    public String addSite(String url, String title) {
        return siteRepository.addSite(url, title);
    }

    public List<Site> getAllSites(){
        return siteRepository.getAllSites();
    }

    public List<Site> getAllSitesForAdmin(){
        return siteRepository.getAllSitesForAdmin();
    }



}
