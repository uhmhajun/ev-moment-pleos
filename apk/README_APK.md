# APK 상태 및 빌드 안내

**현재 GitHub 제출용 ZIP에는 `.apk` 파일이 포함되어 있지 않습니다.**

`app/src/main/java/io/pleos/evmoment/MainActivity.kt`는 웹 화면을 표시하기 위한 Android WebView 프로젝트 기초 코드이며, 본 패키지의 **Android 빌드 성공, 실제 설치 또는 Pleos 에뮬레이터 실행을 검증하지 않았습니다.** 또한 Pleos Vehicle SDK와 NaviHelper SDK의 실제 연동은 아직 구현하지 않았습니다.

## 추후 APK 빌드 및 검증 절차

1. Android Studio에서 프로젝트 루트 폴더를 엽니다.
2. 프로젝트 요구 Gradle/Android SDK 구성을 확인하고 Sync를 완료합니다.
3. `Build > Build Bundle(s) / APK(s) > Build APK(s)`로 APK 생성에 성공했는지 확인합니다.
4. 생성된 경우 일반적으로 `app/build/outputs/apk/debug/app-debug.apk`에서 파일을 확인할 수 있습니다.
5. Pleos Emulator 또는 지원되는 환경에서 실행·권한·화면·SDK 동작을 검증한 후에만 실제 APK를 제출하세요.

실제 APK를 생성했다면 이 폴더로 복사하거나 GitHub Releases에 업로드할 수 있습니다. **현재 본 폴더 또는 GitHub Releases에서 다운로드 가능한 APK가 제공된다는 의미는 아닙니다.**
