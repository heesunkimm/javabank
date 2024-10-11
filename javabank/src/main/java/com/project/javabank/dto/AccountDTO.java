package com.project.javabank.dto;

public class AccountDTO {
	private String depositAccount;
	private String depositPw;
	private String userId;
	private String category;
	private String regDate;
	private String interestRate;
	private int transactionLimit;
	private String mainAccount;
	// 조인되는 테이블 컬럼 추가
	private String updateDate;
	private String type;
	private String memo;
	private int deltaAmount;
	private int balance;
	
	public String getDepositAccount() {
		return depositAccount;
	}
	public void setDepositAccount(String depositAccount) {
		this.depositAccount = depositAccount;
	}
	public String getDepositPw() {
		return depositPw;
	}
	public void setDepositPw(String depositPw) {
		this.depositPw = depositPw;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	public int getTransactionLimit() {
		return transactionLimit;
	}
	public void setTransactionLimit(int transactionLimit) {
		this.transactionLimit = transactionLimit;
	}
	public String getMainAccount() {
		return mainAccount;
	}
	public void setMainAccount(String mainAccount) {
		this.mainAccount = mainAccount;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getDeltaAmount() {
		return deltaAmount;
	}
	public void setDeltaAmount(int deltaAmount) {
		this.deltaAmount = deltaAmount;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
}
