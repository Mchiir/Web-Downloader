package com.urlShortner;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.util.regex.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebDownloader {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/web_downloader_db2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the URL of the website: ");
        String url = scanner.next();

        if (!isValidURL(url)) {
            System.out.println("Invalid URL. Please specify a valid url.");
            return;
        }

        try {
            String domain = getDomainName(url);
            String directory = createDirectory(domain);
            Connection conn = connectToDatabase();

            if (conn != null) {
                long websiteId = saveWebsiteRecord(conn, domain);
                LocalDateTime startTime = LocalDateTime.now();

                File homePageFile = downloadPage(url, directory, "index.html");
                System.out.println("Downloaded homepage: " + homePageFile.getAbsolutePath());

                List<String> externalLinks = extractExternalLinks(homePageFile);
                for (String link : externalLinks) {
                    long start = System.currentTimeMillis();
                    try {
                        File resource = downloadPage(link, directory, getFileNameFromURL(link));
                        long end = System.currentTimeMillis();
                        long elapsed = end - start;
                        long sizeKB = resource.length() / 1024;
                        System.out.printf("Downloaded %s (%d KB) in %d ms\n", link, sizeKB, elapsed);

                        saveLinkRecord(conn, websiteId, link, elapsed, sizeKB);
                    } catch (Exception e) {
                        System.out.println("Failed to download: " + link);
                    }
                }

                LocalDateTime endTime = LocalDateTime.now();
                saveWebsiteCompletion(conn, websiteId, startTime, endTime, calculateTotalDownloadedSize(directory));
                System.out.println("Download complete!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isValidURL(String url) {
        String regex = "^(https?://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$";
        return Pattern.matches(regex, url);
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain != null ? domain.replace("www.", "") : "unknown";
    }




    private static String createDirectory(String domain) {
        String directoryPath = "./" + domain;
        new File(directoryPath).mkdirs();
        return directoryPath;
    }

    private static File downloadPage(String url, String directory, String filename) throws IOException {
        Document document = Jsoup.connect(url).get();
        File file = new File(directory, filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(document.html());
        }
        return file;
    }

    private static List<String> extractExternalLinks(File file) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        Elements links = document.select("a[href]");
        List<String> externalLinks = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (isValidURL(href)) {
                externalLinks.add(href);
            }
        }
        return externalLinks;
    }

    private static String getFileNameFromURL(String url) throws MalformedURLException {
        String path = new URL(url).getPath();
        return path.isEmpty() ? "index.html" : path.substring(path.lastIndexOf('/') + 1);
    }

    private static Connection connectToDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish and return the connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
    }

    // Save website record
    private static long saveWebsiteRecord(Connection conn, String domain) throws SQLException {
            String sql = "INSERT INTO website (website_name, download_start_date_time) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, domain);
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

                // Execute the update
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Inserting website failed, no rows affected.");
                }

                // Retrieve the generated key
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    } else {
                        throw new SQLException("Inserting website failed, no ID obtained.");
                    }
                }
            }
        }


    private static void saveLinkRecord(Connection conn, long websiteId, String link, long elapsedTime, long sizeKB) throws SQLException {
        String sql = "INSERT INTO link (link_name, website_id, total_elapsed_time, total_downloaded_kilobytes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, link);
            stmt.setLong(2, websiteId);

            // Convert elapsed time (in milliseconds) to seconds
            long seconds = elapsedTime / 1000;
            String intervalString = seconds + " seconds"; // Format as "X seconds"

            stmt.setString(3, intervalString); // Insert the formatted interval string
            stmt.setLong(4, sizeKB);
            stmt.executeUpdate(); // Use executeUpdate instead of executeQuery
        }
    }



    private static void saveWebsiteCompletion(Connection conn, long websiteId, LocalDateTime startTime, LocalDateTime endTime, long totalSizeKB) throws SQLException {
        String sql = "UPDATE website SET download_end_date_time = ?, total_elapsed_time = ?, total_downloaded_kilobytes = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(endTime));

            // Calculate elapsed time in milliseconds and convert it to seconds
            long elapsedTime = Duration.between(startTime, endTime).toMillis();
            long seconds = elapsedTime / 1000; // Convert milliseconds to seconds
            String intervalString = seconds + " seconds"; // Format as "X seconds"

            stmt.setString(2, intervalString); // Insert the formatted interval string
            stmt.setLong(3, totalSizeKB);
            stmt.setLong(4, websiteId);
            stmt.executeUpdate(); // Use executeUpdate instead of executeQuery
        }
    }

    private static long calculateTotalDownloadedSize(String directory) {
        return Arrays.stream(new File(directory).listFiles())
                .mapToLong(File::length)
                .sum() / 1024; // in KB
    }
}