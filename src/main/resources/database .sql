CREATE DATABASE IF NOT EXISTS `web_downloader_db`;

use `web_downloader_db`;
CREATE TABLE website (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         website_name VARCHAR(255) NOT NULL,
                         download_start_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         download_end_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         total_elapsed_time INT NOT NULL,
                         total_downloaded_kilobytes INT NOT NULL
);

CREATE TABLE link (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      link_name TEXT NOT NULL,
                      website_id INT NOT NULL,
                      total_elapsed_time INT NOT NULL,
                      total_downloaded_kilobytes INT NOT NULL,
                      FOREIGN KEY (website_id) REFERENCES website(id) ON DELETE CASCADE
);