package com.project.javabank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import jakarta.servlet.DispatcherType;

@Configuration // 설정파일임
@EnableWebSecurity // 설정파일을 필터에 등록
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http	
			.authorizeHttpRequests(request -> request
					.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
					.requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
					.requestMatchers("/", "/login", "/findUserById.ajax", "/findUserInfo.ajax", "updateUserPw.ajax", 
							"/join", "/mailCheck.ajax", "/sendEmail.ajax", "/codeCheck.ajax", "/idCheck.ajax", 
							"/accountCheck.ajax", "/account_delete.ajax ", "/conversionMainAccount.ajax").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin(form -> form					
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("userId")
						.passwordParameter("userPw")
						//.defaultSuccessUrl("/index?javabank", true) // 핸들러없을때 사용
						.successHandler(new CustomAuthenticationSuccessHandler()) // 커스토마이징 핸들러 사용
						.failureUrl("/login?loginError")
						.permitAll()
			)
			.logout(Customizer.withDefaults());
		
		return http.build();
	}
}