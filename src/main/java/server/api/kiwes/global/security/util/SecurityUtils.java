package server.api.kiwes.global.security.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.global.security.service.CustomUserDetails;
import server.api.kiwes.response.BizException;

import java.util.Objects;

import static server.api.kiwes.global.security.exception.SecurityExceptionList.REQUIRED_LOGGED_IN;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class SecurityUtils {

    public static Member getLoggedInUser() {
        try {
            return
                    ((CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getMember();
        } catch (NullPointerException e) {
            throw new BizException(REQUIRED_LOGGED_IN);
        }
    }

}