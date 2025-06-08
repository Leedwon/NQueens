# NQueens App

NQueens is a simple Android app that lets users play the classic N-Queens puzzle on a customizable
board (from 4×4 up to 12×12).
The app offers real-time conflict validation, smooth animations, and a local leaderboard to track
best completion times per board size.

## Running the app

To run the app from the terminal, use the following command:

```bash
./gradlew :app:installDebug
```

This will build and install the app on your connected device or emulator.

## Running tests

### Unit tests

To run unit tests from the terminal, use the following command:

```bash
./gradlew :app:testDebugUnitTest
```

### UI tests

To run UI tests (including E2E tests) from the terminal, use the following command:

```bash
./gradlew :app:connectedDebugAndroidTest
```

⚠️ Note: Ensure to turn off animations on your device without this, UI tests may fail due to timing
issues. You can do this by enabling "Developer options" and setting "Window animation scale", "
Transition animation scale", and "Animator duration scale" to "Animation off".

## Architecture

The NQueens app follows a layered architecture that cleanly separates logic, data, and UI
responsibilities:

### Core

Contains the essential logic and data layer of the app. It includes:

- The `GameEngine`, which drives the entire game logic and enforces the N-Queens rules.
- The `Room` database setup, which serves as the data source for the in-game leaderboard.
- Utility packages for coroutines, date/time, and stopwatch functionality.

This layer is purely logic-focused.

### Services

Hosts repositories that aggregate and manage data from data sources (database in our app case).
Expose simplified APIs to be used by ViewModels or other parts of the app.

### Features (UI)

Includes:

- ViewModels and helper classes that create state for views and handle user actions exposed by
  views.
- Views - Composable functions that render the UI based on the state provided by ViewModels and
  expose user actions to ViewModels.

## Testing strategy

The app follows a comprehensive testing strategy with a testing pyramid approach:

- **Unit tests**: Testing individual components in isolation. Most components are covered by unit
  tests to ensure their correctness.
- **UI tests**: Testing the integration between UI and logic. UI tests are added for each screen (
  game, leaderboard, main menu, win) to ensure proper UI behavior and integration.
- **E2E tests**: Part of UI tests that test the whole game flow from start to finish, ensuring that
  all components work together correctly.

In all tests we prefer real implementations over fakes and fakes over mocks to ensure that code
under test is as close to production code as possible.

## Architectural Decision Records (ADRs)

## 🧠 ADR : Conflict Representation in GameEngine

### 📌 Problem

The N-Queens game requires real-time validation and visual highlighting of conflicts between queens.
As part of the game engine design, we need to decide:

How should we represent conflicts in the `GameEngine` state so that it supports real-time
validation.

This representation should:

- Be efficient for real-time updates.
- Enable accurate UI rendering of conflicts.

---

### 🧪 Options Considered

| Representation                             | Description                                                                | Pros                                                                                                                               | Cons                                       |
|--------------------------------------------|----------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------|
| **Exact Positions (`Set<BoardPosition>`)** | Store all squares where a conflict occurs.                                 | ✅ Simple for UI to render<br>✅ Precise cell-level highlighting                                                                     | ❌ Slight duplication in computation        |
| **Structured Lines (`Conflicts` class)**   | Track conflicting rows, columns, and diagonals using sets of line indices. | ✅ Compact and expressive<br>✅ Ideal for line-based highlighting<br>✅ Memory efficient - no need to store all conflicting positions | ❌ Requires transformation for UI rendering |

---

### ✅ Decision

We chose to use the **Structured `Conflicts` representation** within the `GameEngine`:

```kotlin
data class Conflicts(
    val rows: Set<Int>,
    val columns: Set<Int>,
    val majorDiagonals: Set<Int>, // row - col
    val minorDiagonals: Set<Int> // row + col
)
```

### 🧠 Rationale:

- This representation directly aligns with how conflicts are defined in the N-Queens rules.
- It allows us to efficiently track conflicts without needing to store every individual position.
- Even though board size is small, this approach is more efficient than storing all positions.
- Because it is more compact than storing full position sets, it can be tested and reasoned about
  more easily.

## 🧠 ADR: Conflict Detection Strategy in GameEngine

### 📌 Problem

In the N-Queens game, a conflict occurs when two queens threaten each other. This happens when two
or more queens are placed in the same:

- **Row**
- **Column**
- **Diagonal**:
    - **Major Diagonal**: `row - col` is equal
    - **Minor Diagonal**: `row + col` is equal

The `GameEngine` must detect these conflicts efficiently and accurately in order to support:

- Real-time validation
- Visual feedback
- Win detection logic

