<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="add_account" class="content add_bank">
        <p>입출금통장 개설</p>
        <div class="txt_box bg_yellow">
            <p>java<em>bank</em><br> ${loginUser}님의 통장</p>
        </div>
        <form name="addAccount" action="/add_account" method="post">
        	<input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div>
                <p>통장 비밀번호 설정</p>
                <div class="passwd_box">
                    <label>
                        <input type="password" name="depositPw" placeholder="비밀번호 4자리 숫자입력" maxlength="4" required>
                    </label>
                    <label>
                        <input type="password" name="depositPw02" placeholder="비밀번호확인 4자리 숫자입력" maxlength="4" required>
                    </label>
                    <span class="passwd_noti"></span>
                </div>
            </div>

            <div>
                <p>금융거래 1일 이체한도</p>
                <div>
                    <label>
                        <input type="text" name="transactionLimit" placeholder="최대 100만원까지 설정가능합니다." value="" required>
                    </label>
                </div>
            </div>

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
	    $("input[type='password']").keyup(function() {
	        let value = $(this).val().replace(/[^0-9]/g, '');
	        $(this).val(value);
	    });
		
	 	// 비밀번호 일치여부 확인
		$("input[name='depositPw'], input[name='depositPw02']").on("keyup", function() {
			let accountPw = $("input[name='depositPw']").val();
			let accountPw2 = $("input[name='depositPw02']").val();
			
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
	        let intAccountLimit = $("input[name='transactionLimit']").val();
	        let accountLimit = parseInt(intAccountLimit.replace(/,/g, '')); // 쉼표 제거
			let accountPw = $("input[name='depositPw']").val();
			let accountPw2 = $("input[name='depositPw02']").val();
			
	        // 비밀번호 체크
	        if (accountPw === "" || accountPw2 === "") {
	            return alert("개설할 입출금계좌의 비밀번호를 입력해주세요.");
	        }else if(accountLimit === "") {
	        	return alert("예금 금액을 설정해주세요.");
	        }else if(accountPw != accountPw2) {
				return alert("비밀번호가 서로 일치하지 않습니다.");
			}else if(accountPw.length < 4 || accountPw2.length < 4) {
				return alert("비밀번호는 4자리의 숫자로 설정해주세요.");
			}else if (accountLimit > 1000000) {
	            return alert("일일 이체한도는 최대 100만원까지 설정할 수 있습니다.");
	        }
	        
	        $("form[name='addAccount']").submit();
	    });
	});
</script>