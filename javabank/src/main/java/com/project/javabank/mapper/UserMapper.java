package com.project.javabank.mapper;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.javabank.dto.UserDTO;

@Service
public class UserMapper {

	@Autowired
	private SqlSession sqlSession;
	
	// 로그인
//	public UserDTO findUserByLogin(String userId) {
//		return sqlSession.selectOne("findUserByLogin", userId);
//	}
	// 아이디 중복체크
	public int idCheck(String userId) {
		return sqlSession.selectOne("idCheck", userId);
	}
}
