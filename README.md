<div align="center">

# 🔥 QuickHabit - Habit Tracker

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.1-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-29+-green.svg?style=flat&logo=android)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.12.3-blue.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

![Architecture](https://img.shields.io/badge/Architecture-Clean_Architecture-blue?style=for-the-badge)
![Pattern](https://img.shields.io/badge/Pattern-MVI-purple?style=for-the-badge)
![Persistence](https://img.shields.io/badge/Persistence-Room_Database-orange?style=for-the-badge)

### Daily Habit Tracking App

**A focused single-screen Android app built to consolidate core Android development fundamentals Clean Architecture, MVI, relational Room schema, reactive Flow composition, and Hilt DI before progressing to Adaptive UI and Multi-Module Architecture.**

</div>

---

## 📱 Features

### Core Functionality
- **➕ Add Habits** — Create habits with a name and emoji icon
- **✅ Daily Completion Tracking** — Mark habits done for today, undo if needed
- **🔥 Streak Tracking** — Live current streak and all-time best streak per habit
- **📅 7-Day Grid** — Visual weekly completion history per habit
- **📊 Overall Progress Bar** — Today's completion ratio across all habits
- **🔍 Smart Filtering** — All / Pending / Completed with live counts
- **🗑️ Safe Delete** — Long press triggers a confirmation dialog before deletion
- **♻️ Auto Daily Reset** — Each day starts fresh automatically at midnight

### Technical Highlights
- **🏗️ Clean Architecture** — Strict three-layer separation, domain layer has zero Android imports
- **🔄 MVI Pattern** — All user actions flow through a single typed `Intent`
- **🗄️ Relational Room Schema** — Two tables with `ForeignKey` + `CASCADE` delete
- **⚡ Reactive Pipeline** — `flatMapLatest` + `combine()` merges habits and completions reactively
- **💉 Hilt DI** — Full dependency injection from `Application` → `ViewModel`
- **🎨 Compose Animations** — `animateColorAsState` on cards, `animateFloatAsState` on progress bar
- **🧮 Algorithm Use Cases** — Streak calculation and 7-day grid are isolated, pure Kotlin functions

---

## 📸 Screenshots

<div align="center">

| Habit List | All Completed | Empty State | Delete Dialog |
|:-----------:|:--------------:|:-----------:|:-------------:|
| ![Habit List](assets/screenshots/habit_list.png) | ![Completed](assets/screenshots/all_completed.png) | ![Empty](assets/screenshots/empty_state.png) | ![Delete](assets/screenshots/delete_dialog.png) |

</div>

---

## 🛠️ Tech Stack

| Category | Technology | Why This Choice |
|----------|------------|-----------------|
| **Language** | Kotlin 2.2.1 | Coroutines, Flow, sealed classes for MVI |
| **UI** | Jetpack Compose + Material 3 | Declarative UI, animation APIs, no XML |
| **Architecture** | Clean Architecture + MVI | Testability, unidirectional data flow |
| **Dependency Injection** | Hilt | Compile-time DI, `@HiltViewModel` support |
| **Database** | Room + SQLite | Reactive `Flow<List<T>>`, type-safe SQL |
| **Async** | Kotlin Coroutines + Flow | Native reactive streams, no RxJava |
| **Date Logic** | `java.time.LocalDate` | Clean date arithmetic, no `Calendar` boilerplate |

---

## 🏗️ Architecture

QuickHabit follows **Clean Architecture** with strict layer boundaries. The domain layer is pure Kotlin — no Android imports, no Room, no Compose. All business logic is fully isolated and independently testable.

### Three-Layer Architecture
```
┌──────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────────────┐   │
│  │   Compose    │  │  ViewModel   │  │  MVI Contracts    │   │
│  │  Screen +    │<─┤  (State Mgmt)├─>│ State/Intent/     │   │
│  │  Components  │  │              │  │      Effect       │   │
│  └──────────────┘  └──────────────┘  └───────────────────┘   │
├──────────────────────────────────────────────────────────────┤
│                       DOMAIN LAYER                           │
│              (Pure Kotlin — Zero Android Imports)            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐    │
│  │  Use Cases   │  │    Models    │  │   Repository     │    │
│  │  (6 total)   │  │  (5 models)  │  │   Interface      │    │
│  └──────────────┘  └──────────────┘  └──────────────────┘    │
├──────────────────────────────────────────────────────────────┤
│                        DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐    │
│  │  Repository  │  │  Room DAO    │  │    Entities      │    │
│  │    (Impl)    │<─┤  (@Dao)      ├─>│  (2 entities)    │    │
│  └──────────────┘  └──────────────┘  └──────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```
### MVI Unidirectional Data Flow

```
┌─────────────────────────────────────────────────────────────┐
│                        MVI PATTERN                          │
│                                                             │
│                      ┌──────────────┐                       │
│       ┌─────────────►│   UI/SCREEN  │─────────────┐         │
│       │              │   (Compose)  │             │         │
│       │              └──────────────┘             │         │
│       │                                           │         │
│     STATE                                      INTENT       │
│  (Immutable)                               (Sealed Class)   │
│       │                                           │         │
│       │              ┌──────────────┐             │         │
│       └──────────────│  VIEWMODEL   │◄────────────┘         │
│                      │  onIntent()  │                       │
│                      └──────┬───────┘                       │
│                             │                               │
│                      ┌──────▼───────┐                       │
│                      │  USE CASES   │                       │
│                      └──────┬───────┘                       │
│                             │                               │
│                      ┌──────▼───────┐                       │
│                      │  REPOSITORY  │                       │
│                      │ (Room + DAOs)│                       │
│                      └──────────────┘                       │
└─────────────────────────────────────────────────────────────┘
```
---

## 🗄️ Database Schema

QuickHabit uses **Room** with **2 related entities** — the first relational schema in this learning series.

### Entity Relationship
```
┌──────────────────────────┐           ┌────────────────────────────────┐
│         habits           │           │       habit_completions        │
├──────────────────────────┤           ├────────────────────────────────┤
│ id       INT  (PK, auto) │◄──────────│ id            INT (PK, auto)   │
│ name     TEXT            │   1 : N   │ habitId       INT (FK)         │
│ emoji    TEXT            │           │ completedDate TEXT (yyyy-MM-dd)│
│ createdAt LONG           │           ├────────────────────────────────┤
└──────────────────────────┘           │ UNIQUE (habitId, completedDate)│
                                       └────────────────────────────────┘
```
### Why Two Tables?

A habit is a **long-lived entity**. A completion is a **daily event**. Merging them would mean overwriting history on reset — making it impossible to answer:

- *"Was this habit completed on 14th April?"*
- *"Give me all completion dates for streak calculation"*
- *"What are the last 7 days of completions for the weekly grid?"*

`ForeignKey.CASCADE` ensures deleting a habit automatically removes all its completion history — no orphan rows, no manual cleanup.

### Composite Unique Index

```kotlin
@Index(value = ["habitId", "completedDate"], unique = true)
```

Two jobs in one annotation — speeds up queries filtering by `habitId` + `completedDate`, and enforces at the database level that a habit can only be completed once per day.

---

## 🧮 Use Cases

QuickHabit implements **6 Use Cases** — two contain real algorithms, not simple delegation:

| Use Case | Returns | Responsibility |
|----------|---------|---------------|
| `GetHabitsWithStreakUseCase` | `Flow<List<HabitWithStreak>>` | Orchestrates habits + completions into one UI-ready stream |
| `AddHabitUseCase` | `suspend` | Validates name, constructs `Habit`, inserts |
| `ToggleHabitCompletionUseCase` | `suspend` | Checks if completed today → inserts or deletes |
| `DeleteHabitUseCase` | `suspend` | Delegates deletion (CASCADE handles completions) |
| `CalculateStreakUseCase` | `Pair<Int, Int>` | Pure algorithm: current streak + best streak |
| `BuildWeeklyGridUseCase` | `List<WeekDay>` | Pure algorithm: last 7 days as display-ready cells |

### Streak Calculation Algorithm

```kotlin
// CalculateStreakUseCase.kt — pure Kotlin, zero Android dependencies

private fun calculateCurrentStreak(dates: Set<LocalDate>): Int {
    var streak = 0
    var day = LocalDate.now()

    // If not completed today, start checking from yesterday
    if (!dates.contains(day)) day = day.minusDays(1)

    // Walk backwards while consecutive days exist
    while (dates.contains(day)) {
        streak++
        day = day.minusDays(1)
    }
    return streak
}
```

Completed: today, yesterday, 2 days ago       → streak = 3  ✅
Completed: today, 2 days ago (gap yesterday)  → streak = 1  ✅
Completed: yesterday, 2 days ago (not today)  → streak = 2  ✅
No completions ever                           → streak = 0  ✅

---

## 🎨 MVI Contracts

### `HabitIntent` — Every user action as a typed sealed class

```kotlin
sealed class HabitIntent {
    data class AddHabit(val name: String, val emoji: String) : HabitIntent()
    data class ToggleCompletion(val habitWithStreak: HabitWithStreak) : HabitIntent()
    data class DeleteHabit(val habitWithStreak: HabitWithStreak) : HabitIntent()
    data class ConfirmDelete(val habitWithStreak: HabitWithStreak) : HabitIntent()
    data class SetFilter(val filter: Filter) : HabitIntent()
    data class UpdateInput(val text: String) : HabitIntent()
    data class SelectEmoji(val emoji: String) : HabitIntent()
    data object DismissDialog : HabitIntent()
}
```

### `HabitUiState` — Complete screen snapshot in one immutable data class

```kotlin
data class HabitUiState(
    val allHabits: List<HabitWithStreak> = emptyList(),
    val filteredHabits: List<HabitWithStreak> = emptyList(),
    val selectedFilter: Filter = Filter.ALL,
    val inputText: String = "",
    val selectedEmoji: String = "📌",
    val completedToday: Int = 0,
    val totalHabits: Int = 0,
    val overallProgress: Float = 0f,
    val habitToDelete: HabitWithStreak? = null,  // null = hidden, non-null = show dialog
    val isLoading: Boolean = true
)
```

### Dialog State vs Effect — A Deliberate Design Decision
Snackbar after delete        → UiEffect (Channel)   ✅ fires once, no dismiss action
Delete confirmation dialog   → UiState (nullable)   ✅ survives recomposition + config change

A `Channel` delivers effects exactly once. If the user rotates the device while a dialog is open, a `Channel`-based effect would be consumed before the screen restores — the dialog would disappear. A nullable `habitToDelete` in `UiState` is retained by the `ViewModel` across configuration changes — the dialog reappears correctly.

> **Rule of thumb:** If it has a dismiss or cancel action → it's state. If it fires and vanishes → it's an effect.

---

## 📦 Project Structure
```
quickhabit
│
├── 📁 data/
│   ├── local/
│   │   ├── HabitEntity.kt                ← @Entity for habits table
│   │   ├── HabitCompletionEntity.kt      ← @Entity with ForeignKey + Index
│   │   ├── HabitDao.kt                   ← @Dao
│   │   └── HabitDatabase.kt              ← @Database (2 entities registered)
│   └── repository/
│       └── HabitRepositoryImpl.kt        ← Entity ↔ Domain mappers
│
├── 📁 domain/
│   ├── model/
│   │   ├── Habit.kt
│   │   ├── HabitCompletion.kt
│   │   ├── HabitWithStreak.kt            ← composed UI-ready model
│   │   ├── WeekDay.kt                    ← 7-day grid cell
│   │   └── Filter.kt                     ← enum: ALL, PENDING, COMPLETED
│   ├── repository/
│   │   └── HabitRepository.kt            ← interface only
│   └── usecase/
│       ├── GetHabitsWithStreakUseCase.kt  ← orchestrator
│       ├── AddHabitUseCase.kt
│       ├── ToggleHabitCompletionUseCase.kt
│       ├── DeleteHabitUseCase.kt
│       ├── CalculateStreakUseCase.kt      ← pure algorithm
│       └── BuildWeeklyGridUseCase.kt     ← pure algorithm
│
├── 📁 presentation/
│   ├── components/
│   │   ├── HabitItem.kt                  ← animated card
│   │   ├── WeeklyGrid.kt                 ← 7-day dot row
│   │   ├── StreakInfo.kt                 ← flame + current + best
│   │   ├── AddHabitInput.kt
│   │   ├── EmojiPicker.kt                ← horizontal LazyRow
│   │   ├── OverallProgressBar.kt         ← animated progress
│   │   ├── FilterChipRow.kt              ← chips with live counts
│   │   ├── DeleteConfirmDialog.kt        ← AlertDialog
│   │   └── EmptyHabitsState.kt           ← filter-aware
│   ├── QuickHabitScreen.kt
│   ├── QuickHabitViewModel.kt
│   ├── HabitUiState.kt
│   ├── HabitIntent.kt
│   └── HabitUiEffect.kt
│
└── 📁 di/
└── HabitModule.kt                    ← @Binds + @Provides for Room
```
---

## 🎓 What I Learned

<details>
<summary><b>Relational Schema Design</b></summary>

**Two tables beat one reset column** — My first instinct was to store `isCompletedToday` directly on the habit. That would mean overwriting history every midnight and losing all streak data. Two tables with a foreign key mean I can ask "was this habit completed on any given date?" at any time — history is always intact.

**`ForeignKey.CASCADE` is a feature, not a shortcut** — Without it I'd need to manually delete completions before deleting a habit, or silently accumulate orphan rows. Declaring the relationship in the schema makes the database enforce cleanup automatically.

**Composite unique index does two jobs** — `@Index(value = ["habitId", "completedDate"], unique = true)` both speeds up the most common query and prevents the same habit being marked done twice on the same day at the database level. Two problems, one annotation.

</details>

<details>
<summary><b>Reactive Stream Composition</b></summary>

**`flatMapLatest` is essential for dynamic inner streams** — When a habit is added or deleted, `flatMapLatest` cancels the previous set of completion flows and restarts fresh with the updated habits list. Without it, deleted habits would still have active completion flows running in the background.

**`combine()` on a list of flows is powerful** — Each habit has its own `Flow<List<HabitCompletion>>`. Combining them with `combine(listOfFlows) { it.toList() }` produces a single `Flow<List<HabitWithStreak>>` that fires whenever **any** habit's completions change. Mark one habit done — the whole list reacts. No manual refresh anywhere.

**Repository exposes raw streams, Use Cases combine them** — The repository's job is data access. Assembling `HabitWithStreak` from two streams is business logic. Keeping `combine()` in `GetHabitsWithStreakUseCase` makes each layer's responsibility explicit and keeps the repository independently reusable.

</details>

<details>
<summary><b>MVI at Real Complexity</b></summary>

**Single intent entry point makes debugging trivial** — `onIntent(intent: HabitIntent)` is the only public function that changes state. When something behaves unexpectedly, there is exactly one place to look. With MVVM's multiple public functions, state changes can originate from anywhere.

**Nullable state drives dialogs better than effects** — `habitToDelete: HabitWithStreak?` drives the confirmation dialog. When the user rotates the device mid-dialog, the ViewModel retains state and the dialog reappears. A `Channel<UiEffect>` would consume the event before the screen restores — the dialog would silently disappear.

**`data object` vs `data class` in sealed classes** — `DismissDialog` carries no data, so `data object` is semantically correct. `data class DismissDialog` would work but implies it might carry fields. Small distinction — but precision in sealed classes matters when the intent set grows.

</details>

<details>
<summary><b>Pure Functions in the Domain Layer</b></summary>

**Pure functions are the easiest things to test** — `CalculateStreakUseCase` takes a `List<String>` and returns a `Pair<Int, Int>`. No mocks, no coroutines, no Android context. Testing every streak edge case is just calling the function with a different list and asserting the output.

**Never store what you can calculate** — Storing `currentStreak` as a Room column would mean updating it on every completion, on every app open, and on every midnight reset. Calculating it fresh from completion history on every emission always gives the correct answer with zero sync issues.

</details>

---

## 🚧 Scope: Learning Project vs Production

### What's Implemented

| Feature | Status | Notes |
|---------|--------|-------|
| **Habit CRUD** | ✅ Complete | Add + delete with confirmation dialog |
| **Daily Completion Toggle** | ✅ Complete | Insert / delete completion for today |
| **Current & Best Streak** | ✅ Complete | Calculated fresh from full history |
| **7-Day Weekly Grid** | ✅ Complete | Last 7 days, oldest → newest |
| **Overall Progress Bar** | ✅ Complete | Animated `LinearProgressIndicator` |
| **Filter Chips with Counts** | ✅ Complete | All / Pending / Completed |
| **Auto Daily Reset** | ✅ Complete | Date-aware, resets on new day |
| **Emoji Picker** | ✅ Complete | Preset scrollable `LazyRow` |
| **Cascade Delete** | ✅ Complete | Habit delete removes all completions |

### Production Enhancements

| Enhancement | Why | Complexity |
|-------------|-----|-----------|
| **Habit reminders** | Daily push notifications per habit | Medium (WorkManager + NotificationManager) |
| **Habit reordering** | Drag to prioritise | Medium (drag-and-drop in `LazyColumn`) |
| **Full history screen** | View completions beyond 7 days | Low (second screen — planned in Navigation phase) |
| **Unit tests** | `CalculateStreakUseCase` is purely testable with no mocks | Low |
| **Habit categories** | Visual grouping with colours | Low (extra column + colour picker) |
| **Home screen widget** | Quick mark-done without opening the app | High (Glance API) |

---

## 🗺️ Roadmap

- [x] Phase 1: Domain models + repository interface
- [x] Phase 2: Algorithm use cases (streak + weekly grid)
- [x] Phase 3: Room schema — 2 entities + ForeignKey + CASCADE
- [x] Phase 4: Reactive pipeline (`flatMapLatest` + `combine()`)
- [x] Phase 5: MVI ViewModel + Hilt wiring
- [x] Phase 6: Compose UI — animations, emoji picker, weekly grid, dialog
- [ ] Phase 7: Unit tests for `CalculateStreakUseCase` + `BuildWeeklyGridUseCase`
- [ ] Phase 8: Habit reminders via WorkManager
- [ ] Phase 9: Full history screen (multi-screen Navigation phase)

---

## 👤 Author

**Usman Ali Ansari**

- 💼 LinkedIn: [usman1ansari](https://www.linkedin.com/in/usman1ansari)

---

<div align="center">

**Built with ❤️ as part of a deliberate Android learning progression**

</div>
