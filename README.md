# Transfer - URL Shortener

A simple Android application to shorten URLs using a custom API and keep track of shortening history.

## Overview

Transfer allows users to:
- Shorten long URLs via a configurable shortener API.
- View a history of shortened URLs.
- Copy shortened URLs to the clipboard.
- Delete history entries.

## Requirements

- **Android SDK**: Compile/Target SDK 36, Min SDK 29.
- **JDK**: 11.
- **Android Studio**: Ladybug or newer.

## Setup and Run

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and build the project.
4. Add your private server URL to `local.properties` (not committed):
   - `shortener.baseUrl=https://your-private-domain.example/api/shorten`
5. Run the `app` module on an emulator or a physical device (API level 29+).

## Scripts and Commands

- `./gradlew assembleDebug`: Build the debug APK.
- `./gradlew test`: Run unit tests.
- `./gradlew connectedAndroidTest`: Run instrumented tests on a device.

## Configuration

The shortener endpoint is read from `local.properties` via `shortener.baseUrl`. If unset, the app uses a placeholder.

## Tests

- **Unit Tests**: Found in `app/src/test`. These tests cover core logic like URL extraction from API responses.
- **Instrumented Tests**: Found in `app/src/androidTest`. These tests cover UI and Room database integration.

## Project Structure

- `app/src/main/java/com/peco2282/transfer/`:
    - `MainActivity.kt`: Main UI and shortening logic.
    - `HistoryViewModel.kt`: ViewModel for history management.
    - `data/`: Room database, DAO, Entity, and Repository.
    - `ui/theme/`: Compose theme and styling.

## TODOs

- [ ] Add URL validation before sending to API.
- [ ] Implement error handling for network failures.
- [ ] Add ability to share shortened URLs directly.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
