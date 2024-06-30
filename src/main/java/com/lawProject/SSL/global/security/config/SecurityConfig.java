package com.lawProject.SSL.global.security.config;

import com.lawProject.SSL.global.security.oauth.handler.OAuthLoginFailureHandler;
import com.lawProject.SSL.global.security.oauth.handler.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;

    // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/**").permitAll()
                )

                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        oauth
                                .successHandler(oAuthLoginSuccessHandler) // 로그인 성공 시 핸들러
                                .failureHandler(oAuthLoginFailureHandler) // 로그인 실패 시 핸들러
                );

        return httpSecurity.build();
    }
}



//    private final OauthLoginFailureHandler

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll() // 정적 자원 설정
//                        .requestMatchers("/","/home", "/verify-email", "/verification-code").permitAll()
//                        .requestMatchers("/join/**", "/login").permitAll()
//                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                        .anyRequest().authenticated())
////                .formLogin(form -> form
////                        .loginPage("/login")
////                        .defaultSuccessUrl("/"))
////                .logout((logout) -> logout
////                        .logoutSuccessUrl("/login")
////                        .invalidateHttpSession(true)) // 로그아웃 이후 전체 세션 삭제 여부
////                .sessionManagement(session -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT에 맞게 세션 정책 설정
//                .addFilterAfter(jsonUsernamePasswordLoginFilter(), LogoutFilter.class)
//                .addFilterBefore(jwtAuthenticationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class)
//        ;
//        return http.build();
//    }

