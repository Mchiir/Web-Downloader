package com.digital;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class URLValidator {
    private static final String URL_REGEX =
            "^(https?://)?" +
                    "(([\\w\\d\\-]+\\.)+[\\w\\d\\-]+)" +
                    "(:\\d+)?(/[^\\s]*)?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    public static String getShortUrl(String url) {
        return url;
    }

    public static String validateURL(String inputUrl) throws Exception {
        URL url = new URL(inputUrl);
        if (!url.getProtocol().startsWith("http")) {
            throw new IllegalArgumentException("Invalid URL: Protocol must be HTTP or HTTPS");
        }

        if (!URL_PATTERN.matcher(inputUrl).matches()) {
            throw new IllegalArgumentException("Invalid URL: Does not match URL format");
        }

        return url.getHost();
    }

    public static String resolveFullURL(String baseUrl, String relativeUrl) throws URISyntaxException {
        URI baseURI = new URI(baseUrl);
        URI resolvedURI = baseURI.resolve(relativeUrl);
        return resolvedURI.toString();
    }
}
