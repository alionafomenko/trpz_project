package org.example.crawler_api.controller;


import org.example.crawler_api.model.Content;
import org.example.crawler_api.model.Picture;
import org.example.crawler_api.model.Site;
import org.example.crawler_api.service.ContentService;
import org.example.crawler_api.service.PictureService;
import org.example.crawler_api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class SiteController {

    @Autowired SiteService siteService;
    @Autowired PictureService pictureService;
    @Autowired
    ContentService contentService;


    @GetMapping("/")
    public String mainP(Model model){

        List<Site> sitelist = siteService.getAllSites();
        model.addAttribute("sites", sitelist);
        return "main";
    }


    @GetMapping("/pictures/{siteId}/{pageNumber}")
    public String getAllPictures(@PathVariable int siteId, @PathVariable(required = false) Integer pageNumber, Model model) {
        System.out.println(siteId + "    /" + pageNumber );

        model.addAttribute("siteId", siteId);
        model.addAttribute("pageNumber", pageNumber);

        List<Picture> pictures = pictureService.getPicturesBySiteId(siteId, pageNumber);
        List<Picture> next_page_pictures = pictureService.getPicturesBySiteId(siteId, pageNumber+1);
        String isEmpty = String.valueOf(next_page_pictures.isEmpty());
        model.addAttribute("pictures",  pictures);
        model.addAttribute("next_page_is_empty",  isEmpty);
        return "pictures";
    }


    @GetMapping("/search/{searchPhrase}")
    public String getAllPictures(@PathVariable String searchPhrase,  Model model) {

        List<Content>  contentLinks  = contentService.getSearchContent(searchPhrase);
        model.addAttribute("contentLinks", contentLinks);
        model.addAttribute("searchPhrase", searchPhrase);

        return "search-result";
    }

    @PostMapping("/add-site")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addSite(@RequestBody Map<String, String> siteData) {
        String title = siteData.get("title");
        String url = siteData.get("url");

        String error = siteService.addSite(url, title);

        Map<String, String> response = new HashMap<>();
        response.put("error", error);
        System.out.println(error);

        // Return the error message in the response body
        return ResponseEntity.ok(response);
    }



}
