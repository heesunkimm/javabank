<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="transfer_money" class="content">
        <form name="insertMoneyForm" action="/insertMoney" method="post">
        <input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="depositAccount" value="${depositAccount}">
        <input type="hidden" name="transferAccount" value="${transferAccount}">
        <input type="hidden" name="memo" value="${memo}">
            <div class="bank_info">
                <p>송금할 금액를 입력해주세요.</p>
                <label>
                    <input type="text" name="deltaAmount" value="" placeholder="금액입력" required>
                </label>
                <button class="bg_yellow nextBtn" type="button">다음</button>

            </div>
            <div class="select_box">
                <button type="button" name="deltaAmount" value="10000">1만원</button>
                <button type="button" name="deltaAmount" value="50000">5만원</button>
                <button type="button" name="deltaAmount"  value="100000">10만원</button>
                <button class="balanceAll" type="button" name="deltaAmount" value="${balance}">전액</button>
            </div>
        </form>
    </section>
    <div class="popup_box pwbox" style="display: none;">
        <p class="popup_title">비밀번호룰 입력해주세요.</p>
        <label>
            <input type="password" name="depositPw" placeholder="비밀번호 4자리 입력" maxlength="4" required>
        </label>
        <div class="pbtn_box">
            <button type="button" class="submitBtn">확인</button>
            <button type="button" class="closeBtn">취소</button>
        </div>
    </div>
    <div class="dimm"></div>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>
<script>
	$(document).ready(function() {
		let csrfToken = $(".csrfToken").val();
		let balanceAll = $(".balanceAll").val();
		let depositPw = $(".pwbox input[name='depositPw']");
		let depositAccount = $("input[name='depositAccount']").val();
		
		// 숫자 정규표현식 변환
		$("input[name='deltaAmount']").keyup(function() {
	        let value = $(this).val().replace(/[^0-9]/g, '');
	        $(this).val(value);
	    });
		
		// 선택한 버튼의 금액 input에 담기
		$(".select_box button").on('click', function() {
			let moneyVal = $(this).val();
			$("input[name='deltaAmount']").val(moneyVal);
		})
		
		// 입력한 금액과 내 계좌잔액 비교
		$(".nextBtn").on('click', function() {
			let balance = parseFloat($(".balanceAll").val());
			let deltaAmount = parseFloat($("input[name='deltaAmount']").val());
			
			// 숫자 정규표현식 변환
			$("input[name='deltaAmount']").keyup(function() {
			    let value = $(this).val().replace(/[^0-9]/g, '');
			    $(this).val(value);
			});
			// 유효성 체크
		    if (isNaN(deltaAmount) || deltaAmount === '') {
		        return alert("송금할 금액을 입력해주세요.");
		    } else if (deltaAmount <= 0) {
		        return alert("0원 이하의 금액은 송금이 불가합니다.");
		    } else if (deltaAmount > balance) {
		        return alert("계좌의 잔액을 초과하는 금액은 송금할 수 없습니다.");
		    } else {
		        $(".pwbox, .dimm").addClass('s_active');
		    }
		});
		
		// 팝업창 확인버튼 이벤트
		$(".pwbox .submitBtn").on('click', function() {
			// 비밀번호 일치여부 확인
			$.ajax ({
				url: "accountPwCheck.ajax",
		        type: "POST",
		        headers: {"X-CSRF-TOKEN": csrfToken}, 
				data: {
					depositPw: depositPw.val(),
					depositAccount: depositAccount
					},
				success: function(res) {
					if(res === "OK") {
						alert("비밀번호가 일치합니다.");
						$("form[name='insertMoneyForm']").submit();
					}else {
						alert("비밀번호가 일치하지 않습니다.");
						$("input[name='depositPw']").val("").focus();
					}
				},
				error: function(err) {
					console.log(err);
		            alert("서버 요청 실패.\n네트워크 상태를 확인해주세요.");
				}
			});
			
		});
		// 팝업창 닫기 이벤트
		$(".pwbox .closeBtn").on('click', function() {
			$("input[name='depositPw']").val("");
			$(".pwbox, .dimm").removeClass('s_active');
		});
		
		// msg가 존재하는 경우 alert
		let msg = "${msg}";
		console.log(msg);
		if (msg && msg.trim() !== "") {
		    alert(msg);
		}
	});
</script>