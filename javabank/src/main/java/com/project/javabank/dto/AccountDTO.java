package com.project.javabank.dto;

public class AccountDTO {
	private String depositAccount;
	private String depositPw;
	private String userId;
	private String category;
	private String updateDate;
	private String accountType;
	private String accountMemo;
	private int deltaAmount;
	private int accountBalance;
	private String accountRegDate;
	private String expiryDate;
	private int interestRate;
	private int interestAmount;
	private int accountLimit;
	private String mainAccount;
	
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
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountMemo() {
		return accountMemo;
	}
	public void setAccountMemo(String accountMemo) {
		this.accountMemo = accountMemo;
	}
	public int getDeltaAmount() {
		return deltaAmount;
	}
	public void setDeltaAmount(int deltaAmount) {
		this.deltaAmount = deltaAmount;
	}
	public int getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(int accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getAccountRegDate() {
		return accountRegDate;
	}
	public void setAccountRegDate(String accountRegDate) {
		this.accountRegDate = accountRegDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public int getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(int interestRate) {
		this.interestRate = interestRate;
	}
	public int getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(int interestAmount) {
		this.interestAmount = interestAmount;
	}
	public int getAccountLimit() {
		return accountLimit;
	}
	public void setAccountLimit(int accountLimit) {
		this.accountLimit = accountLimit;
	}
	public String getMainAccount() {
		return mainAccount;
	}
	public void setMainAccount(String mainAccount) {
		this.mainAccount = mainAccount;
	}
}
