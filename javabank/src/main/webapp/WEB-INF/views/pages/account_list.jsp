<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="index_top.jsp"/>
<!-- s: content -->
    <section id="account_list" class="content">
    	
        <div class="info_box bg_yellow">
	            <div class="txt_box">
	                <p class="account_name">${accountInfo.category}</p>
	                <p class="account_number">${accountInfo.depositAccount}</p>
	                <p class="account_amount">
                      	<fmt:formatNumber value="${accountInfo.balance}" pattern="#,###"/>원
                    </p>
	            </div>
	            <div class="btn_box">
	                <button type="button">이체</button>
	            </div>
        </div>

        <div class="account_details">
            <div class="toolbar">
                <select name="period">
                    <option value="1M">1개월</option>
                    <option value="3M">3개월</option>
                    <option value="1Y">1년</option>
                </select>
                <select name="details">
                    <option value="all">전체</option>
                    <option value="deposit">입금</option>
                    <option value="payment">출금</option>
                </select>
            </div>
            <ul class="account_list">
            	<c:forEach var="account" items="${accountList}">
                <li class="account_items">
                    <div class="txt_box">
                        <p class="account_date font_gray"></p>
                        <p class="account_name">홍길동</p>
                        <p class="account_meno font_darkgray">입출금 메모</p>
                        <p></p>
                    </div>
                    <div class="account_info">
                        <p class="account_type font_blue">입금</p>
                        <p class="delta_amount font_blue">100,000원</p>
                        <p class="account_balance font_darkgray">100,000원</p>
                    </div>
                </li>
    			</c:forEach>
            </ul>
        </div>
    </section>
<!-- e: content -->
<jsp:include page="index_bottom.jsp"/>