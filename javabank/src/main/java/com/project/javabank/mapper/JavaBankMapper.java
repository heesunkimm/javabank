package com.project.javabank.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.javabank.dto.AccountDTO;
import com.project.javabank.dto.DtransactionDTO;
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
	// 로그인 유저의 계좌 정보조회
	public AccountDTO loginUserAccountInfo(Map<String, Object> params) {
		return sqlSession.selectOne("loginUserAccountInfo", params);
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
	public int loginUserMainAccountCheck(Map<String, Object> params) {
	    return sqlSession.selectOne("loginUserMainAccount", params);
	}
	// 입출금계좌 생성 및 최초 거래내역 0원으로 생성
	@Transactional
	public void addAccount(Map<String, Object> params) {
		// 계좌추가
		sqlSession.insert("addAccount", params);
		// 첫거 내역 추가
		sqlSession.insert("addAccountDetails", params);
	}
	// 입출금계좌 조회
	public List<AccountDTO> accountList(Map<String, Object> params) {
		return sqlSession.selectList("accountList", params);
	}
	// 로그인 유저의 최근 입출금거래 계좌리스트
//	public List<DtransactionDTO> recentlyAccountList(String depositAccount) {
//		return sqlSession.selectList("recentlyAccountList", depositAccount);
//	}
	// db에 존재하는 계좌 체크
	public AccountDTO accountCheck(String transferAccount) {
	    return sqlSession.selectOne("accountCheck", transferAccount);
	}
	// 계좌잔액 체크
	public int balanceCheck(String depositAccount) {
		return sqlSession.selectOne("balanceCheck", depositAccount);
	}
	// 비밀번호 일치여부 체크
	public String accountPwCheck(Map<String, Object> params) {
		return sqlSession.selectOne("accountPwCheck", params);
	}
	// 송금하기
	public int insertMoney(Map<String,Object> params) {
		return sqlSession.insert("insertMoney", params);
	}
	// 수신 계좌 userId 조회
	public String getUserIdByAccount(String depositAccount) {
		return sqlSession.selectOne("getUserIdByAccount", depositAccount);
	}
}
