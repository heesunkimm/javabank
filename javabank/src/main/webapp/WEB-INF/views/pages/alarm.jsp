<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="index_top.jsp"/>
	<!-- s: content -->
    <section id="alarms" class="content">
        <ul class="alarm_list">
        <c:if test="${empty alarmList}">
        <li class="nolist">알람이 없습니다.</li>
        </c:if>
        <c:if test="${not empty alarmList}">
        	<c:forEach var="alarm" items="${alarmList}">
            <li class="alarm_items">
                <div class="img_box">
                    <img src="../../images/icons/message.png">
                </div>
                <div class="txt_box">
                    <p>${alarm.alarmCate}</p>
                    <p>${alarm.alarmCont}</p>
                </div>
            </li>
        	</c:forEach>
    	</c:if>
        </ul>
    </section>
    <!-- e: content -->
<jsp:include page="index_bottom.jsp"/>