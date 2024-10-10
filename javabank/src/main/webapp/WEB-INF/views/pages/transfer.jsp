<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="transfer" class="content">
        <form name="f" action="/transfer" method="post">
            <div class="bank_info">
                <p>송금할 계좌를 입력해주세요.</p>
                <div class="transfer_box">
	                <div>
		                <select class="bank">
		                    <option value="신한">신한</option>
		                    <option value="국민">국민</option>
		                    <option value="기업">기업</option>
		                    <option value="농협">농협</option>
		                    <option value="우리">우리</option>
		                    <option value="하나">하나</option>
		                    <option value="카카오뱅크">카카오뱅크</option>
		                    <option value="케이뱅크">케이뱅크</option>
		                    <option value="토스뱅크">토스뱅크</option>
		                    <option value="새마을">새마을</option>
		                </select>
		                <label>
		                    <input type="text" name="depositAccount" value="" placeholder="계좌번호 직접입력" required>
		                </label>
	                </div>
	                <label>
	                    <input type="text" name="accountMemo" value="" placeholder="메모입력(최대 6글자)" required>
	                </label>
                </div>
                <button class="bg_yellow" type="button">다음</button>
            </div>
        </form>

        <div class="bank_list">
            <p>내계좌</p>
            <ul class="my_list account_list">
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="../../images/icons/passbook.png">
                        </div>
                        <div class="txt_box">
                            <p class="account_name">계좌명</p>
                            <p class="deposit_account"><span>은행명</span>0000-0000-0000-0000</p>
                        </div>
                    </a>
                </li>
            </ul>
            <p>최근 이체</p>
            <ul class="recently_list account_list">
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="../../images/icons/passbook.png">
                        </div>
                        <div class="txt_box">
                            <p class="account_name">홍길동</p>
                            <p class="deposit_account"><span>은행명</span>0000-0000-0000-0000</p>
                        </div>
                    </a>
                </li>
            </ul>
        </div>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>