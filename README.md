# NQueens App

## 🧠 ADR: Conflict Representation in GameEngine

### 📌 Problem

The N-Queens game requires real-time validation and visual highlighting of conflicts between queens.
As part of the game engine design, we need to decide:

How should we represent conflicts in the `GameEngine` state so that it supports real-time validation.

This representation should:

- Be efficient for real-time updates.
- Enable accurate UI rendering of conflicts.

---

### 🧪 Options Considered

| Representation                             | Description                                                                | Pros                                                            | Cons                                       |
|--------------------------------------------|----------------------------------------------------------------------------|-----------------------------------------------------------------|--------------------------------------------|
| **Exact Positions (`Set<BoardPosition>`)** | Store all squares where a conflict occurs.                                 | ✅ Simple for UI to render<br>✅ Precise cell-level highlighting  | ❌ Slight duplication in computation        |
| **Structured Lines (`Conflicts` class)**   | Track conflicting rows, columns, and diagonals using sets of line indices. | ✅ Compact and expressive<br>✅ Ideal for line-based highlighting | ❌ Requires transformation for UI rendering |

---

### ✅ Decision

We chose to use the **Structured `Conflicts` representation** within the `GameEngine`:

```kotlin
data class Conflicts(
    val rows: Set<Int>,
    val columns: Set<Int>,
    val majorDiagonals: Set<Int>,
    val minorDiagonals: Set<Int>
)
```

### 🧠 Rationale:

- This approach keeps the engine efficient and focused on rule evaluation, not rendering details.
- It maintains a clean separation of concerns: game logic stays in the engine; visual interpretation
  is handled in the ViewModel.
- It is more compact than storing full position sets and can be tested and reason about more easily

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

The N-Queens game includes a leaderboard to showcase player scores. We need a way to store these scores locally in a format that:
- Supports retrieval and display of scores per board size.
- Allows future extensibility.
- Balances simplicity with flexibility.

### 🧪 Options Considered

| Storage Method            | Description                                                                                                     | Pros                                                                                           | Cons                                                                                            |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| **Preferences DataStore** | Store scores as key-value pairs using board size as the key and sets of strings (representing times) as values. | ✅ Simple to implement<br>✅ Low overhead                                                        | ❌ Limited to primitive types<br>❌ Requires string-to-long conversion<br>❌ No duplicates allowed |
| **SQLite Database**       | Store scores using a structured table with board size and time as columns.                                      | ✅ Supports complex structures<br>✅ Stores times as `Long`<br>✅ Scales well for future features | ❌ Adds moderate implementation complexity                                                       |

### ✅ Decision

We chose to use an SQLite database to store player scores locally.
# NQueens App

## 🧠 ADR: Conflict Representation in GameEngine

### 📌 Problem

The N-Queens game requires real-time validation and visual highlighting of conflicts between queens.
As part of the game engine design, we need to decide:

How should we represent conflicts in the `GameEngine` state so that it supports real-time validation.

This representation should:

- Be efficient for real-time updates.
- Enable accurate UI rendering of conflicts.

---

### 🧪 Options Considered

| Representation                             | Description                                                                | Pros                                                            | Cons                                       |
|--------------------------------------------|----------------------------------------------------------------------------|-----------------------------------------------------------------|--------------------------------------------|
| **Exact Positions (`Set<BoardPosition>`)** | Store all squares where a conflict occurs.                                 | ✅ Simple for UI to render<br>✅ Precise cell-level highlighting  | ❌ Slight duplication in computation        |
| **Structured Lines (`Conflicts` class)**   | Track conflicting rows, columns, and diagonals using sets of line indices. | ✅ Compact and expressive<br>✅ Ideal for line-based highlighting | ❌ Requires transformation for UI rendering |

---

### ✅ Decision

We chose to use the **Structured `Conflicts` representation** within the `GameEngine`:

```kotlin
data class Conflicts(
    val rows: Set<Int>,
    val columns: Set<Int>,
    val majorDiagonals: Set<Int>,
    val minorDiagonals: Set<Int>
)
```

### 🧠 Rationale:

- This approach keeps the engine efficient and focused on rule evaluation, not rendering details.
- It maintains a clean separation of concerns: game logic stays in the engine; visual interpretation
  is handled in the ViewModel.
- It is more compact than storing full position sets and can be tested and reason about more easily

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

The N-Queens game includes a leaderboard to showcase player scores. We need a way to store these scores locally in a format that:
- Supports retrieval and display of scores per board size.
- Allows future extensibility.
- Balances simplicity with flexibility.

### 🧪 Options Considered

| Storage Method            | Description                                                                                                     | Pros                                                                                           | Cons                                                                                            |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| **Preferences DataStore** | Store scores as key-value pairs using board size as the key and sets of strings (representing times) as values. | ✅ Simple to implement<br>✅ Low overhead                                                        | ❌ Limited to primitive types<br>❌ Requires string-to-long conversion<br>❌ No duplicates allowed |
| **SQLite Database**       | Store scores using a structured table with board size and time as columns.                                      | ✅ Supports complex structures<br>✅ Stores times as `Long`<br>✅ Scales well for future features | ❌ Adds moderate implementation complexity                                                       |

### ✅ Decision

We chose to use an SQLite database to store player scores locally.

### 🧠 Rationale

- SQLite enables structured data modeling, ideal for associating times with board sizes.
- It allows scores to be stored as Longs, avoiding error-prone conversions.
- While slightly more complex than DataStore, SQLite provides greater flexibility and future-proofing.


### 🧠 Rationale

- SQLite enables structured data modeling, ideal for associating times with board sizes.
- It allows scores to be stored as Longs, avoiding error-prone conversions.
- While slightly more complex than DataStore, SQLite provides greater flexibility and future-proofing.

