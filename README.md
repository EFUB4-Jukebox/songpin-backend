<div align="center">

# 🎵songpin-backend🎵
EFUB 4기 SWS 3팀 "SongPin" 프로젝트 백엔드 레포지토리입니다.

**나의 음악지도, SongPin**

<a href="https://hits.seeyoufarm.com"><img src="https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https://github.com/EFUB4-Jukebox/songpin-backend"/></a>

<a href="https://www.instagram.com/songpin_official/">
<img alt="Instagram" src="https://img.shields.io/badge/-Instagram-white?logo=Instagram&logoColor=d42121">
</a>
</div>

## ⏰ 개발 기간

- 24.07.01 - 24.08.09

## 🌟 팀원 소개

| 이서현 | 정유정 | 황채린 | 문하영 |
|:-------------------------:|:----------------------:|:-----------------------:|:---------------------:|
| <img src="https://avatars.githubusercontent.com/u/32611398?v=4" width="125" height="125"/> | <img src="https://avatars.githubusercontent.com/u/141399892?v=4" width="125" height="125"/> | <img src="https://avatars.githubusercontent.com/u/87927105?v=4" width="125" height="125"/> | <img src="https://avatars.githubusercontent.com/u/124586544?v=4" width="125" height="125"/> |
| [@seohyun-lee](https://github.com/seohyun-lee) | [@jud1thDev](https://github.com/jud1thDev) | [@crHwang0822](https://github.com/crHwang0822) | [@gkdudans](https://github.com/gkdudans) |
| CI/CD 환경 구축 및 배포<br>Place, Follow, Alarm API 개발<br>통계 페이지, 지도 핀 정보 제공 | 스포티파이 API 연결<br>Pin, Song, Genre API 개발 | Member API 개발<br>Spring security, jwt, Redis | Playlist, Bookmark API 개발<br>Home 페이지 |

## 🛠️ 기술 스택

**Environment**

<img src="https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellij idea&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

**Language**

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

**Development**

<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/REDIS-FF4438?style=for-the-badge&logo=redis&logoColor=white">
 <img src="https://img.shields.io/badge/Spotify API-1DB954?style=for-the-badge&logo=spotify&logoColor=white">


**Deploy**

<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=github actions&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

## 📝 규칙

#### 커밋 컨벤션
  - "태그: 한글 커밋 메시지" 형식으로 작성
  - 컨벤션 예시
    - feat: 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
    - fix: 버그 수정
    - docs: 문서 수정
    - style: 코드 포맷팅, 오타 수정, 주석 수정 및 삭제 등
    - refactor: 코드 리팩터링
    - chore: 빌드 및 패키지 수정 및 삭제
    - merge: 브랜치를 머지
    - ci: CI 관련 설정 수정
    - test: 테스트 코드 추가/수정
    - release: 버전 릴리즈

<br>

#### PR 템플릿
  ```
  # 구현 기능
    - 구현한 기능을 요약하여 정리합니다.

  # 구현 상태 (선택)
    - img, gif, video...
    - 혹은 내용 정리

  # Resolve
    - 이슈 태그(ex: #7)
  ```

  - PR 체크 리스트
    -  PR 제목 형식 : `[Type] PR 제목`
        - ex. `[Chore] 프로젝트 구조 설정`
        - 타입은 대문자로
    -  label 설정
    -  Code Review 요청 / 작업자 Assign
    -  PR 확인한 사람은 확인 코멘트 달기. 작성자 외 2명 확인 후 마지막 사람이 merge
      
<br>

#### issue 규칙
  - 각 기능에 맞는 이슈 템플릿 작성 (작업 및 변경사항 확인용)
  - to-do에 구현해야할 기능을 작성하고, 구현이 끝나면 체크표시

<br>

#### branch 규칙
- 브랜치 네이밍 규칙: `feat/{도메인혹은큰기능}` ex) `feat/place`
- `feat -> develop -> deploy -> main` 순으로 merge
- `feat` : 각 기능을 개발하는 브랜치
- `develop` : 각 기능의 개발을 완료하고 테스트 완료 후 병합하는 브랜치
- `deploy` : 배포 브랜치
