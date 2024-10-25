package com.project.javabank.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.javabank.dto.AccountDTO;
import com.project.javabank.dto.AlarmDTO;
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
	// 로그인 유저의 예적금 정보조회
	public ProductDTO loginUserProductInfo(Map<String, Object> params) {
		return sqlSession.selectOne("loginUserProductInfo", params);
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
		// 첫거래 내역 추가
		sqlSession.insert("addAccountDetails", params);
		// 알람추가
		sqlSession.insert("newAlarm", params);
	}
	// 입출금계좌 조회
	public List<AccountDTO> accountList(Map<String, Object> params) {
		return sqlSession.selectList("accountList", params);
	}
	// 예적금계좌 조회
	public List<ProductDTO> productList(Map<String, Object> params) {
		return sqlSession.selectList("productList", params);
	}
	// 로그인 유저의 최근 입출금거래 계좌리스트
	public List<DtransactionDTO> recentlyAccountList(String depositAccount) {
		return sqlSession.selectList("recentlyAccountList", depositAccount);
	}
	// db에 존재하는 계좌 체크
	public AccountDTO accountCheck(String transferAccount) {
	    return sqlSession.selectOne("accountCheck", transferAccount);
	}
	// 계좌잔액 체크
	public DtransactionDTO balanceCheck(String depositAccount) {
		return sqlSession.selectOne("balanceCheck", depositAccount);
	}
	// 이체한도 확인
	public int transferLimit(String depositAccount) {
		return sqlSession.selectOne("transferLimit", depositAccount);
	}
	// 오늘 거래금액 조회 
	public int transferMoney(String depositAccount) {
		return sqlSession.selectOne("transferMoney", depositAccount);
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
	// 전체 입출금계좌 조회
	public List<AccountDTO> AllUserAccount() {
		return sqlSession.selectList("AllUserAccount");
	}
	@Transactional
	public void depositScheduled(Map<String, Object> params) {
		// 만기거래내역 추가
		sqlSession.insert("insertInterest", params);
		// 만기 예적금 상태 변경
		sqlSession.update("productStatusUpdate", params);
		// 정기예금 만기액, 이자 입금
		sqlSession.insert("insertMoney", params);
		// 알림추가
		sqlSession.insert("newAlarm", params);
	}
	// 입출금계좌 생성 및 최초 거래내역 0원으로 생성
	@Transactional
	public void addProduct(Map<String, Object> params) {
		// 상품 추가
		sqlSession.insert("addProduct", params);
		// 첫거래 내역 추가
		sqlSession.insert("addProductDetails", params);
		// 입출금계좌에서 예금금액 출금
		sqlSession.insert("insertMoney", params);
		// 알람추가
		sqlSession.insert("newAlarm", params);
	}
	// 알람리스트 조회
	public List<AlarmDTO> alarmList(String userId) {
		return sqlSession.selectList("alarmList", userId);
	}
	// 읽지않은 알람 갯수 체크
	public int alarmCheck(String userId) {
		return sqlSession.selectOne("alarmCheck", userId);
	}
	// 알림 읽음상태 변경
	public int alarmStatusUpdate(String userId) {
		return sqlSession.update("alarmStatusUpdate", userId);
	}
	@Transactional
	public void conversionMainAccount(String userId, String depositAccount) {
	    // 기존 주거래 계좌 상태 변경
	    sqlSession.update("updateMainAccount", userId);

	    // 선택된 계좌를 주거래 계좌로 변경
	    Map<String, Object> params = new HashMap<>();
	    params.put("userId", userId);
	    params.put("depositAccount", depositAccount);
	    sqlSession.update("updateNewMainAccount", params);
	}
	// 자동이체일체크
	public List<ProductDTO> autoTransferDateCheck(int autoTransferDate) {
		return sqlSession.selectList("autoTransferDateCheck", autoTransferDate);
	}
	@Transactional
	public void insertInterest(Map<String, Object> params) {
		// 만기거래내역 추가
		sqlSession.insert("insertInterest", params);
		// 만기 예적금 상태 변경
		sqlSession.update("productStatusUpdate", params);
		// 정기적금 월입금, 만기시 이자 입금
		sqlSession.insert("insertMoney", params);
		// 알람추가
		sqlSession.insert("newAlarm", params);
	}
	@Transactional
	public void savingAccountScheduled(Map<String, Object> params) {
		// 정기적금 입금
		sqlSession.insert("insertInterest", params);
		// 정기적금 출금
		sqlSession.insert("insertMoney", params);
		// 알람추가
		sqlSession.insert("newAlarm", params);
		
	}
	// 전체 예적금계좌 정보 조회
	public List<ProductDTO> AllUserProduct() {
		return sqlSession.selectList("AllUserProduct");
	}
	// 계좌삭제 전 메인계좌, 잔액 체크
	public AccountDTO AccountDelCheck(String depositAccount) {
		return sqlSession.selectOne("AccountDelCheck", depositAccount);
	}
	@Transactional
	public void accountDelete(String depositAccount) {
		// 입출금 거래내역 삭제
		sqlSession.delete("accountListDelete", depositAccount);
		// 입출금계좌 삭제
		sqlSession.delete("accountDelete", depositAccount);
	}
	// 예적금 상품 정보 확인
	public ProductDTO productCheck(String productAccount) {
		return sqlSession.selectOne("productCheck", productAccount);
	}
	@Transactional
	public void productDelete(Map<String, Object> params) {
		// 입출금계좌 거래내역 삭제
		sqlSession.delete("productListDelete", params);
		// 예적금계좌 삭제
		sqlSession.delete("productDelete", params);
		// 예적금 해지금액 입금
		sqlSession.insert("insertMoney", params);
	}
}
