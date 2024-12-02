package com.digital;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LinkExtractor {
    public static List<String> extractLinks(String filePath) throws Exception {
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.parse(new File(filePath), "UTF-8");
        for (Element link : doc.select("a[href]")) {
            String url = link.attr("abs:href");
            if (!url.isEmpty() && !links.contains(url)) {
                links.add(url);
            }
        }
        return links;
    }
}