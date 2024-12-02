package com.digital;

import java.net.URL;

public class URLValidator {
    public static String getShortUrl(String url) {
        return url;
    }

        public static String validateURL(String inputUrl) throws Exception {
            URL url = new URL(inputUrl);
            if (!url.getProtocol().startsWith("http")) {
                throw new IllegalArgumentException("Invalid URL: Protocol must be HTTP or HTTPS");
            }

            Url urlShortner = URLValidator::getShortUrl;
            return url.getHost();
        }
}
