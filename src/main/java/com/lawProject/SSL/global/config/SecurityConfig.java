package com.lawProject.SSL.global.config;

import com.lawProject.SSL.domain.token.repository.TokenRepository;
import com.lawProject.SSL.global.oauth.handler.OAuthLoginFailureHandler;
import com.lawProject.SSL.global.oauth.handler.OAuthLoginSuccessHandler;
import com.lawProject.SSL.global.oauth.handler.OAuthLogoutHandler;
import com.lawProject.SSL.global.oauth.service.CustomOAuth2UserService;
import com.lawProject.SSL.global.security.filter.JwtAuthenticationProcessingFilter;
import com.lawProject.SSL.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtService;
    private final TokenRepository tokenRepository;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuthLogoutHandler oAuthLogoutHandler;

    @Value("${security.origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity

                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // CORS 설정 추가

                .addFilterAfter(jwtAuthenticationProcessingFilter(), OAuth2LoginAuthenticationFilter.class)
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        oauth
                                .userInfoEndpoint(end -> end.userService(customOAuth2UserService))
                                .successHandler(oAuthLoginSuccessHandler) // 로그인 성공 시 핸들러
                                .failureHandler(oAuthLoginFailureHandler) // 로그인 실패 시 핸들러
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*", "/error", "/error/**", "/stomp/**", "/redis/**").permitAll() // 정적 자원 설정
                        .requestMatchers("/", "/join", "/login/**", "/info", "/info/**", "/healthcheck").permitAll()
                        .requestMatchers("/api/v1/inquery/my/**", "/api/v1/inquery").authenticated()
                        .requestMatchers("/api/v1/inquery/**", "/api/v1/lawyers", "/api/v1/lawyers/**").permitAll()
//                        .requestMatchers("/admin/**", "/api/v1/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .addLogoutHandler(oAuthLogoutHandler)
                                .logoutSuccessHandler(oAuthLogoutHandler)
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                );

        return httpSecurity.build();
    }
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter(){

        return new JwtAuthenticationProcessingFilter(jwtService, tokenRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList(allowedOrigins));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "Cache-Control", "Content-Type"));
            config.setMaxAge(3600L);

            /* 응답 헤더 설정 추가*/
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            config.setExposedHeaders(Collections.singletonList("Authorization-refresh"));
            config.setExposedHeaders(Collections.singletonList("Set-Cookie"));

            return config;
        };
    }
}

