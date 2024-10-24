<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="transfer" class="content">
        <form name="transferForm" action="/transfer_money" method="post">
        <input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="depositAccount" value="${depositAccount}">
            <div class="bank_info">
                <p style="width: 100%">송금할 계좌를 입력해주세요.</p>
                <div class="transfer_box">
	                <label>
	                    <input type="text" name="transferAccount" value="" placeholder="계좌번호 직접입력" maxlength="19" required>
	                </label>
	                <label>
	                    <input type="text" name="memo" value="" placeholder="메모입력(최대 6글자)" maxlength="6" required>
	                </label>
                </div>
                <button class="bg_yellow nextBtn" type="button">다음</button>
            </div>
        </form>

        <div class="bank_list">
            <p>내계좌</p>
            <ul class="my_list account_list">
            	<c:forEach var="account" items="${accountlist}">
	            	<c:if test="${account.depositAccount != depositAccount}">
	                <li>
	                    <a class="accountBtn" href="javascript:;" data-account="${account.depositAccount}">
	                        <div class="img_box">
	                            <img src="../../images/icons/passbook.png">
	                        </div>
	                        <div class="txt_box">
	                            <p class="account_name">${account.category}</p>
	                            <p class="deposit_account">${account.depositAccount}</p>
	                        </div>
	                    </a>
	                </li>
	            	</c:if>
            	</c:forEach>
            </ul>
            <p>최근 이체</p>
            <ul class="recently_list account_list">
            <c:if test="${empty recentlyList}">
            	<li class="nolist">최근 거래된 계좌가 없습니다.</li>
            </c:if>
            <c:forEach var="recentlyAccount" items="${recentlyList}">
                <li>
                    <a class="accountBtn" href="javascript:;" data-account="${recentlyAccount.transferAccount}">
                        <div class="img_box">
                            <img src="../../images/icons/passbook.png">
                        </div>
                        <div class="txt_box">
                            <p class="account_name">${recentlyAccount.category}</p>
                            <p class="deposit_account">${recentlyAccount.transferAccount}</p>
                        </div>
                    </a>
                </li>
            </c:forEach>
            </ul>
        </div>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>
<script>
	$(document).ready(function() {
	    let csrfToken = $(".csrfToken").val();
		
		// 숫자 정규표현식 변환, 4자리마다 하이픈 표시
		$("input[name='transferAccount']").keyup(function() {
		    let val = $(this).val().replace(/[^0-9]/g, '');
		    let formattedVal = val.replace(/(\d{4})(?=\d)/g, '$1-');
		    $(this).val(formattedVal);
		});
		// 내계좌 또는 최근 이체한 계좌 클릭시 input에 계좌번호 추가 
		$(".accountBtn").on('click', function() {
			let btnData = $(this).data("account");
			$("input[name='transferAccount']").val(btnData);
		});
		
		$(".nextBtn").on('click', function() {
			let accountVal = $("input[name='transferAccount']").val().trim();
			
			// 유효성 체크
			if(accountVal.length === '') {
				alert("계좌번호를 입력해주세요.");
				return $("input[name='transferAccount']").focus();
			}
			
			// db에 존재하는 계좌인지 확인
			$.ajax({
		        url: "/accountCheck.ajax",
		        type: "POST",
		        headers: {"X-CSRF-TOKEN": csrfToken}, 
		        data: {
		        	transferAccount: accountVal
		        	},
		        success: function(res) {
		        	if (res === "OK") {
		                alert("계좌가 확인되었습니다.");
		                $("form[name='transferForm']").submit();
		            } else {
		                alert("존재하지 않는 계좌입니다.");
		            }
		        },
		        error: function(err) {
		            console.error(err);
		            alert("서버 요청 실패.\n네트워크 상태를 확인해주세요.");
		        }
		    });
		});
	});
</script>