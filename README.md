# SPRING BOOT와 Spring Security를 이용하여 입출금 프로젝트 만들기

## 프로젝트 목적
 - 목표: Spring Boot와 Spring Security를 기반으로 로그인 및 회원 관리를 하고, 입출금과 예적금 상품 가입 및 정기적인 입금 내역과 알림을 트랜잭션과 스케줄링을 사용하여 구현하였습니다.
 - 주요 기능: 스프링 시큐리티를 이용한 로그인 회원관리, 입출금 트랙잭션 및 스케줄링 관리
 - 개발 기간: 2024.10.03 ~ 2024.10.25 (총 23일)
 - 개발 인원: 개인프로젝트

## 프로젝트 결과
 - Spring Security를 이용한 로그인 및 회원 관리 기능 구현

 - 입출금 트랜잭션 처리 및 알림 설정
   - 입출금 트랜잭션을 처리하고, 트랜잭션 발생 시 사용자에게 알림을 전송하도록 설정
     
 - 정기 적금 납입일 자동 처리
   - 정기 적금 상품의 납입일에 맞춰 지정된 계좌에서 자동으로 금액이 출금되도록 설정
   - 매월 정해진 날짜에 스케줄링을 통해 납입금이 자동으로 출금되며, 납입일에 따라 이체가 원활히 이루어지도록 처리
  
 - 상품 만기일에 이자 입금 자동화
   - 정해진 상품의 만기일에 맞춰 해당 상품의 이자를 계산하고, 개설 시 선택된 계좌로 자동 입금되도록 설정
   - 상품의 만기일에 따른 이자율을 계산하여, 사용자가 지정한 계좌로 정확하게 입금될 수 있도록 자동화된 트랜잭션 처리

## 파일구조
    javabank/
    ├── src/
    │ ├── main/
    │ │ ├── java/
    │ │ │ └── com/
    │ │ │  └── project/
    │ │ │   └── javabank/
    │ │ │    ├── config/
    │ │ │    ├── controller/
    │ │ │    ├── dto/
    │ │ │    └── mapper/
    │ │ ├── resources/
    │ │ │ ├── mybatis/
    │ │ │ ├── static/
    │ │ │ └── application.properties
    │ ├── webapp/
    │ │ └── WEB-INF/
    │ │   └── views/
    │ └── test/
    ├── .gitignore
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── README.md

## 기술 스택
 - Environment
   - Framework: Spring Boot
   - Database: Oracle
   - Version Control: Git, GitHub

 - Config
   - Security: Spring Security
 
 - Development
   - Frontend: jQuery, JSP, AJAX
   - Backend: Spring Boot, MyBatis
