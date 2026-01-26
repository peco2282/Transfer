### Build and Configuration Instructions

This is an Android project developed with Kotlin and Jetpack Compose.

- **Requirements**:
  - Android Studio (latest stable version recommended).
  - JDK 11 (specified in `build.gradle.kts`).
  - Android SDK 36 (Compile and Target SDK).
  - Min SDK 29.

- **Setup**:
  1. Open the project in Android Studio.
  2. Sync Gradle to download dependencies.
  3. Ensure `local.properties` contains the correct `sdk.dir` path.

- **Dependencies**:
  - Jetpack Compose for UI.
  - Room for local database (history).
  - OkHttp for network requests.
  - KSP for Room annotation processing.

### Testing Information

#### Running Tests
- **Unit Tests**: Located in `app/src/test`. Run via Android Studio by right-clicking the folder/file and selecting "Run 'Tests in...'".
- **Instrumented Tests**: Located in `app/src/androidTest`. Requires an emulator or physical device.

#### Adding New Tests
- Use JUnit 4 for unit tests.
- For Compose UI testing, use `createComposeRule()`.
- For Room testing, use an in-memory database.

#### Demo Test
The following test demonstrates how to test the URL extraction logic:

```kotlin
@Test
fun testExtractUrl() {
    val body = "Short URL created!\nShort: https://example.com/api/s/demo"
    val expected = "https://example.com/api/s/demo"
    assertEquals(expected, extractUrl(body))
}
```

### Additional Development Information

- **Code Style**:
  - Follow standard Kotlin coding conventions.
  - Use Jetpack Compose for all new UI components.
  - Use `viewModelScope` for coroutines in ViewModels.
  - Database operations should be handled via Repository and Dao patterns.

- **URL Shortener API**:
  - Endpoint: `https://example.com/api/shorten`
  - Method: POST
  - Query Parameter: `url` (the long URL to shorten)
  - Response Body: Plain text containing the shortened URL with "Short: " prefix.

- **Project Structure**:
  - `ui.theme`: Compose theme definitions.
  - `data`: Room entities, DAO, and Repository.
  - `MainActivity`: Main entry point and core UI logic.
  - `HistoryViewModel`: Manages history data flow.
