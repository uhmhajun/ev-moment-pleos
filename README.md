# EV MOMENT — 충전하는 시간까지, 내 시간으로.

> 현대자동차그룹 차량용 앱 개발 공모전 출품용 **인터랙티브 프로토타입 및 Android 프로젝트 소스**
>
> 전기차 충전 잔여 시간에 맞춰 주변 활동의 **왕복 이동 시간 + 체류 시간 + 복귀 여유 시간**을 계산하고, 충전 시간이 바뀌면 추천·일정을 재계산하는 서비스입니다.

## 제출물 상태 (중요)

- **실제로 시연 가능:** `index.html` 기반 브라우저 프로토타입. 충전 시간 수동 입력·시연값 변경, 장소별 시간 예산 판별, 동승 유형별 가중치, 일정 재계산, 화면 전환, QR 생성·웹 알림 테스트 등이 포함됩니다.
- **실제 네트워크 호출 코드 포함:** OpenStreetMap Overpass API로 장소 정보를 요청하는 기능이 있습니다. 인터넷 연결, API 가용성, 브라우저 정책에 따라 실패할 수 있으며, 실패 시 내장 장소 데이터로 시연합니다. 도보 시간은 현재 실제 보행 경로 API 결과가 아닌 거리 기반 근삿값입니다.
- **Android 프로젝트 소스 포함:** `MainActivity.kt`에서 로컬 HTML을 Android WebView로 로드하고 Android 기본 `TextToSpeech`를 사용하는 기반 코드가 있습니다. **빌드·에뮬레이터 실행은 확인되지 않았습니다.**
- **미구현 / 미검증:** Pleos Vehicle SDK의 실제 SoC·충전·기어 데이터 구독, Virtual Console 실제 연결, Pleos FusedLocation SDK, NaviHelper SDK 경유지 등록, Pleos 전용 TTS SDK, 실차 테스트 및 원격 스마트폰 푸시 알림. 관련 함수·권한은 연동 계획 또는 데모용 연결 지점이지 실제 SDK 연결 증거가 아닙니다.
- **APK 미포함:** 이 ZIP에는 빌드된 `.apk` 파일이 없습니다. `apk/` 폴더에는 생성·검증 방법 안내만 있습니다. GitHub Releases에도 APK가 있다고 가정하지 마세요.

위 내용은 소스코드 기준 설명입니다. **Pleos SDK를 실제 연동했다고 주장하거나, APK가 이미 생성되었다고 주장하지 않습니다.**

## 1. 주요 구현 기능

시간 예산 계산 방식:

`총 소요 시간 = (편도 도보 시간 × 2 × 동승자 보행 가중치) + 체류 시간 + 복귀 여유 시간`

`총 소요 시간 ≤ 가용 충전 시간`인 활동을 추천합니다.

| 화면 | 웹 프로토타입 구현 내용 |
|---|---|
| 01 충전 현황 | 배터리·시간 시연값, 충전 시간 수동 조정, 동승자 유형 선택 |
| 02 장소 탐색 | 내장 장소 데이터와 지도 형태의 시각화, 조건별 추천, Overpass API 호출 시도 |
| 03 활동 타임라인 | 도보 이동·체류·복귀 권고 시각 계산, 픽업/체류 모드 전환, QR 및 브라우저 알림 데모 |
| 04 일정 재계산 | 잔여 시간 -12분 단축 / +15분 지연을 수동 시뮬레이션하여 추천 재산출 |

**참고:** 브라우저 화면에 나타나는 차량 상태·지도·충전 상황은 주로 시연용입니다. 실제 차량 텔레메트리 또는 순정 내비게이션 조작이 아닙니다. QR은 현재 페이지 URL을 인코딩하므로 `file://`로 연 로컬 페이지는 다른 스마트폰에서 바로 접속되지 않을 수 있습니다. 브라우저 알림은 사용자의 권한 허용과 지원 환경에 좌우되며, 별도 모바일 푸시 서버는 없습니다.

## 2. 파일 구성

```text
index.html                              # 브라우저 독립 시연본
app/src/main/assets/ev_moment_pleos_app.html  # Android WebView에 로드되는 같은 화면
app/src/main/java/io/pleos/evmoment/MainActivity.kt  # WebView / 기본 Android TTS 및 데모 브리지
app/src/main/AndroidManifest.xml        # Android 설정 및 차후 SDK 검토용 권한 선언
app/build.gradle.kts                    # Android 모듈 빌드 설정
settings.gradle.kts / build.gradle.kts  # Gradle 설정
apk/README_APK.md                       # APK 미제공 상태 및 추후 빌드 절차
```

