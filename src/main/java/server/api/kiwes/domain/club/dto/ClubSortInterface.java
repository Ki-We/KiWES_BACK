package server.api.kiwes.domain.club.dto;

import server.api.kiwes.domain.heart.constant.HeartStatus;

public interface ClubSortInterface {
    Long getClub_id();
    String getTitle();
    String getThumbnail_url();
    String getDate();
    String getLocation();
    String getLatitude();
    HeartStatus getStatus();
    String getLongitude();
}
