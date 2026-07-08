# GrindOS Android Project - Implementation Plan & Architecture

## 🎯 Project Overview

**GrindOS** is a native Android personal command center app built with **Kotlin**, **Jetpack Compose**, and **modern Android architecture**. It serves as a comprehensive productivity system for FAST 2027 exam prep, daily prayers, Quran reading, habit tracking, and ADHD-friendly task management.

---

## 📦 Dependencies (All Latest Stable Versions - July 2026)

| Dependency | Version | Purpose | Status |
|---|---|---|---|
| Kotlin | 2.1.20 | Language | ✅ Latest stable |
| AGP (Android Gradle Plugin) | 8.9.2 | Build system | ✅ Latest stable |
| Gradle | 8.11.1 | Build tool | ✅ Required by AGP |
| Compose BOM | 2025.06.00 | UI toolkit | ✅ Latest stable |
| Material3 | (from BOM) | Design system | ✅ Latest stable |
| Navigation Compose | 2.8.9 | Navigation | ✅ Latest stable |
| Room | 2.7.1 | Local database | ✅ Latest 2.x stable |
| DataStore | 1.1.4 | Preferences | ✅ Latest stable |
| WorkManager | 2.10.1 | Background tasks | ✅ Latest stable |
| Hilt | 2.56.2 | DI | ✅ Latest stable |
| Lifecycle | 2.9.0 | ViewModel/Flow | ✅ Latest stable |
| Coroutines | 1.10.1 | Async | ✅ Latest stable |
| KSP | 2.1.20-1.0.31 | Annotation processing | ✅ Matches Kotlin |
| Core KTX | 1.16.0 | Android extensions | ✅ Latest stable |
| Activity Compose | 1.10.1 | Activity integration | ✅ Latest stable |

---

## 🏗️ Architecture

### MVVM + Clean Architecture Layers

```
┌─────────────────────────────────────────┐
│              UI Layer                    │
│  Screens → ViewModels → Components      │
│  (Jetpack Compose + StateFlow)          │
├─────────────────────────────────────────┤
│           Domain Layer                   │
│  Models → Use Cases → Business Logic    │
│  (Pure Kotlin, no Android deps)         │
├─────────────────────────────────────────┤
│            Data Layer                    │
│  Repositories → DAOs → Database         │
│  (Room + DataStore + Flow)              │
├─────────────────────────────────────────┤
│         Infrastructure                   │
│  Notifications → DI → Utils             │
│  (Android system APIs)                  │
└─────────────────────────────────────────┘
```

### Key Architectural Decisions

| Decision | Choice | Why |
|---|---|---|
| UI Framework | Jetpack Compose | Modern, declarative, less code |
| Architecture | MVVM | Simple, testable, Google-recommended |
| DI Framework | Hilt | Standard Android DI, easy setup |
| Database | Room | Type-safe SQL, Flow integration |
| Preferences | DataStore | Modern replacement for SharedPreferences |
| Background | WorkManager + AlarmManager | Reliable scheduling |
| State Management | StateFlow | Lifecycle-aware, reactive |
| Navigation | Navigation Compose | Type-safe, deep-link ready |
| Annotation Processing | KSP | Faster than KAPT (2x speed) |
| Build Config | Version Catalog | Centralized dependency management |

---

## 📊 File Count Summary

### Total: 65+ Kotlin files

| Layer | Files | Description |
|---|---|---|
| Build System | 6 | Gradle files, version catalog, properties |
| Config | 3 | AndroidManifest, strings, themes |
| Entry Points | 2 | MainActivity, GrindOSApp |
| Navigation | 3 | Screen routes, NavGraph, BottomBar |
| Theme | 3 | Color, Typography, Theme |
| Domain Models | 10 | Task, Prayer, Quran, StudyTopic, etc. |
| Room Entities | 9 | Database table definitions |
| Room DAOs | 9 | Data access objects |
| Type Converters | 1 | Enum/date converters |
| Database | 1 | Room database class |
| DataStore | 1 | Settings preferences |
| Repositories | 10 | Data layer abstraction |
| Use Cases | 4 | Business logic |
| DI Module | 1 | Hilt configuration |
| Notifications | 4 | Channels, Receiver, Scheduler, Boot |
| Utilities | 2 | DateTime, XpCalculator |
| UI Components | 6 | Reusable composables |
| ViewModels | 7 | State management |
| Screens | 12 | Full screen composables |

