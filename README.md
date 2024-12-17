# MySongSpace

MySongSpace는 사용자가 자신만의 음악이나 사운드를 업로드하고, 
특정 감정(mood)과 장르(genre)를 태그로 설정하여 다른 사용자와 공유할 수 있는 음악 플랫폼입니다. 
이 플랫폼은 사용자가 음악을 발견하고 감상하며, 댓글과 평가를 통해 소통할 수 있는 공간을 제공합니다.

## 📝 프로젝트 소개

MySongSpace는 창작자와 청취자를 연결하는 공간으로, 다음과 같은 목표를 가지고 개발되었습니다:
- 사용자가 간편하게 음악을 업로드하고 태그를 설정할 수 있는 환경 제공
- 음악의 분위기나 장르에 따라 검색하고 감상할 수 있는 사용자 경험 제공
- 음악과 관련된 소통을 위한 댓글 및 평가 기능 지원

## 📌 프로젝트에 들어있는 기능들

### 주요 기능
1. **트랙 업로드**
   - 음악 파일과 커버 이미지를 업로드
   - 제목, 설명, 감정(mood), 장르(genre) 태그 설정
   - 업로드된 트랙은 클라우드 스토리지에 저장

2. **댓글 및 평가 시스템**
   - 트랙에 대한 댓글 작성 및 대댓글(답글) 작성
   - 무한 대댓글 작성 가능
   - 첫 댓글에는 평가(별점) 기능 포함

3. **앨범 관리**
   - 사용자는 앨범을 생성할 수 있음
   - 사용자는 원하는 음악을 본인의 앨범에 담고 관리

4. **검색 및 필터링**
   - 감정 및 장르를 기반으로 트랙 검색
   - 트랙별 상세 정보 확인 가능

6. **카카오 소셜 로그인**
   - OAuth2, Spring Security, JWT를 활용한 카카오 소셜 로그인 구현
   - 사용자는 카카오 계정을 통해 간편하게 로그인 가능
  
##   ✅ 사용 기술 및 개발 환경

![image](https://github.com/user-attachments/assets/86b2bc5e-5c85-49d5-983d-52cf877494e7)



##   ✅ 서비스 아키텍쳐

![image](https://github.com/user-attachments/assets/716c3cff-e531-45df-af38-d4e43a358cfc)


##   ✅ ERD && Entity structure

![image](https://github.com/user-attachments/assets/e81641f8-3e42-41ae-b3c8-c7854afc7a27)


##   ✅ 프로젝트 하면서 겪었던 고민과 트레이드 오프


### 1. 안전한 파일 업로드에 대한 처리 chapter 1

https://hyeonseokdelvelop.tistory.com/42

---
### 2. 안전한 파일 업로드에 대한 처리 chapter 2

https://hyeonseokdelvelop.tistory.com/47

---
### 3. 좋아요 버튼의 숨겨진 딜레마 : 동시성 이슈

[[https://hyeonseokdelvelop.tistory.com/47](https://hyeonseokdelvelop.tistory.com/46)](https://hyeonseokdelvelop.tistory.com/46)

---

### 4. 좋아요 버튼의 숨겨진 딜레마: DB Lock 관련

[https://hyeonseokdelvelop.tistory.com/47](https://hyeonseokdelvelop.tistory.com/48)

---

### 5. Offset 페이징을 고려한 효율적인 대댓글 처리 방법

[https://hyeonseokdelvelop.tistory.com/47](https://hyeonseokdelvelop.tistory.com/44)

---

### 6. 검색 쿼리가 너무 느려ㅜㅜ

[[https://hyeonseokdelvelop.tistory.com/47](https://hyeonseokdelvelop.tistory.com/44)](https://hyeonseokdelvelop.tistory.com/49)

---

### 7. 동적 쿼리와 배치 사이즈 최적화로 1+N 문제 해결 및 성능 개선

https://hyeonseokdelvelop.tistory.com/45

---