`PleosSdkBridge.kt`는 이 ZIP에 존재하지 않습니다. SDK의 실제 구독/호출 클래스도 포함되어 있지 않습니다.

## 3. 실행 및 테스트 방법

### A. 브라우저 시연 (현재 제공되는 검증 경로)

1. 압축을 풀고 루트의 **`index.html`**을 Chrome 또는 Edge에서 엽니다.
2. 충전 시간을 수동으로 조절하고 동승 유형을 변경합니다.
3. 주변 장소의 활동 가능 여부·활동 타임라인을 확인합니다.
4. `-12분 단축` / `+15분 지연`을 눌러 일정 재계산을 확인합니다.
5. 네트워크가 되는 환경에서는 장소 API 요청과 QR·브라우저 알림 데모를 별도로 시험할 수 있습니다.

일부 CSS·폰트·스크립트는 외부 CDN에 의존합니다. 인터넷 연결이 없으면 화면 구성 또는 추가 기능에 제약이 생길 수 있습니다.

### B. Android Studio 빌드 **시도** (소스 제공, 검증 전)

1. Android Studio에서 저장소 루트를 엽니다. Android SDK 및 프로젝트가 요구하는 Gradle 버전이 설치되어 있어야 합니다.
2. Gradle Sync 및 빌드 오류가 없는지 확인합니다. 이 제출 ZIP의 Android 프로젝트는 별도 실기기·Pleos 에뮬레이터 빌드 검증을 완료하지 않았습니다.
3. 필요한 설정을 완료한 후 `Build > Build Bundle(s) / APK(s) > Build APK(s)`로 APK 빌드를 **시도**할 수 있습니다.
4. 빌드에 성공했다면 일반적인 Android Gradle 프로젝트에서 `app/build/outputs/apk/debug/app-debug.apk` 경로를 확인합니다. 실제 실행 테스트 후에만 APK를 제출하세요.

**주의:** 이 단계만으로 Pleos 전용 SDK 호출이나 차량 정보 연동이 이루어지지 않습니다. SDK 의존성, 정확한 권한명 및 API 호출은 Pleos Playground 최신 문서에 맞춰 별도 구현·검증해야 합니다.

## 4. Pleos SDK 연동 계획 (현재 미구현)

- **Vehicle SDK / Virtual Console:** 차량 배터리 SoC·충전·기어 정보를 구독한 뒤 `MainActivity.notifyVirtualConsoleTelemetry(...)`와 웹의 `window.onPleosVehicleUpdate(...)`로 전달할 계획입니다. 현재 이 함수는 데이터 수신 연결 지점으로만 존재하며, 실제 SDK 이벤트 구독이 없습니다.
- **NaviHelper SDK:** 추천 장소를 차량 내비게이션에 등록할 계획입니다. 현재 `sendWaypointToNaviHelper(...)`는 미구현 상태를 반환합니다. 실제 경유지가 생성되지 않습니다.
- **FusedLocation SDK:** 차량 위치를 얻어 장소 검색의 기준점으로 사용할 계획입니다. 현재 검색 좌표는 양재 주변 시연값입니다.
- **음성 안내:** Android 기본 `TextToSpeech` 엔진을 사용하는 코드는 포함되어 있지만, Pleos 전용 TTS SDK 연동 및 실제 음성 출력 환경 검증은 완료하지 않았습니다.
- **권한:** `AndroidManifest.xml`의 Pleos 관련 권한 이름은 후속 연동을 검토하기 위한 선언이며, 해당 API 연결이나 승인 여부를 뜻하지 않습니다. 실제 적용 전 공식 권한 이름·지원 범위·배포 심사 요구사항을 확인해야 합니다.

## 5. APK 및 GitHub 제출 안내

- **APK 현황:** 생성된 APK **없음** (`apk/README_APK.md` 참고).
- **GitHub 링크:** 이 ZIP을 본인 저장소에 업로드한 뒤 해당 **실제 저장소 URL**을 공모전 접수양식에 기재하세요. ZIP만으로 GitHub 주소가 자동 생성되지 않습니다.
- **검증 범위:** 브라우저 기반 기능을 시연하는 단계입니다. Android/AAOS/Pleos Emulator 통합 검증 및 APK 배포는 후속 작업입니다.

## 6. 향후 작업

1. 공식 Pleos SDK와 에뮬레이터 구성 및 필요한 실제 API 호출 연결
2. 차량 충전 상태 변경에 따른 자동 재계산 검증
3. 위치·보행 경로·내비 연동을 실제 데이터로 대체
4. Android APK 빌드, 설치, 실행 및 권한·주행 안전성 검증
5. 필요하면 HTTPS 배포 기반 모바일 타이머와 원격 알림 별도 구현
