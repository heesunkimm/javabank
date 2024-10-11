<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="my_account" class="content">
        <p>내 계좌 모아보기</p>
        <div class="account_box">
            <p class="account_tit">입출금</p>
            <ul>
            	<c:if test="${empty accountList}">
                <li class="nolist">
                    <p>나의 입출금 계좌가 없습니다.</p>
                    <div class="img_box">
                        <img src="../../images/icons/account.png">
                    </div>
                </li>
                </c:if>
                <c:forEach var="account" items="${accountList}">
                <li class="account_item bg_yellow">
                    <!-- 주거래: star.png / 주거래X: star_line.png -->
                    <div class="img_box">
                        <img src="../../images/icons/star_line.png">
                    </div>
                    <div class="txt_box">
                        <p class="account_name">${account.category}</p>
                        <p class="account_number">${account.depositAccount}</p>
                        <p class="account_amount">
                        	<fmt:formatNumber value="${account.balance}" pattern="#,###"/>원
                       	</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                        <button type="button">계좌삭제</button>
                    </div>
                </li>
                </c:forEach>
            </ul>
        </div>

        <c:if test="${not empty depositList}">
        <div class="account_box">
            <p class="account_tit">예금</p>
            <ul>
			    <c:forEach var="deposit" items="${depositList}">
	            <li class="account_item bg_green">
	                <div class="txt_box">
	                    <p class="account_name">${deposit.category}</p>
	                    <p class="account_number">${deposit.productAccount}</p>
	                    <p class="account_amount">
	                    	<fmt:formatNumber value="${deposit.balance}" pattern="#,###"/>원
                    	</p>
	                </div>
	                <div class="btn_box">
	                    <button type="button">조회</button>
	                    <button type="button">이체</button>
	                    <button type="button">계좌삭제</button>
	                </div>
	            </li>
			    </c:forEach>
            </ul>
        </div>
		</c:if>

        <c:if test="${not empty savingAccountList}">
        <div class="account_box">
            <p class="account_tit">적금</p>
            <ul>
			    <c:forEach var="savingAccount" items="${savingAccountList}">
                <li class="account_item bg_green">
                    <div class="txt_box">
                        <p class="account_name">${savingAccount.category}</p>
                        <p class="account_number">${savingAccount.productAccount}</p>
                        <p class="account_amount">
                        	<fmt:formatNumber value="${savingAccount.balance}" pattern="#,###"/>원
                        </p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                        <button type="button">계좌삭제</button>
                    </div>
                </li>
			    </c:forEach>
            </ul>
        </div>
		</c:if>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>