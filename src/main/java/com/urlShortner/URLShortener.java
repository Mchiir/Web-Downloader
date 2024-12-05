package com.urlShortner;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class URLShortener {

    // Map to store long URLs and their corresponding short URLs
    private static Map<String, String> urlMap = new HashMap<>();
    private static int idCounter = 1;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                // Prompt user for URL input
                System.out.print("Enter a website URL (or type 'exit' to quit): ");
                String inputUrl = scanner.nextLine();

                // Exit condition
                if (inputUrl.equalsIgnoreCase("exit")) {
                    break;
                }

                // Validate URL
                if (validateURL(inputUrl)) {
                    // Generate short URL and display both URLs
                    String shortUrl = generateShortUrl(inputUrl);
                    System.out.println("Long URL: " + inputUrl);
                    System.out.println("Short URL: " + shortUrl);
                } else {
                    System.out.println("Invalid URL. Please enter a valid URL.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Validate URL using regex
    public static boolean validateURL(String url) {
        String urlRegex = "^(https?://)?" +
                "(([\\w\\d\\-]+\\.)+[\\w\\d\\-]+)" +
                "(:\\d+)?(/[^\\s]*)?$";
        return url.matches(urlRegex);
    }

    // Generate a short URL based on a unique ID
    public static String generateShortUrl(String longUrl) {
        String shortUrl = "http://short.url/" + idCounter;
        urlMap.put(shortUrl, longUrl); // Store the mapping
        idCounter++; // Increment the ID counter for the next URL
        return shortUrl;
    }
}