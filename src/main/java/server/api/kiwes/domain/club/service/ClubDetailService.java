package server.api.kiwes.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.dto.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_category.entity.ClubCategory;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.club_member.repository.ClubMemberRepository;
import server.api.kiwes.domain.heart.service.HeartService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.entity.Nationality;
import server.api.kiwes.domain.qna.constant.QnaDeletedStatus;
import server.api.kiwes.domain.qna.entity.Qna;
import server.api.kiwes.domain.review.entity.Review;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubDetailService {
    private final HeartService heartService;
    private final ClubMemberRepository clubMemberRepository;

    public ClubMemberInfoDto getClubSimple(Club club) {
        Member host = getHostFrom(club);
        List<ClubMembersInfoDto> members = clubMemberRepository.findAllMembersInClub(club);
        for (ClubMembersInfoDto member : members) {
            String thumbnail = member.getThumbnail();
            thumbnail = "https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/" + thumbnail + ".jpg";
            member.setThumbnail(thumbnail);
        }
        ClubMemberInfoDto memberInfoDto = ClubMemberInfoDto.builder()
                .hostId(host.getId())
                .hostThumbnailImage("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                        host.getProfileImg()+".jpg")
                .hostNickname(host.getNickname())
                .currentPeople(club.getCurrentPeople())
                .title(club.getTitle())
                .Members(members)
                .build();
        return memberInfoDto;
    }

    /**
     * 모임 상세정보 페이지에 필요한 정보들 리턴
     */
    public ClubArticleResponseDto getClubDetail(Member member, Club club) {
        ClubArticleBaseInfoDto baseInfoDto;
        ClubArticleMemberInfoDto memberInfoDto;
        List<ClubArticleQnaDto> qnas = new ArrayList<>();
        List<ClubArticleReviewDto> reviews = new ArrayList<>();

        Member host = getHostFrom(club);
        Map<Nationality, Integer> memberNationCountMap = getNationCountFrom(club);

        baseInfoDto = ClubArticleBaseInfoDto.builder()
                .clubId(club.getId())
                .title(club.getTitle())
                .maxPeople(club.getMaxPeople())
                .thumbnailImageUrl("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/clubThumbnail/"+
                        club.getThumbnailUrl())
                .heartCount(club.getHearts().size())
                .tags(getTagList(club))
                .date(formateDate(club.getDate()))
                .dueTo(formateDate(club.getDueTo()))
                .cost(club.getCost())
                .gender(club.getGender().getName())
                .locationKeyword(club.getLocationKeyword())
                .content(club.getContent())
                .location(club.getLocation())
                .latitude(club.getLatitude())
                .longitude(club.getLongitude())
                .dateInfo(new String[]{club.getDate(), club.getDueTo()})
                .build();
        memberInfoDto = ClubArticleMemberInfoDto.builder()
                .hostId(host.getId())
                .hostThumbnailImage("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                        host.getProfileImg()+".jpg")
                .hostNickname(host.getNickname())
                .koreanCount(memberNationCountMap.get(Nationality.KOREA))
                .foreignerCount(memberNationCountMap.get(Nationality.FOREIGN))
                .maxPeople(club.getMaxPeople())
                .build();


        // qnaDtos 채우기 - 최대 2개
        for(Qna qna : club.getQnas()){
            if(qna.getIsDeleted().equals(QnaDeletedStatus.YES)) continue;
            qnas.add(ClubArticleQnaDto.of(qna));

            if(qnas.size() > 2) break;
        }

        // reviews 채우기 - 최대 2개
        for(Review review : club.getReviews()){
            reviews.add(ClubArticleReviewDto.of(review));

            if(reviews.size() > 2) break;
        }


        return ClubArticleResponseDto.builder()
                .baseInfo(baseInfoDto)
                .memberInfo(memberInfoDto)
                .qnas(qnas)
                .reviews(reviews)
                .isHost(host.getId().equals(member.getId()))
                .isHeart(heartService.getHearted(club, member))
                .isActivated(club.getIsActivated())
                .build();
    }

    /**
     * 모임 상세정보에 들어갈 tag 리스트 구성
     */
    private List<String> getTagList(Club club){
        List<String> tags = new ArrayList<>();

        tags.add(club.getCategory().getCategory().getName().getName());


        for(ClubLanguage clubLanguage : club.getLanguages()){
            tags.add(clubLanguage.getLanguage().getName().getName());
        }

        return tags;
    }

    /**
     * 인자로 넘겨받은 Club의 호스트를 리턴한다.
     */
    private Member getHostFrom(Club club){
        List<ClubMember> members = club.getMembers();
        for(ClubMember clubMember : members){
            if(clubMember.getIsHost()){
                return clubMember.getMember();
            }
        }

        return null;
    }

    /**
     * 승인된 클럽 구성원의 국적 숫자 리턴. (한국인, 외국인)
     */
    private Map<Nationality, Integer> getNationCountFrom(Club club){
        Integer korean = 0;
        Integer foreigner = 0;
        List<ClubMember> clubMembers = club.getMembers();
        for(ClubMember clubMember : clubMembers){
            if(clubMember.getIsApproved()){
                Nationality nationality = clubMember.getMember().getNationality();
                if(nationality == Nationality.KOREA) korean++;
                else if(nationality == Nationality.FOREIGN) foreigner++;
            }
        }

        Map<Nationality, Integer> result = new HashMap<>();
        result.put(Nationality.KOREA, korean);
        result.put(Nationality.FOREIGN, foreigner);

        return result;
    }



    /**
     * 날짜를 Mar 2 의 포맷으로 변경
     */
    private String formateDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        Month month = date.getMonth();
        String monthString;
        switch (month){
            case JANUARY:
                monthString = "Jan";
                break;
            case FEBRUARY:
                monthString = "Feb";
                break;
            case MARCH:
                monthString = "Mar";
                break;
            case APRIL:
                monthString = "Apr";
                break;
            case MAY:
                monthString = "May";
                break;
            case JUNE:
                monthString = "Jun";
                break;
            case JULY:
                monthString = "Jul";
                break;
            case AUGUST:
                monthString = "Aug";
                break;
            case SEPTEMBER:
                monthString = "Sep";
                break;
            case OCTOBER:
                monthString = "Oct";
                break;
            case NOVEMBER:
                monthString = "Nov";
                break;
            case DECEMBER:
                monthString = "Dec";
                break;
            default:
                monthString = "null";
        }

        return monthString + " " + date.getDayOfMonth();
    }
}
