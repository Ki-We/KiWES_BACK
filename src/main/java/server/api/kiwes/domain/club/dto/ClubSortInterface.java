package server.api.kiwes.domain.club.dto;

public interface ClubSortInterface {
    Long getClub_id();
    String getTitle();
    String getThumbnail_url();
    String getDate();
    String getLocation();

    String getLatitude();

    String getLongitude();
}
