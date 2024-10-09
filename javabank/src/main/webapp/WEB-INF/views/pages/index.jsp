<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="main" class="content">
        <div class="account_box">
            <p class="account_tit">내 계좌</p>
            <ul>
            	<c:if test="${empty accountList}">
                <li class="nolist">
                    <a href="add_account">
                        <p>등록된 계좌가 없습니다. 계좌를 추가해주세요.</p>
                        <div class="img_box">
                            <img src="../../images/icons/account.png">
                        </div>
                    </a>
                </li>
            	</c:if>
            	<c:forEach var="account" items="${accountList}">
                <li class="account_item bg_yellow">
                    <div class="txt_box">
                        <p class="account_name">${account.category}</p>
                        <p class="account_number">${account.depositAccount}</p>
                        <p class="account_amount">${account.accountBalance}원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
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
	                    <p class="account_amount">${deposit.accountBalance}원</p>
	                </div>
	                <div class="btn_box">
	                    <button type="button">조회</button>
	                    <button type="button">이체</button>
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
                        <p class="account_amount">${savingAccount.accountBalance}원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                    </div>
                </li>
			    </c:forEach>
            </ul>
        </div>
		</c:if>

        <div class="account_box">
            <p class="account_tit">상품</p>
            <ul class="product_list">
            	<li>
                    <a href="add_account">
                        <div class="img_box">
                            <img src="../../images/icons/account.png">
                        </div>
                        <span>입출금통장</span>
                        <p class="txt_noti">이자 매월 0.1%</p>
                    </a>
                </li>
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="../../images/icons/account.png">
                        </div>
                        <span>정기예금</span>
                        <p class="txt_noti">6개월 기준 기본 2.8%</p>
                    </a>
                </li>
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="../../images/icons/account.png">
                        </div>
                        <span>정기적금</span>
                        <p class="txt_noti">6개월 기준 기본 3.3%</p>
                    </a>
                </li>
            </ul>
        </div>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>