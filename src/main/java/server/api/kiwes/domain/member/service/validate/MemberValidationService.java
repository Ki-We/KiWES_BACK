package server.api.kiwes.domain.member.service.validate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.api.kiwes.domain.member.dto.CheckNicknameResponse;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.repository.MemberRepository;
import server.api.kiwes.response.BizException;

import javax.transaction.Transactional;

import static server.api.kiwes.domain.member.constant.MemberResponseType.NOT_FOUND_EMAIL;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.EXISTED_NCIKNAME;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.VALID_NICKNAME;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberValidationService {

    private final MemberRepository memberRepository;

    /**
     * 닉네임 중복 확인
     */
    public CheckNicknameResponse checkNickname(String nickName) {
        if (this.memberRepository.findNotDeletedByNickname(nickName.trim()).isPresent()) {
            return new CheckNicknameResponse(EXISTED_NCIKNAME.getValue());
        } else {
            return new CheckNicknameResponse(VALID_NICKNAME.getValue());
        }
    }

    /**
     * 회원가입 시, 가입한 이메일인지 확인
     */
    public Member validateEmail(String email) {
        return this.memberRepository.findNotDeletedByEmail(email).orElseThrow(() -> new BizException(NOT_FOUND_EMAIL));
    }
}
