package server.api.kiwes.domain.banner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BannerResponse {

    private Long id;

    private String imageUrl;

    private String type;
}
