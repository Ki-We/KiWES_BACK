package server.api.kiwes.domain.banner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.api.kiwes.domain.banner.dto.BannerResponse;
import server.api.kiwes.domain.banner.entity.Banner;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByOrderByPriorityAsc();
}
