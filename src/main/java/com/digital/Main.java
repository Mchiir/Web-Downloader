package com.digital;

import java.io.File;
import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new java.util.Scanner(System.in)) {
            // Step 1: Input and validate URL
            System.out.print("Enter website URL: ");
            String inputUrl = scanner.nextLine();
            String domainName = URLValidator.validateURL(inputUrl);

            // Step 2: Create directory for the website
            String saveDir = System.getProperty("user.dir") + File.separator + domainName;
            new File(saveDir).mkdirs();

            // Step 3: Record start time
            long startTime = System.currentTimeMillis();

            System.out.println("Downloading home page...");
            long totalDownloadedKB = FileDownloader.downloadFile(inputUrl, saveDir);

            // Step 4: Extract links from the downloaded home page
            System.out.println("Extracting links...");
            List<String> links = LinkExtractor.extractLinks(saveDir + File.separator + "index.html");

            DBConnector db = new DBConnector();
            try (Connection conn = db.getConnection()) {
                conn.setAutoCommit(false);

                // Step 5: Insert website details into the database
                String insertWebsiteSQL = "INSERT INTO website (website_name, total_elapsed_time, total_downloaded_kilobytes) VALUES (?, ?, ?)";
                PreparedStatement websiteStmt = conn.prepareStatement(insertWebsiteSQL, Statement.RETURN_GENERATED_KEYS);
                websiteStmt.setString(1, domainName);
                websiteStmt.setInt(2, 0); // Placeholder for total elapsed time
                websiteStmt.setInt(3, 0); // Placeholder for total downloaded KB
                websiteStmt.executeUpdate();

                // Get the generated website ID
                ResultSet rs = websiteStmt.getGeneratedKeys();
                rs.next();
                int websiteId = rs.getInt(1);

                // Step 6: Download and record each link
                for (String link : links) {
                    System.out.println("Downloading: " + link);
                    long linkStartTime = System.currentTimeMillis();
                    long downloadedKB = FileDownloader.downloadFile(link, saveDir);
                    long linkEndTime = System.currentTimeMillis();
                    int elapsedTime = (int) (linkEndTime - linkStartTime);

                    // Insert link details into the database
                    String insertLinkSQL = "INSERT INTO link (link_name, website_id, total_elapsed_time, total_downloaded_kilobytes) VALUES (?, ?, ?, ?)";
                    PreparedStatement linkStmt = conn.prepareStatement(insertLinkSQL);
                    linkStmt.setString(1, link);
                    linkStmt.setInt(2, websiteId);
                    linkStmt.setInt(3, elapsedTime);
                    linkStmt.setInt(4, (int) downloadedKB);
                    linkStmt.executeUpdate();

                    totalDownloadedKB += downloadedKB;
                }

                // Step 7: Update website details with total elapsed time and downloaded KB
                long endTime = System.currentTimeMillis();
                int totalElapsedTime = (int) (endTime - startTime);

                String updateWebsiteSQL = "UPDATE website SET total_elapsed_time = ?, total_downloaded_kilobytes = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateWebsiteSQL);
                updateStmt.setInt(1, totalElapsedTime);
                updateStmt.setInt(2, (int) totalDownloadedKB);
                updateStmt.setInt(3, websiteId);
                updateStmt.executeUpdate();

                conn.commit();

                // Final report
                System.out.println("Website download completed!");
                System.out.println("Summary:");
                System.out.println("Website: " + domainName);
                System.out.println("Total Links Downloaded: " + links.size());
                System.out.println("Total Downloaded: " + totalDownloadedKB + " KB");
                System.out.println("Elapsed Time: " + totalElapsedTime + " ms");
            }
        }catch (Exception e) {
            System.out.println("Error occured, "+ e.getMessage());
        }
    }
}