<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="my_account" class="content">
    <input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
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
                    <div class="img_box">
	                    <a class="changeBtn" href="javascript:;" data-deposit="${account.depositAccount}" data-main="${account.mainAccount}">
               			<c:choose>
		                   	<c:when test="${account.mainAccount eq 'Y'}">
		                   		<img src="../../images/icons/star.png">
		                   	</c:when>
		                   	<c:otherwise>
		                   		<img src="../../images/icons/star_line.png">
		                   	</c:otherwise>
	                   	</c:choose>
	                    </a>
                    </div>
                    <div class="txt_box">
                        <p class="account_name">${account.category}</p>
                        <p class="account_number">${account.depositAccount}</p>
                        <p class="account_amount">
                        	<fmt:formatNumber value="${account.balance}" pattern="#,###"/>원
                       	</p>
                    </div>
                    <div class="btn_box">
                        <button type="button" onclick="location.href='account_list?depositAccount=${account.depositAccount}'">조회</button>
                        <button type="button" onclick="location.href='transfer?depositAccount=${account.depositAccount}'">이체</button>
                        <button type="button" onclick="location.href='account_delete?depositAccount=${account.depositAccount}'">계좌삭제</button>
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
	                    <button type="button" onclick="location.href='product_list?productAccount=${deposit.productAccount}'">조회</button>
	                    <button type="button" onclick="location.href='product_delete?productAccount=${deposit.productAccount}'">계좌삭제</button>
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
                <li class="account_item bg_blue">
                    <div class="txt_box">
                        <p class="account_name">${savingAccount.category}</p>
                        <p class="account_number">${savingAccount.productAccount}</p>
                        <p class="account_amount">
                        	<fmt:formatNumber value="${savingAccount.balance}" pattern="#,###"/>원
                        </p>
                    </div>
                    <div class="btn_box">
                        <button type="button" onclick="location.href='product_list?productAccount=${savingAccount.productAccount}'">조회</button>
                        <button type="button" onclick="location.href='product_delete?productAccount=${savingAccount.productAccount}'">계좌삭제</button>
                    </div>
                </li>
			    </c:forEach>
            </ul>
        </div>
		</c:if>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>
<script>
	$(document).ready(function() {
		let csrfToken = $(".csrfToken").val();
		
		$(".changeBtn").on("click", function() {
			let btn = $(this);
			let depositAccount = $(this).data("deposit");
			
			$.ajax({
				url: "conversionMainAccount.ajax",
				type: "post",
				headers: {"X-CSRF-TOKEN": csrfToken}, 
				data: {depositAccount: depositAccount},
				success: function(res){
					if (res === "OK") {
	                    alert("주거래 계좌가 변경되었습니다.");
	                    location.reload();
	                } else {
	                    alert("주거래 계좌 변경에 실패했습니다.");
	                }
				},error: function(err){
					console.log(err);
				}
			});
		});
		
		// msg가 존재하는 경우 alert
		let msg = "${msg}";
		console.log(msg);
		if (msg && msg.trim() !== "") {
		    alert(msg);
		}
	});
</script>