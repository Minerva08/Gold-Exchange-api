# [금 거래 웹 서비스]
금방주식회사 백엔드 입사과제

</br>

## 목차
- [개요](#개요)
- [시나리오](#시나리오)
- [기술 스택](#기술-스택)
- [데이터베이스 모델링](#데이터베이스-모델링)
- [API 명세](#API-명세)
- [구현 기능](#구현-기능)

<br/>

## 개요
- 금 거래 웹 서비스
- **Port** : 9999
- 개발기간 : 2024.09.4 ~ 2024.09.11
  <br/>

## 시나리오
1. 사용자 회원가입 후 인증서버 요청
2. 인증 이후 발급받은 accesstoken을 사용하여 자원 요청 API 호출
3. token 기간 만료 시 refreshToken을 통해 인증 서버 재발급 요청
   <br/>

## 기술 스택
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-17-blue) ![Static Badge](https://img.shields.io/badge/Springboot-3.2.8-green)<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/Mariadb-11.5.2-orange) <br/>
기타 : ![Static Badge](https://img.shields.io/badge/redis-red) <br/>
<br/>

## 데이터베이스 모델링
<img src="./docs/gold_api_db.png">

<br/>

## API 명세
| **분류** | **API 명칭** | **HTTP 메서드** | **엔드포인트** | **설명** |
| --- | --- | --- | --- | --- |
| **인증&인가** | 사용자 회원가입 | POST | /api/join | 사용자는 계정명과 비밀번호로 회원가입하며, 회원가입 요청은 gRPC를 통해 auth 서버로 전달됩니다. 이후 회원 정보는 auth 서버와 API 서버에 각각 저장됩니다.|
|  | 사용자 로그인 | POST | /api/login | gRPC 통신을 통해 auth 서버에서 사용자 정보를 인증한 후, Response Body에 access 토큰과 refresh 토큰이 반환됩니다. |
|  | 토큰 재발급 | GET | /api/reissue-jwt | gRPC 통신을 통해 auth 서버에서 refresh토큰의 유효성 검증 후, Response Body에 재발급된 access 토큰과 refresh 토큰이 반환됩니다.  |
| **상품 주문** | 상품 주문(판매/구매) 요청 | POST | /api/orders/{orderType} | 사용자가 상품의 판매 또는 구매를 요청합니다. |
|  | 상품 거래 상태 변경 요청 | PATCH | /api/orders/modStatus | 기존 주문의 거래 상태를 변경합니다. |


<br/>

## 구현 기능
1. 회원가입 요청 시 인증 서버로 grpc 요청, 인증서버 DB 사용자 정보(전체:id, pw,주소, 생년월일) 저장, 자원서버 DB 사용자 일부(id, pw, 주소) 정보 저장
2. 로그인 요청 시 인증 서버로 grpc 요청, 인증서버에서 발급한 access token, refresh token 반환 
   1. access token : userid, address 
   2. refresh token : userid
3. 토큰 기간 만료 시 인증서버로 재발급 요청
   1. 로그인 시 발급한 refreshToken 과 Auth 서버의 redis에 저장된 RefreshToken의 일치 확인 후 access, refresh 토큰 재발급
   <br/>


