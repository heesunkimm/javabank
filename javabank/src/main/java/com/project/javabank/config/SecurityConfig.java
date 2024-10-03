//package com.project.javabank.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
////@Configuration
//@EnableWebSecurity // 웹보완 활성화
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/", "/login", "/join").permitAll() // 로그인, 회원가입 페이지는 누구나 접근 가능
//                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
//            )
//            .formLogin(form -> form
//                .loginPage("/login") // 사용자 정의 로그인 페이지 경로
//                .defaultSuccessUrl("/pages/index") // 로그인 성공 시 리다이렉트 경로
//                .failureUrl("/login") // 로그인 실패 시 경로
//            )
//            .logout(logout -> logout
//                .logoutSuccessUrl("/login") // 로그아웃 성공 시 리다이렉트 경로
//            );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//