---

## 🗺️ Navigation Structure

```
Bottom Navigation (5 tabs)
├── Home (Dashboard)
│   ├── Sprint Timer (sub-screen)
│   └── Settings
├── Study
│   ├── Sprint Timer (sub-screen)
│   └── Error Log (sub-screen)
├── Deen
│   ├── Prayer Tracker (sub-screen)
│   └── Quran Tracker (sub-screen)
├── Life
│   ├── Hackathon (sub-screen)
│   ├── Books (sub-screen)
│   └── Golf (sub-screen)
└── Settings
```

---

## 🗄️ Database Schema

### 9 Tables with Relationships

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   tasks      │  │   prayers    │  │ quran_entries │
│──────────────│  │──────────────│  │──────────────│
│ id (PK)      │  │ id (PK)      │  │ id (PK)      │
│ title        │  │ prayerName   │  │ date         │
│ category     │  │ date         │  │ targetPages  │
│ priority     │  │ time         │  │ pagesRead    │
│ date         │  │ completed    │  │ notes        │
│ time         │  └──────────────┘  └──────────────┘
│ isCompleted  │
│ reminder     │  ┌──────────────┐  ┌──────────────┐
│ tone         │  │ study_topics │  │  error_logs  │
└──────────────┘  │──────────────│  │──────────────│
                  │ id (PK)      │  │ id (PK)      │
┌──────────────┐  │ section      │  │ date         │
│sprint_sessions│ │ name         │  │ section      │
│──────────────│  │ status       │  │ topic        │
│ id (PK)      │  │ accuracy     │  │ question     │
│ mode         │  └──────────────┘  │ mistakeType  │
│ duration     │                    │ whyHappened  │
│ xpEarned     │  ┌──────────────┐  │ correctMethod│
└──────────────┘  │hackathon_tasks│ │ status       │
                  │──────────────│  └──────────────┘
┌──────────────┐  │ id (PK)      │
│ book_entries │  │ title        │  ┌──────────────┐
│──────────────│  │ deadline     │  │ golf_entries │
│ id (PK)      │  │ status       │  │──────────────│
│ bookName     │  │ priority     │  │ id (PK)      │
│ pagesRead    │  └──────────────┘  │ drillName    │
│ date         │                    │ duration     │
└──────────────┘                    └──────────────┘
```

---

## 🔔 Notification System

### 5 Channels
1. **Prayer Reminders** (HIGH priority) - Salah times
2. **Study Reminders** (DEFAULT) - Sprint notifications
3. **Habit Reminders** (DEFAULT) - Quran/books/golf
4. **Hackathon Reminders** (DEFAULT) - Deadlines
5. **Panic Reset** (LOW) - Recovery messages

### 5 Tone Modes
- **Gentle** - "Time for your study sprint. Small win today."
- **Hype** - "BRO LOCK IN. Study sprint starts now 🔥"
- **Roast** - "Scholarship won't spawn while you scroll reels 💀"
- **DeenFirst** - "Salah first. Everything else gets barakah after 🤲"
- **ExamWar** - "40-sec MCQ mode. FAST is watching 🎯"

---

## 🧪 Testing Strategy

| Layer | Test Type | Framework |
|---|---|---|
| DAOs | Unit tests | Room testing, JUnit |
| Repositories | Unit tests | JUnit, Coroutines test |
| ViewModels | Unit tests | Turbine, JUnit |
| Use Cases | Unit tests | JUnit |
| Screens | UI tests | Compose testing |
| Integration | Instrumented | AndroidX Test |

---

## 📱 Build Configuration

```
compileSdk: 35
targetSdk: 35
minSdk: 26

JDK: 17
Kotlin JVM Target: 17

Build Types:
  - debug (with .debug suffix)
  - release (minified + shrunk)

ProGuard: Enabled for release
Configuration Cache: Enabled
Parallel Builds: Enabled
```

---

## 🚀 Build & Deploy

```bash
# Debug build
./gradlew assembleDebug

# Release build (signed)
./gradlew assembleRelease

# Install on device
./gradlew installDebug

# Run tests
./gradlew test
./gradlew connectedAndroidTest
```

---

*Project created: July 8, 2026*  
*Status: Sprint 1-6 Complete ✅ | Sprint 7-8 Pending*
