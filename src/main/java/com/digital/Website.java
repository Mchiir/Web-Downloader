package com.digital;

public class Website {
    private String website_name;
    private String download_start_date_time;
    private String download_end_date_time;
    private String total_elapsed_time;
    private String total_downloaded_kilobytes;

    public Website(
            String website_name,
            String download_start_date_time,
            String download_end_date_time,
            String total_elapsed_time,
            String total_downloaded_kilobytes
            ) {
        this.website_name = website_name;
        this.download_start_date_time = download_start_date_time;
        this.download_end_date_time = download_end_date_time;
        this.total_elapsed_time = total_elapsed_time;
        this.total_downloaded_kilobytes = total_downloaded_kilobytes;
    }
    @Override
    public String toString() {
        return website_name;
    }

    public String getWebsite_name() { return website_name; }
    public String getDownload_start_date_time() { return download_start_date_time; }
    public String getDownload_end_date_time() { return download_end_date_time; }
    public String getTotal_elapsed_time() { return total_elapsed_time; }
    public String getTotal_downloaded_kilobytes() { return total_downloaded_kilobytes; }

    public void setWebsite_name(String website_name) { this.website_name = website_name; }
    public void setDownload_start_date_time(String download_start_date_time) { this.download_start_date_time = download_start_date_time; }
    public void setDownload_end_date_time(String download_end_date_time) { this.download_end_date_time = download_end_date_time; }
    public void setTotal_elapsed_time(String total_elapsed_time) { this.total_elapsed_time = total_elapsed_time; }
    public void setTotal_downloaded_kilobytes(String total_downloaded_kilobytes) { this.total_downloaded_kilobytes = total_downloaded_kilobytes; }
}
