<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="add_deposit" class="content add_bank">
        <p>정기예금 개설</p>
        <div class="txt_box bg_green">
            <p>java<em>bank</em></p>
            <p>${loginUser}님의 정기예금</p>
        </div>
        <form name="addDepositForm" action="add_deposit" method="post">
        <input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div>
                <p>통장 비밀번호 설정</p>
                <div class="passwd_box">
                    <label>
                        <input type="password" name="productPw" placeholder="비밀번호 4자리 입력" maxlength="4" required>
                    </label>
                    <label>
                        <input type="password" name="productPw02" placeholder="비밀번호확인 4자리 입력" maxlength="4" required>
                    </label>
                    <span class="passwd_noti"></span>
                </div>
            </div>
            <div>
                <p>예금 금액 설정</p>
                <div>
                    <label>
                        <input type="text" name="payment" placeholder="최대한도 5,000만원" value="">
                    </label>
                </div>
            </div>
            <div>
                <p>기간 선택</p>
                <div>
				    <select name="interestRate">
					    <option value="0.028">6개월 (기본 2.8%)</option>
					    <option value="0.03">1년 (기본 3.0%)</option>
					</select>
				</div>
            </div>
            <div>
                <p>출금 계좌번호</p>
                <div>
                    <select name="depositAccount">
                    <c:forEach var="account" items="${accountList}">
                        <option value="${account.depositAccount}" data-balance="${account.balance}">
                        	${account.depositAccount}
                        	<c:choose>
                        		<c:when test="${account.mainAccount eq 'Y'}">[주거래 계좌]</c:when>
                        	</c:choose>
                        </option>
                    </c:forEach>
                    </select>
                </div>
            </div>
            <p class="deposit_noti">* 만기 후 선택한 계좌로 입금됩니다.</p>

            <div class="btn_box">
                <button class="addBtn" type="button">개설하기</button>
            </div>
        </form>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>
<script>
	$(document).ready(function() {
		// 숫자 정규표현식 변환
	    $("input[type='password'], input[type='text']").keyup(function() {
	        let value = $(this).val().replace(/[^0-9]/g, '');

	        $(this).val(value);
	    });
		
	 	// 비밀번호 일치여부 확인
		$("input[name='productPw'], input[name='productPw02']").on("keyup", function() {
			let accountPw = $("input[name='productPw']").val();
			let accountPw2 = $("input[name='productPw02']").val();
			
			if(accountPw != '' && accountPw2 != '' && accountPw === accountPw2) {
				$(".passwd_noti").text("일치").css("color", "#359176");
			}else if (accountPw != accountPw2) {
				$(".passwd_noti").text("불일치").css("color", "#fc5c50");
			}else {
				$(".passwd_noti").text("");
			}
		})
		
		// 유효성 체크
	    $('.addBtn').on("click", function() {
	    	let payment = $("input[name='payment']").val();
	    	let accountPw = $("input[name='productPw']").val();
			let accountPw2 = $("input[name='productPw02']").val();
			let accountBalance = $("select[name='depositAccount'] option:selected").data("balance");
			let formattedBalance = accountBalance.toLocaleString();
			
			if(accountPw === '' || accountPw2 ==='') {
				return alert("개설할 정기예금의 비밀번호를 입력해주세요.");
			}else if (payment === '') {
				return alert("예금 금액을 설정해주세요.");
			}else if(accountPw != accountPw2) {
				return alert("비밀번호가 서로 일치하지 않습니다.");
			}else if(accountPw.length < 4 || accountPw2.length < 4) {
				return alert("비밀번호는 4자리의 숫자로 설정해주세요.");
			}else if(payment === 0 || payment < 0 || payment < 500000) {
				return alert("예금가능한 금액은 최소 50만원 이상 입니다.");
			}else if(payment > 50000000) {
				return alert("예금 금액은 최대 5,000만원입니다.");
			}else if(payment > accountBalance) {
				return alert ("출금계좌의 잔액이 부족합니다.\n출금계좌 잔액: " + formattedBalance + "원");
			}
			
			$("form[name='addDepositForm']").submit();
	    });
	 	
		// msg가 존재하는 경우 alert
		let msg = "${msg}";
		console.log(msg);
		if (msg && msg.trim() !== "") {
		    alert(msg);
		}
	});
</script>