package org.example.crawler_api.job;

import org.example.crawler_api.service.DocumentService;
import org.example.crawler_api.service.PictureService;
import org.example.crawler_api.service.SiteService;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

@Component
public class ParseTask {

    @Autowired
    PictureService pictureService;
    @Autowired
    DocumentService documentService;

    @Scheduled(fixedDelay = 10000)
    public void parseSite() {

        List<org.example.crawler_api.model.Document> documentList = documentService.getAllDocs();

        for (org.example.crawler_api.model.Document document : documentList) {
           // System.out.println("Level------- " + document.getLevel());
                int level = document.getLevel();
                if (level < 5) {
                    level++;
                    String url = document.getUrl();
                    System.out.println(url);
                    StringBuilder content = new StringBuilder();
                    String title = "";
                    int httpStatus = 0;
                    try {
                        Document doc = Jsoup.connect(url)
                                .userAgent("AlionaCrawler")
                                .timeout(5000)
                                .followRedirects(true)
                                .referrer("https://google.com")
                                .get();
                        Elements sites = doc.getAllElements();
                        httpStatus = doc.connection().response().statusCode();
                        for (Element el : sites) {
                            String tagName = el.tagName();
                            if (tagName.equals("a")) {
                                String link = el.attr("href");
                                if (!link.endsWith(".jpg") && !link.endsWith(".png") && !link.endsWith(".jpeg") && !link.endsWith(".svg")) {
                                    if (!link.startsWith("http")) {
                                        if (link.startsWith("/")) {
                                            documentService.addDoc(document.getSiteId() ,link, document.getUrl(), "to_do", level);
                                        }
                                    } else {
                                        documentService.addDoc(document.getSiteId() ,link, document.getUrl(), "external_link", 0);
                                    }
                                } else {
                                    pictureService.addPicture(document.getSiteId(), link, document.getUrl());
                                }
                            } else if (tagName.equals("img")) {
                                String picLink = el.attr("src");
                                pictureService.addPicture(document.getSiteId(), picLink, document.getUrl());
                            } else if (tagName.equals("title")) {
                                title = el.ownText();
                            }

                            String text = el.ownText();
                            if (!text.isEmpty()) {
                                content.append("<p>").append(text).append("</p>\n");

                            }
                        }
                        String finalContent = content.toString();
                        documentService.saveContent(document.getId(), title, finalContent, "scanned", httpStatus);


                    } catch (HttpStatusException e) {
                        httpStatus = e.getStatusCode();
                    } catch (SocketTimeoutException e) {
                        httpStatus = 0;
                    } catch (UnsupportedMimeTypeException e) {
                        httpStatus = 415;
                    } catch (IOException e) {
                        httpStatus = 500;
                    } finally {

                        documentService.saveContent(document.getId(), title, content.toString(), "scanned", httpStatus);
                    }
                }
        }


    }
}
