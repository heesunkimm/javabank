<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="add_deposit" class="content add_bank">
        <p>정기예금 개설</p>
        <div class="txt_box bg_yellow">
            <p>java<em>bank</em></p>
            <p>${loginUser}님의 정기예금</p>
        </div>
        <form name="f" action="" method="post">
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
                <p>예금금액 설정</p>
                <div>
                    <label>
                        <input type="text" name="payment" value="">
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
                        <option value=""></option>
                    </select>
                </div>
	            <p>* 만기 후 선택한 계좌로 입금됩니다.</p>
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
	        /* let intAccountLimit = $("input[name='transactionLimit']").val();
	        let accountLimit = parseInt(intAccountLimit.replace(/,/g, '')); // 쉼표 제거 */
	        
	        
	    });
	});
</script>