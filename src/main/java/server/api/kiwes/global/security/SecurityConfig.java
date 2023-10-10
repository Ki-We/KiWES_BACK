package server.api.kiwes.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import server.api.kiwes.global.jwt.JwtAccessDeniedHandler;
import server.api.kiwes.global.jwt.JwtAuthenticationEntryPoint;
import server.api.kiwes.global.jwt.JwtSecurityConfig;
import server.api.kiwes.global.jwt.TokenProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resource/**", "/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()   //CSRF 보호 비활성화
                .formLogin().disable()  //폼 로그인 비활성화
                .httpBasic().disable()  // HTTP 기본 인증 비활성화
                .cors()
                .and()
                .exceptionHandling()    //예외 처리 설정
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //인증되지 않은 사용자가 보호된 리소스에 액세스 할 때 호출되는 JwtAuthenticationEntryPoint 설정
                .accessDeniedHandler(jwtAccessDeniedHandler)    //권한이 없는 사용자가 보호된 리소스에 액세스 할 때 호출되는 JwtAccessDeniedHandler 설정
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Spring Security에서 세션을 사용하지 않도록 설정
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/auth/refresh").permitAll()
                // 카카오
                .antMatchers(HttpMethod.POST, "/auth/kakao").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/members/additional-info").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/members/nickname/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/members/nickname/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/members/auth/refresh").permitAll()
                .antMatchers("/oauth/kakao/**").permitAll()
                .antMatchers("/oauth/**").permitAll()
                // 구글
//                .antMatchers(HttpMethod.POST, "/oauth/google").permitAll()
                .antMatchers("/login/oauth2/code/google").permitAll()
                .antMatchers("/oauth/google/**").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/userinfo.email").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/userinfo.profile").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/userinfo.profile").permitAll()
//                .antMatchers("openid").permitAll()
                //애플
                .antMatchers("/oauth/apple").permitAll()
                .antMatchers("/oauth/apple/**").permitAll()
                .antMatchers("/login/oauth/apple").permitAll()
                .antMatchers("/login/oauth/apple/**").permitAll()

                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                /* swagger v2 */
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                /* swagger v3 */
                "/v3/api-docs/**",
                "/swagger-ui/**");
    }

}
