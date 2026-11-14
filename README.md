# EV MOMENT — 충전하는 시간까지, 내 시간으로.

> **현대자동차그룹 차량용 앱 개발 공모전 출품작 (1차 프로토타입 & Android 프로젝트 기반)**  
> 전기차 충전 잔여 시간에 맞춰 주변 활동의 **왕복 도보 시간 + 체류 시간 + 복귀 안전 여유 시간**을 통합 계산하고, 충전 시간 변동 시 추천과 일정을 즉시 재계산하는 차량용 인포테인먼트 서비스입니다.

---

## 1. 핵심 아이디어 및 시간 예산(Time-Budget) 알고리즘

단순히 주변 매장 목록을 나열하는 모바일 지도 앱과 달리, **EV MOMENT**는 차량의 충전 잔여 시간 안에 안전하게 다녀올 수 있는 활동만을 선별해 타임라인을 설계합니다.

- **핵심 판별 수식**: `총 소요 시간 = (편도 도보 시간 × 2 × 동승자 보행 가중치) + 체류 시간 + 복귀 안전 여유 시간(Buffer)`
- **추천 조건**: `총 소요 시간 ≤ 가용 충전 시간`을 만족하는 활동만 필터링하여 우선 추천

### 4대 핵심 화면 및 구현 내용 (프로토타입 시연 가능)
| 화면 | 주요 기능 및 구현 사항 |
| :--- | :--- |
| **01. 충전 현황** | 배터리 잔량(SoC)·가용 시간 표시, 수동 시간 조절 슬라이더, 동승 상황별(나 혼자·아이·반려동물·비즈니스) 보행 가중치 및 선호 카테고리 설정 |
| **02. 주변 장소** | 현대차 양재 본사 충전소 기준 사각형 내비게이션 맵 UI, 거리·도보·체류 기반 여유/초과 판별, 퍼블릭 네트워크 기반 **OpenStreetMap Overpass API 실시간 장소 조회**(네트워크 제한 시 내장 검증 데이터 자동 폴백) |
| **03. 활동 일정** | 출발·체류·복귀 권고 시각 분 단위 계산, 매장 체류↔빠른 픽업 원터치 전환, **실제 스캔 가능한 동적 QR 생성** 및 브라우저 카운트다운·알림(Web Notification/Audio) 데모 |
| **04. 일정 재조정** | 충전 속도 상승(`-12분 단축`) 및 전력 분배(`+15분 지연`) 시나리오 시뮬레이션, 포장 전환·초근거리 매장 변경 등 원터치 일정 재계산 |

---

## 2. 현재 개발 범위 및 2차 본선 확장 계획 (투명한 기술 명세)

본 저장소는 **1차 기획서 제출 및 핵심 UX·알고리즘 시연을 위한 단계**로, 브라우저 기반 인터랙티브 프로토타입과 Android WebView 브릿지 기반 프로젝트 구조를 포함하고 있습니다.

- **1차 구현·시연 완료 범위**:
  - `index.html` 및 `app/src/main/assets/ev_moment_pleos_app.html` 기반의 17인치 Pleos 스타일 4분할 UI(주/야간 테마 전환, 풀스크린 전환 포함)
  - 시간 예산 알고리즘, 동승자 가중치, 실시간 일정 재조정 로직, Overpass API 호출 및 동적 QR 타이머·알림 데모
  - `MainActivity.kt` 내 Android `WebView` 로드, 기본 `TextToSpeech(TTS)` 호출 및 향후 차량 데이터 수신을 위한 `PleosJavascriptBridge` 인터페이스 구조 설계
- **2차 본선(PT) 고도화 예정 범위**:
  - Android Studio 환경에서의 최종 빌드·APK 추출 및 **Pleos Connect Emulator / Virtual Console** 구동 검증
  - 공식 **Pleos Connect SDK**(`Vehicle SDK`, `FusedLocation`, `NaviHelper`, `Pleos TTS`) 실연동 및 차종별 권한 검증
  - 도보 시간 정밀 산출을 위한 실제 보행 경로 API 및 모바일 컴패니언 원격 푸시 연동

---

## 3. 저장소 파일 구성

```text
├── index.html                                     # 브라우저 단독 시연용 프로토타입
├── README.md                                      # 프로젝트 소개 및 실행 안내
├── build.gradle.kts / settings.gradle.kts         # Gradle 8.0+ 프로젝트 설정
├── apk/
│   └── README_APK.md                              # 추후 APK 빌드 및 배치 안내
└── app/
    ├── build.gradle.kts                           # Android 모듈 설정 (minSdk 28, targetSdk 34)
    └── src/main/
        ├── AndroidManifest.xml                    # 네트워크·위치 및 차후 Pleos SDK 연동용 권한 선언
        ├── assets/ev_moment_pleos_app.html        # WebView 탑재용 앱 화면
        └── java/io/pleos/evmoment/MainActivity.kt # WebView, 기본 Android TTS 및 JS 브릿지 구조
