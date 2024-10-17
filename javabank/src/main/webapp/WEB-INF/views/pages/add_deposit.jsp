<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="add_deposit" class="content add_bank">
        <p>정기예금 개설</p>
        <div class="txt_box bg_yellow">
            <p>java<em>bank</em></p>
            <p>user님의 정기예금</p>
        </div>
        <form name="f" action="" method="post">
            <div>
                <p>통장 비밀번호 설정</p>
                <div class="passwd_box">
                    <label>
                        <input type="password" placeholder="비밀번호 4자리 입력" required>
                    </label>
                    <label>
                        <input type="password" placeholder="비밀번호확인 4자리 입력" required>
                    </label>
                </div>
            </div>

            <div>
                <p>예금금액 설정</p>
                <div class="">
                    <label>
                        <input type="text" name="" value="">
                    </label>
                </div>
            </div>

            <div>
                <p>기간 선택</p>
                <div class="">
                    <select name="">
                        <option value="6M">6개월 (기본 2.8%)</option>
                        <option value="12M">1년 (기본 3.0%)</option>
                    </select>
                </div>
            </div>

            <div class="btn_box">
                <button type="button">개설하기</button>
            </div>

        </form>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>