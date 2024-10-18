package com.project.javabank.dto;

public class ProductDTO {
	private String productAccount;
	private String productPw;
	private String userId;
	private String category;
	private int autoTransferDate;
	private int monthlyPayment;
	private int payment;
	private String regDate;
	private String expiryDate;
	private double interestRate;
	private String depositAccount;
	private String productEnable;
	// 조인한 테이블 컬럼 추가
	private int balance;
	
	public String getProductAccount() {
		return productAccount;
	}
	public void setProductAccount(String productAccount) {
		this.productAccount = productAccount;
	}
	public String getProductPw() {
		return productPw;
	}
	public void setProductPw(String productPw) {
		this.productPw = productPw;
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
	public int getAutoTransferDate() {
		return autoTransferDate;
	}
	public void setAutoTransferDate(int autoTransferDate) {
		this.autoTransferDate = autoTransferDate;
	}
	public int getMonthlyPayment() {
		return monthlyPayment;
	}
	public void setMonthlyPayment(int monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public String getDepositAccount() {
		return depositAccount;
	}
	public void setDepositAccount(String depositAccount) {
		this.depositAccount = depositAccount;
	}
	public String getProductEnable() {
		return productEnable;
	}
	public void setProductEnable(String productEnable) {
		this.productEnable = productEnable;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
}