We need to choose an algorithm for detecting conflicts among the currently placed queens.

---

### 🧪 Options Considered

| Approach                | Description                                                                     | Complexity                        | Notes                                                                                                                              |
|-------------------------|---------------------------------------------------------------------------------|-----------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| **Pairwise comparison** | Compare every pair of queens to check for conflict conditions.                  | O(q²), where q = number of queens | ✅ Simple to implement.<br>❌ May redundantly detect the same conflict multiple times.<br>❌ Slightly verbose for highlighting logic. |
| **Line-based counting** | Use hash maps to count how many queens exist in each row, column, and diagonal. | O(q + N), where N = board size    | ✅ More performant.<br>✅ Matches well with structured line-based conflict representation.                                           |

---

### ✅ Decision

We chose to implement line-based conflict detection using hash maps to track queen counts per:

- Row (`Map<Int, Int>`)
- Column (`Map<Int, Int>`)
- Major Diagonal (`Map<Int, Int>` for `row - col`)
- Minor Diagonal (`Map<Int, Int>` for `row + col`)

A conflict is identified when any count exceeds 1.

---

### 🧠 Rationale

- Line-based detection aligns directly with how the N-Queens rules are defined.
- It integrates naturally with our `Conflicts` structure, which represents rows, columns, and
  diagonals.
- Even though performance is not a primary concern since the board size is small, this approach is
  still more efficient.

---

## 🧠 ADR: Storing leaderboards locally

### 📌 Problem

The N-Queens game includes a leaderboard to showcase player scores. We need a way to store these
scores locally in a format that:

- Supports retrieval and display of scores per board size.
- Allows future extensibility.
- Balances simplicity with flexibility.

### 🧪 Options Considered

| Storage Method            | Description                                                                                                     | Pros                                                                                                           | Cons                                                                                            |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| **Preferences DataStore** | Store scores as key-value pairs using board size as the key and sets of strings (representing times) as values. | ✅ Simple to implement<br>✅ Low overhead                                                                        | ❌ Limited to primitive types<br>❌ Requires string-to-long conversion<br>❌ No duplicates allowed |
| **SQLite Database**       | Store scores using a structured table with board size and time as columns.                                      | ✅ Supports complex structures<br>✅ Stores times as `Long`<br>✅ More metadata can be added easily in the future | ❌ Adds moderate implementation complexity                                                       |

### ✅ Decision

We chose to use an SQLite database to store player scores locally.

### 🧠 Rationale

- SQLite enables structured data modeling, ideal for associating times with board sizes.
- It allows scores to be stored as Longs, avoiding error-prone conversions.
- While slightly more complex than DataStore, SQLite provides greater flexibility and
  future-proofing.

## 🧠 ADR: Retrieving scores from the database

### 📌 Problem

The N-Queens app maintains a local leaderboard where multiple completion times are stored per board
size (4x4 to 12x12).
The leaderboard UI expects a structure like:

```kotlin
data class Leaderboard(
    val boardSize: Int,
    val elapsedMillis: List<Long>
)
```

A natural first idea might be to use SQL's GROUP_CONCAT to group times directly in the query, like:

```sql
SELECT boardSize, GROUP_CONCAT(elapsedMillis) FROM leaderboard GROUP BY boardSize
```

However, this comes with some issues related to parsing and type safety which will be described
below.

### 🧪 Options Considered

| Approach                              | Description                                                            | Pros                                                                     | Cons                                                                    |
|---------------------------------------|------------------------------------------------------------------------|--------------------------------------------------------------------------|-------------------------------------------------------------------------|
| `GROUP_CONCAT` in SQL                 | Use SQL to return a comma-separated list of times for each board size. | ✅ Fewer rows<br>✅ Compact result<br>✅ Feels natural from SQL perspective | ❌ Requires string parsing<br>❌ Loses type safety<br>❌ Awkward with Room |
| `SELECT *` and group + sort in Kotlin | Fetch all rows and group/sort them in Kotlin after retrieval.          | ✅ Full control<br>✅ Type-safe<br>✅ Simple Room query                     | ❌ Slightly more memory use<br>❌ All rows must be loaded at once         |
| `SELECT * ORDER BY` + group in Kotlin | Use SQL for sorting, then group by boardSize in Kotlin.                | ✅ Clean separation of concerns<br>✅ Type-safe<br>✅ Efficient sort        | ❌ Relies on predictable sorting from SQL                                |

### ✅ Decision

We selected the sorted SQL + Kotlin grouping approach:

🧠 Rationale

-SQL handles sorting efficiently, Kotlin handles grouping clearly.
-Avoids string manipulation and parsing entirely.
-Plays well with Room
