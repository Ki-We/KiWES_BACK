package server.api.kiwes.domain.banner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.banner.dto.BannerResponse;
import server.api.kiwes.domain.banner.entity.Banner;
import server.api.kiwes.domain.banner.repository.BannerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerResponse> getBanners() {

//        List<Banner> banners = new ArrayList<>();
        List<Banner> banners = bannerRepository.findAllByOrderByPriorityAsc();

        List<BannerResponse> bannerResponses = new ArrayList<>();

        for (Banner banner : banners) {
            bannerResponses.add( BannerResponse.builder()
                    .id(banner.getId())
                    .imageUrl(banner.getImageUrl())
                    .type(banner.getType())
                    .build());
        }
        return bannerResponses;
    }
}
