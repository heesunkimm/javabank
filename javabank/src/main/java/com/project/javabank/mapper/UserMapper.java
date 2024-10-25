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
	// 이메일로 등록된 이메일, 비밀번호, 아이디 찾기
	public UserDTO findUserByEmail(String userEmail) {
		return sqlSession.selectOne("findUserByEmail", userEmail);
	}
	// 비밀번호 변경
	public int updateUserPw(Map<String, Object> params) {
		return sqlSession.update("updateUserPw", params);
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
