# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Building the Project
```bash
# Build the entire project
./gradlew build

# Assemble without running tests
./gradlew assemble

# Clean build outputs
./gradlew clean
```

### Running Tests
```bash
# Run all tests
./gradlew allTests

# Run JVM tests only
./gradlew jvmTest

# Run specific platform tests
./gradlew jsTest           # JavaScript tests
./gradlew iosX64Test       # iOS X64 tests
./gradlew iosSimulatorArm64Test  # iOS Simulator ARM64 tests
```

### Code Quality
```bash
# Run Spotless code formatter check
./gradlew spotlessCheck

# Apply Spotless code formatting
./gradlew spotlessApply

# Run all checks (tests + spotless)
./gradlew check
```

## Architecture Overview

This is a Kotlin Multiplatform library for the Trakt API, supporting JVM, JavaScript, and iOS platforms.

### Key Components

1. **Main Entry Point**: `Trakt` class (lib/src/commonMain/kotlin/app/moviebase/trakt/Trakt.kt:26)
   - Factory function with DSL configuration
   - Lazy-loaded API endpoints
   - HTTP client configuration with interceptors

2. **API Structure**: Each API category has its own class in `app.moviebase.trakt.api`:
   - `TraktMoviesApi` - Movie-related endpoints
   - `TraktShowsApi` - TV show endpoints
   - `TraktSeasonsApi` - Season endpoints
   - `TraktEpisodesApi` - Episode endpoints
   - `TraktSearchApi` - Search functionality
   - `TraktUsersApi` - User-related operations
   - `TraktSyncApi` - Synchronization features
   - `TraktAuthApi` - Authentication handling

3. **Core Infrastructure**:
   - `HttpClientFactory` - Creates configured Ktor HTTP clients
   - `TraktClientConfig` - Configuration DSL for client setup
   - Request interceptors for API key and version headers
   - JSON serialization with kotlinx-serialization

4. **Platform-Specific Implementations**:
   - JVM: Uses OkHttp engine
   - iOS: Uses Darwin engine
   - JS: Browser and Node.js support

### Dependency Management

The project uses Gradle version catalogs (gradle/libs.versions.toml) for dependency management:
- Kotlin 2.0.21
- Ktor 3.0.1 for HTTP client
- kotlinx-serialization 1.7.3
- kotlinx-datetime 0.6.1
- Spotless 7.0.0.BETA4 for code formatting

### Testing Approach

- Unit tests in `jvmTest` using JUnit 5 and Google Truth
- Integration tests that interact with the actual Trakt API
- Test fixtures for mocked responses in `src/jvmTest/resources/trakt/`