package com.project.javabank.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.javabank.dto.AccountDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.UserDTO;

@Service
public class JavaBankMapper {

	@Autowired
	private SqlSession sqlSession;
	
	// 로그인 유저의 입출금 계좌리스트
	public List<AccountDTO> loginUserAccount(String userId) {
		return sqlSession.selectList("loginUserAccount", userId);
	}
	// 로그인 유저의 주거래계좌 정보조회
	public AccountDTO loginUserMainAccountInfo(String userId) {
		return sqlSession.selectOne("loginUserMainAccountInfo", userId);
	}
	// 로그인 유저의 예적금 계좌리스트
	public List<ProductDTO> loginUserProduct(String userId) {
		return sqlSession.selectList("loginUserProduct", userId);
	}
	// 로그인 유저이름 가져오기
	public UserDTO loginUserById(String userId) {
	    return sqlSession.selectOne("loginUserById", userId);
	}
	// 계좌번호 중복확인
	public int checkAccount(String depositAccount) {
		return sqlSession.selectOne("checkAccount", depositAccount);
	}
	// 로그인 유저의 주거래계좌 존재유무 확인
	public int loginUserMainAccount(Map<String, Object> params) {
	    return sqlSession.selectOne("loginUserMainAccount", params);
	}
	// 입출금계좌 생성
	public int addAccount(Map<String, Object> params) {
		return sqlSession.insert("addAccount", params);
	}
	// 입출금계좌 조회
	public List<AccountDTO> accountDetails(Map<String, Object> params) {
		return sqlSession.selectList("accountDetails", params);
	}
}
