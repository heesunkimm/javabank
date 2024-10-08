package com.project.javabank.mapper;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.javabank.dto.UserDTO;

@Service
public class UserMapper {

	@Autowired
	private SqlSession sqlSession;

	// 회원가입
	public int joinUser(Map<String, Object> params) {
		return sqlSession.insert("joinUser", params);
	}
	
	public UserDTO findUserByEmail(String userEmail) {
		return sqlSession.selectOne("findUserByEmail", userEmail);
	}
	// 이메일 중복체크
	public int checkEmail(String userEmail) {
		return sqlSession.selectOne("checkEmail", userEmail);
	}
	// 아이디 중복체크
	public int idCheck(String userId) {
		return sqlSession.selectOne("idCheck", userId);
	}
}
