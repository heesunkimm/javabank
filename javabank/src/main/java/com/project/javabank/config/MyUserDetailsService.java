package com.project.javabank.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.MemberServiceMapper;



@Component
public class MyUserDetailsService implements UserDetailsService {
	
	private final MemberServiceMapper mapper;
	
	public MyUserDetailsService(MemberServiceMapper mapper) {
		this.mapper = mapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserDTO userDTO = new UserDTO();		
		userDTO = mapper.findUsernameById(userId);
        
        // 해당 ID가 DB에 있는 경우
        return User.builder()
                .username(userDTO.getUserId())
                .password(userDTO.getUserPw()) // 비밀번호도 필요시 가져와서 처리
                .roles(userDTO.getUserRoles()) // 권한 설정
                .build();
        
	}

}