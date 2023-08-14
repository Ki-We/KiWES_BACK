package server.api.kiwes.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.member.service.validate.MemberValidationService;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberValidationService validateService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(this.validateService.validateEmail(email));
    }
}
