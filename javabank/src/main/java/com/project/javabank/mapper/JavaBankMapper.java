package com.project.javabank.mapper;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JavaBankMapper {

	@Autowired
	private SqlSession sqlSession;
	
	// 입출금계좌 생성
	public int addAccount(Map<String, Object> params) {
		return sqlSession.insert("addAccount", params);
	}
	
	// 계좌번호 중복확인
	public int checkAccount(String depositAccount) {
		return sqlSession.selectOne("checkAccount", depositAccount);
	}
}
