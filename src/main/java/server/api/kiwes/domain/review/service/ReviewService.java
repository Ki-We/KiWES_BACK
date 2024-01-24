package server.api.kiwes.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleDto;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleInterface;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_member.service.ClubMemberService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.constant.ReviewResponseType;
import server.api.kiwes.domain.review.dto.*;
import server.api.kiwes.domain.review.entity.Review;
import server.api.kiwes.domain.review.repository.ReviewRepository;
import server.api.kiwes.response.BizException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ClubMemberService clubMemberService;

    /**
     * id로 Review 객체 찾아 반환
     */
    public Review findById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new BizException(ReviewResponseType.NOT_EXIST));
    }

    /**
     * 후기 등록
     */
    public void postReview(Club club, Member member, ReviewRegisterDto registerDto) {
        if(reviewRepository.existsByClubAndReviewer(club, member)){
            throw new BizException(ReviewResponseType.ALREADY_POSTED);
        }

        reviewRepository.save(Review.builder()
                .club(club)
                .reviewer(member)
                .reviewContent(registerDto.getContent())
                .reviewDate(getDateTime())
                .build());
    }


    /**
     * 후기 수정
     */
    public void modifyReview(Review review, ReviewRegisterDto registerDto) {
        review.modifyReview(registerDto.getContent(), getDateTime());
    }

    /**
     * 후기 삭제
     */
    public void deleteReview(Review review) {
        reviewRepository.deleteById(review.getId());
    }
    public void deleteReply(Review review) {
        review.deleteReply();
    }

    /**
     * 후기 모두 보기
     */
    public ReviewEntireResponseDto getEntire(Club club, Member member, int cursor) {
        List<Review> reviews = reviewRepository.findByClub(club);
        club.getReviews().stream()
                .map(review -> ReviewDetailDto.of(review, member))
                .collect(Collectors.toList());
        return ReviewEntireResponseDto.builder()
                .isHost(clubMemberService.findByClubAndMember(club, member).getIsHost())
                .userId(member.getId())
                .reviews(club.getReviews().stream()
                        .filter(review -> review.getId() >= cursor*7)
                        .limit(7)
                        .map(review -> ReviewDetailDto.of(review, member))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 후기에 대한 답글 달기
     */
    public void postReply(Member member, Review review, ReviewRegisterDto registerDto){
        review.setReply(member, registerDto.getContent(), getDateTime());
    }

    /**
     * 명시된 포맷으로 현재 시간을 문자열로 리턴하는 함수
     */
    private String getDateTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm"));
    }

    public List<ReviewMineDto>  findMyReview(Member member) {
        List<ReviewMineSimpleInterface> myReviews = reviewRepository.findAllMyReview(member);
        List<ReviewMineDto> reviewMineDtos = new ArrayList<>();
        for(ReviewMineSimpleInterface r : myReviews){
            reviewMineDtos.add(new ReviewMineDto(r.getReview_id(),r.getReview_content(),r.getReview_date().split(" ")[0],r.getClub_id(),r.getTitle(),true));
        }
        List<ReviewMineSimpleInterface> myClubReview = reviewRepository.findAllHostReview(member);
        for(ReviewMineSimpleInterface r : myClubReview){
            reviewMineDtos.add(new ReviewMineDto(r.getReview_id(),r.getReview_content(),r.getReview_date().split(" ")[0],r.getClub_id(),r.getTitle(),false));
        }
        reviewMineDtos.sort(Comparator.comparing(ReviewMineDto::getReviewId).reversed());
        return reviewMineDtos;
    }
}
