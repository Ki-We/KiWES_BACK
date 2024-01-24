package server.api.kiwes.domain.club.dto;

import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.List;

public interface ClubApprovalWaitingSimpleInterface {
    Long getClub_id();
    String getTitle();
    String getThumbnail_url();
    String getDate();
    String getLocation_keyword();
    List<String> getLanguages();
    HeartStatus getStatus();
}
