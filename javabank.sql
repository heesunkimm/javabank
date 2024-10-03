-- 테이블 삭제
DROP TABLE javabankUser CASCADE CONSTRAINTS;
DROP TABLE javabankAccount CASCADE CONSTRAINTS;
DROP TABLE javabankProduct CASCADE CONSTRAINTS;
DROP TABLE javabankAlarm CASCADE CONSTRAINTS;

-- 시퀀스 삭제
DROP SEQUENCE seqUserNum;
DROP SEQUENCE seqAccountNum;
DROP SEQUENCE seqProductNum;
DROP SEQUENCE seqAlarmNum;

-- 시퀀스 생성
create sequence seqUserNum increment by 1 start with 1;
create sequence seqAccountNum increment by 1 start with 1;
create sequence seqProductNum increment by 1 start with 1;
create sequence seqAlarmNum increment by 1 start with 1;

-- 유저 테이블 생성
CREATE TABLE javabankUser (
    userId varchar(60) primary key,
    userPw varchar(60) not null,
    userName varchar(60) not null,
    userBrith Date not null,
    userEmail varchar(60) not null,
    userTel varchar(60) not null,
    userRegDate Date Default sysdate
);

insert into javabankUser values('user001','user001','홍길동','1995-09-25','aaa01@naver.com','010-1234-5678',default);
select * from javabankUser;
commit;

-- 입출금계좌 테이블 생성
CREATE TABLE javabankAccount (
    depositAccount varchar(60) primary key,
    depositPw Number not null,
    userId varchar(60) not null,
    category varchar(60) not null,
    updateDate Date,
    accountType varchar(60) not null,
    accountMemo varchar(60),
    deltaAmount Number,
    accountBalance Number not null,
    accountRegDate Date not null,
    expiryDate Date not null,
    interestRate Number not null,
    interestAmount Number,
    mainAccount varchar(10),
    CONSTRAINT fk_account_user FOREIGN KEY (userId) REFERENCES javabankUser(userId)
);

-- 상품계좌 테이블 생성
CREATE TABLE javabankProduct (
    productAccount varchar(60) primary key,
    productPw Number not null,
    userId varchar(60) not null,
    category varchar(60) not null,
    productType varchar(60) not null,
    updateDate Date,
    productMemo varchar(60),
    deltaAmount Number,
    accountBalance Number not null,
    autoTransferDate Date not null,
    monthlyPayment Number not null,
    accountRegDate Date not null,
    expiryDate Date not null,
    interestRate Number not null,
    interestAmount Number,
    depositAccount varchar(60) not null,
    CONSTRAINT fk_product_user FOREIGN KEY (userId) REFERENCES javabankUser(userId),
    CONSTRAINT fk_product_deposit_account FOREIGN KEY (depositAccount) REFERENCES javabankAccount(depositAccount)
);

-- 알람 테이블 생성
CREATE TABLE javabankAlarm (
    alarmNum Number primary key,
    userId varchar(60) not null,
    alarmIsRead varchar(10),
    alarmCate varchar(60),
    alarmCont varchar(200),
    alarmRegDate Date,
    CONSTRAINT fk_alarm_user FOREIGN KEY (userId) REFERENCES javabankUser(userId)
);

commit;

