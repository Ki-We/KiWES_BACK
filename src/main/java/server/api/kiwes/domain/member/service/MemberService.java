package server.api.kiwes.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import server.api.kiwes.domain.member.constant.MemberResponseType;
import server.api.kiwes.domain.member.dto.MyPageResponse;
import server.api.kiwes.domain.member.dto.NickNameRequest;
import server.api.kiwes.domain.member.dto.MyIdResponse;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.repository.MemberRepository;
import server.api.kiwes.global.security.util.SecurityUtils;
import server.api.kiwes.response.BizException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 로그인된 Member 객체를 리턴하는 함수
     * @return Member
     */
    public Member getLoggedInMember(){
        return memberRepository.findById(SecurityUtils.getLoggedInUser().getId())
                .orElseThrow(()-> new BizException(MemberResponseType.NOT_LOGGED_IN_USER));
    }

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new BizException(MemberResponseType.NOT_LOGGED_IN_USER));
    }

    /**
     * 마이페이지-프로필 이미지 변경 (presigned URL)
     */
    public String changeProfileImg() {
        Long memberId = SecurityUtils.getLoggedInUser().getId();
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.setProfileImg(String.valueOf(UUID.randomUUID()));
        memberRepository.save(member);

        return member.getProfileImg();
    }

    public String getPreProfileImg() {
        Long memberId = SecurityUtils.getLoggedInUser().getId();
        Member member = memberRepository.findById(memberId).orElseThrow();
        return member.getProfileImg();
    }
    /**
     * 닉네임 중복 체크
     */
    public String nicknameDuplicateCheck(NickNameRequest nickNameRequest) {
        System.out.println(nickNameRequest.getNickname());
        if(nickNameRequest.getNickname().equals("NotSet")){return MemberResponseType.EXISTED_NICKNAME.getMessage();}
        if (memberRepository.findNotDeletedByNickname(nickNameRequest.getNickname()).isPresent()) {
            return MemberResponseType.EXISTED_NICKNAME.getMessage();
        } else {
            return MemberResponseType.VALID_NICKNAME.getMessage();
        }
    }
    /**
     * 자기소개 update
     */
    public String updateIntroduction(String introduction) {
        Long memberId = SecurityUtils.getLoggedInUser().getId();
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.setIntroduction(introduction);
        memberRepository.save(member);

        return member.getNickname();
    }

    /**
     * 마이페이지 GET
     */
    public MyPageResponse myPage(long memberId) throws ParseException {
        Member member = memberRepository.findById(memberId).orElseThrow();
        String birth= member.getBirth();
        int age =0;
        if(!birth.equals("NotSet")){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dueToDate = format.parse(birth);

            Calendar now = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(dueToDate);
            age = now.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
        }
        //프로필 사진, 닉네임, 국적, 나이, 성별, 소개
        return new MyPageResponse(member.getId(),"https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                member.getProfileImg()+".jpg", member.getNickname(), member.getNationality().getName(), age, member.getGender().getName(), member.getIntroduction());

    }
    public MyIdResponse myId() throws ParseException {
        Long memberId = SecurityUtils.getLoggedInUser().getId();
        Member member = memberRepository.findById(memberId).orElseThrow();

        return MyIdResponse.builder()
                .id(member.getId())
                .nickName(member.getNickname())
                .email(member.getEmail())
                .build();

    }

    public void setDefalutProfile(Long id) {
        memberRepository.findById(id).get().setProfileImg("profile");
    }
}
