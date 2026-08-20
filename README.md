# N-Queens

An Android puzzle game built with Jetpack Compose: place `n` non-attacking queens on an `n × n`
board. Pick a board size and a help level, place queens by tapping, and beat your best time.

## Build / Run / Test

```bash
# Build a debug APK
./gradlew assembleDebug

# Unit tests (domain, data, ViewModels)
./gradlew testDebugUnitTest

# Instrumented UI test (needs a running emulator/device)
./gradlew connectedDebugAndroidTest
```

Or open the project in Android Studio and Run the `app` configuration.

## Architecture

- Project: **Clean Architecture**
- View Architecture: **MVVM + MVI**

```
ui/            Compose screens + components (no game rules live here)
 ├─ screen/setup   board size + help level selection
 ├─ screen/game    board, controls, victory dialog + GameViewModel
 └─ screen/score   paginated scoreboard + ScoreViewModel
domain/        framework-agnostic core
 ├─ model          Position, Score, HelpLevel, ...
 ├─ usecase        IsValidQueenPosition (the rules)
 └─ repository     ScoreRepository (interface)
data/          implementations
 ├─ ScoreRepositoryImpl
 └─ storage        Room: ScoreEntity, ScoreDao, NQueensDatabase
di/            Hilt modules (Database, Repository)
infra/         cross-cutting helpers (formatting extensions)
```

### Key decisions

- **MVVM with a light MVI flavor**
  - Each screen has a ViewModel that exposes an immutable `StateFlow<UiState>` to drive the UI
  - The game additionally emits one-shot events through a `Channel` for banners, navigation or
    alerts
- **Separation of UI and logic**
  - UI and navigation are kept separate from business logic
  - Composables only render from a `UiState`
  - Business logic lives in the `domain` layer, following Clean Architecture guidelines
- **Dependency injection**
  - Hilt for DI, following the latest Android guidelines
  - Classes are injected rather than constructed directly, to improve maintainability and
    testability
- **Persistence with Room + Paging 3**
  - Room as the SQL database for storing scores
  - Paging 3 to paginate database queries and keep the UI responsive
  - Paging models are exposed in the `domain` layer. Domain should not depend on Android, so this
    is a deliberate exception, made given how closely Composables and Room's paging integrate.
- **Gradle Monomodule**
  - Used one gradle module for this interview process given the size of the task
  - Root packages could be converted into gradle module due to package dependencies are
    unidirectional
- **`java.time`**
    - Used for timestamps (via **core library desugaring** for API 24 support)
    - Durations are stored as millis (`Long`) and formatted only at the UI edge
