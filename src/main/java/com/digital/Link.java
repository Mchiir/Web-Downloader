package com.digital;

public class Link {
    private String link_name;
    private String website_id;
    private String total_elapsed_time;
    private String total_downloaded_kilobytes;

    public Link(String linkName, String websiteId, String totalElapsedTime, String totalDownloadedKilobytes) {
        this.link_name = linkName;
        this.website_id = websiteId;
        this.total_elapsed_time = totalElapsedTime;
        this.total_downloaded_kilobytes = totalDownloadedKilobytes;
    }
    @Override
    public String toString() {
        return "Linkname: "+ link_name;
    }

    public String getLink_name() { return link_name; }
    public String getWebsite_id() { return website_id; }
    public String getTotal_elapsed_time() { return total_elapsed_time; }
    public String getTotal_downloaded_kilobytes() { return total_downloaded_kilobytes; }

    public void setLink_name(String linkName) { this.link_name = linkName; }
    public void setWebsite_id(String websiteId) { this.website_id = websiteId; }
    public void setTotal_elapsed_time(String totalElapsedTime) { this.total_elapsed_time = totalElapsedTime; }
    public void setTotal_downloaded_kilobytes(String totalDownloadedKilobytes) { this.total_downloaded_kilobytes = totalDownloadedKilobytes; }
}
