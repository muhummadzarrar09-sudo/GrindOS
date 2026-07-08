# GrindOS - Personal Command Center ⚡📱

**A native Android app built with Kotlin, Jetpack Compose, and modern Android architecture**

GrindOS is your personal operating system for FAST 2027 prep, prayers, Quran, books, golf, hackathon tasks, custom reminders, ADHD-friendly routine, and daily mission execution.

---

## 🎯 Features

### Core Modules
- **📊 Home Dashboard** - Today's mission, top 3 priorities, prayer status, Quran progress, XP/streak tracking
- **📚 FAST Study Module** - Topic tracker with 5 sections (Basic Math → English), status progression, sprint timer
- **🕌 Deen Module** - Prayer tracker (5 daily prayers), Quran page tracker with streaks
- **💻 Life Module** - Hackathon kanban board, books reading tracker, golf practice log
- **⚙️ Settings** - Notification modes, daily targets, customization

### Key Features
- ✅ **Task Management** - Categories, priorities, reminders, completion tracking
- 🔔 **Smart Notifications** - 5 modes (Gentle, Hype, Roast, DeenFirst, ExamWar)
- 🕌 **Prayer Tracker** - 5 daily prayers with manual times, completion checklist
- 📖 **Quran Tracker** - Daily pages, progress tracking, streaks
- ⏱️ **Sprint Timer** - 4 modes (5min, 25min, 50min, custom MCQ drill)
- 🐛 **Error Log** - Track mistakes, analyze patterns, mark as fixed
- 💻 **Hackathon Board** - Kanban (Backlog → Doing → Done)
- 📚 **Books & Golf** - Simple habit tracking
- 🎮 **Gamification** - XP system, levels, streaks, achievements
- 🧘 **Panic Button** - ADHD-friendly reset with calming messages

---

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin 2.1.20
- **Build System**: Gradle 8.11.1 with Kotlin DSL
- **UI Framework**: Jetpack Compose (BOM 2025.06.00)
- **Architecture**: MVVM with Clean Architecture principles

### Android Libraries
- **Android Gradle Plugin**: 8.9.2
- **Room Database**: 2.7.1 (local persistence)
- **DataStore**: 1.1.4 (preferences/settings)
- **Navigation Compose**: 2.8.9 (screen navigation)
- **Hilt**: 2.56.2 (dependency injection)
- **WorkManager**: 2.10.1 (background tasks)
- **Lifecycle**: 2.9.0 (ViewModel, LiveData, Flow)
- **Coroutines**: 1.10.1 (async programming)

### Key Patterns
- **Repository Pattern** - Data layer abstraction
- **Use Cases** - Business logic encapsulation
- **StateFlow** - Reactive UI state management
- **Type Converters** - Room enum/date handling
- **AlarmManager** - Exact time notifications (prayers)

---

## 📁 Project Structure

```
GrindOS/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/grindos/app/
│           ├── MainActivity.kt              # Entry point
│           ├── GrindOSApp.kt                # Application class
│           │
│           ├── navigation/
│           │   ├── Screen.kt                # Route definitions
│           │   ├── AppNavGraph.kt           # Navigation graph
│           │   └── BottomNavBar.kt          # Bottom navigation
│           │
│           ├── data/
│           │   ├── local/
│           │   │   ├── GrindOsDatabase.kt   # Room database
│           │   │   ├── dao/                 # 9 DAOs (Task, Prayer, Quran, etc.)
│           │   │   ├── entity/              # 9 Room entities
│           │   │   └── converter/           # Type converters
│           │   ├── repository/              # 10 repositories
│           │   └── datastore/               # Settings DataStore
│           │
│           ├── domain/
│           │   ├── model/                   # 10 domain models + enums
│           │   └── usecase/                 # 4 use cases
│           │
│           ├── ui/
│           │   ├── home/                    # Dashboard screen + ViewModel
│           │   ├── study/                   # Study, ErrorLog, SprintTimer screens
│           │   ├── deen/                    # Prayer, Quran screens
│           │   ├── life/                    # Hackathon, Books, Golf screens
│           │   ├── settings/                # Settings screen
│           │   ├── components/              # Reusable UI components
│           │   └── theme/                   # Colors, typography, theme
│           │
│           ├── notifications/
│           │   ├── NotificationChannels.kt  # 5 notification channels
│           │   ├── NotificationReceiver.kt  # BroadcastReceiver
│           │   ├── NotificationScheduler.kt # AlarmManager integration
│           │   └── BootReceiver.kt          # Reschedule on boot
│           │
│           ├── di/
│           │   └── AppModule.kt             # Hilt DI module
│           │
│           └── util/
│               ├── DateTimeUtils.kt         # Date/time formatting
│               └── XpCalculator.kt          # XP/level system
│
├── gradle/
│   └── libs.versions.toml                   # Version catalog
├── build.gradle.kts                         # Project-level build
├── settings.gradle.kts                      # Project settings
└── gradle.properties                        # Gradle config
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Quail 1 (2026.1.1) or Panda 4 (2025.3.4) or later
- **JDK**: 17 or higher
- **Android SDK**: API 35 (compileSdk), minSdk 26
- **Gradle**: 8.11.1 (wrapper included)

### Setup Instructions

1. **Clone/Open the project**
   ```bash
   cd GrindOS
   ```

2. **Sync Gradle**
   - Open in Android Studio
   - Click "Sync Now" when prompted
   - Or run: `./gradlew build`

3. **Run the app**
   - Connect an Android device (API 26+) or start an emulator
   - Click Run ▶️ in Android Studio
   - Or run: `./gradlew installDebug`

4. **Build APK**
   ```bash
   ./gradlew assembleDebug
   ```
   APK location: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📋 Implementation Plan

### Sprint 1: Project Setup ✅
- [x] Gradle build system with version catalog
- [x] Jetpack Compose navigation
- [x] Dark theme UI
- [x] Bottom navigation (5 tabs)
- [x] Room database setup

### Sprint 2: Data Layer ✅
- [x] 9 Room entities with type converters
- [x] 9 DAOs with Flow queries
- [x] 10 repositories
- [x] DataStore for settings

### Sprint 3: Domain Layer ✅
- [x] Domain models
- [x] Use cases (tasks, XP, streaks, panic)
- [x] Hilt dependency injection

### Sprint 4: UI Components ✅
- [x] GrindCard, TaskItem, PrayerStatusRow
- [x] PanicButton, XpStreakCard, SprintTimerCard
- [x] Theme system (colors, typography)

### Sprint 5: Screens & ViewModels ✅
- [x] Home dashboard with all cards
- [x] Study module (topics, error log, sprint timer)
- [x] Deen module (prayer tracker, Quran tracker)
- [x] Life module (hackathon, books, golf)
- [x] Settings screen

### Sprint 6: Notifications ✅
- [x] 5 notification channels
- [x] AlarmManager integration
- [x] NotificationReceiver
- [x] Boot receiver for rescheduling

### Sprint 7: Polish & Testing
- [ ] Unit tests for repositories
- [ ] UI tests for screens
- [ ] Performance optimization
- [ ] ProGuard rules refinement

### Sprint 8: Release
- [ ] Release build configuration
- [ ] APK signing
- [ ] Documentation
- [ ] Beta testing

---

## 🎨 Design System

### Color Palette
- **Background**: Dark navy/black (#0A0E1A)
- **Primary**: Electric blue (#2979FF)
- **Accent**: Neon green (#00E676), Orange (#FF6D00)
- **Deen**: Emerald green (#00C853)
- **Status**: Success green, Warning orange, Danger red

### Typography
- **Display**: Bold, large headlines
- **Title**: Semi-bold, medium size
- **Body**: Regular, readable
- **Label**: Medium, small caps

### UI Principles
- **No clutter** - Only show what matters today
- **Dark mode** - Focused, gamer/productivity feel
- **ADHD-friendly** - Clear hierarchy, small wins
- **No guilt** - Recovery-focused messaging

---

## 🔐 Permissions

```xml
POST_NOTIFICATIONS      - Custom reminders (Android 13+)
SCHEDULE_EXACT_ALARM  - Prayer/exact time reminders
USE_EXACT_ALARM       - Alternative for exact alarms
RECEIVE_BOOT_COMPLETED  - Reschedule alarms after reboot
VIBRATE                 - Notification vibration
WAKE_LOCK               - WorkManager background tasks
```

---

## 📊 Database Schema

### Tables (9 entities)
1. **tasks** - Daily tasks with categories, priorities, reminders
2. **prayers** - 5 daily prayers with completion status
3. **quran_entries** - Daily Quran reading progress
4. **study_topics** - FAST roadmap topics with status
5. **error_logs** - Mistake tracking with analysis
6. **sprint_sessions** - Study timer sessions
7. **hackathon_tasks** - Kanban board tasks
8. **book_entries** - Reading habit tracking
9. **golf_entries** - Practice session logging

---

## 🎮 Gamification System

### XP Rewards
- Task complete: +10 XP (high priority: +15)
- Prayer on time: +15 XP
- Quran target met: +10 XP
- Sprint complete: +5 to +30 XP (based on duration)
- Error fixed: +10 XP
- Hackathon task: +20 XP

### Levels
1. Rookie (0 XP)
2. Getting Started (100 XP)
3. Locked In (300 XP)
4. Demon Mode (600 XP)
5. Scholarship Hunter (1000 XP)
6. FAST Warrior (2000 XP)
7. Unstoppable (3500 XP)
8. Legend (5000 XP)
9. GrindOS Master (8000 XP)

### Streaks
- Daily activity tracking
- Streak bonuses (+5 to +50 XP)
- Visual streak counter with fire emoji 🔥

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Manual Testing Checklist
- [ ] Task CRUD operations
- [ ] Prayer completion toggling
- [ ] Quran page tracking
- [ ] Sprint timer functionality
- [ ] Notification scheduling
- [ ] XP/level progression
- [ ] Data persistence across app restarts

---

## 📱 Minimum Requirements

- **Android**: 8.0 (API 26) or higher
- **Storage**: ~50 MB for app + database
- **RAM**: 2 GB recommended
- **Screen**: 5" minimum, optimized for 6"+

---

## 🔮 Future Enhancements

### Phase 2
- [ ] Prayer time API integration (automatic times)
- [ ] Cloud sync with Firebase
- [ ] Widget for home screen
- [ ] Voice reminders
- [ ] AI-generated daily plans

### Phase 3
- [ ] Mock test analytics
- [ ] Weak topic identification
- [ ] Smart rescheduling
- [ ] Social features (study groups)
- [ ] Wear OS companion app

---

## 📄 License

This is a personal project built for FAST 2027 prep and personal productivity.

---

## 🙏 Acknowledgments

- **Jetpack Compose** - Modern Android UI toolkit
- **Room** - Local database abstraction
- **Hilt** - Dependency injection made simple
- **Material Design 3** - Design system
- **Android Architecture Components** - MVVM best practices

---

## 💬 Philosophy

> "GrindOS should not be a guilt machine.  
> It should be a comeback machine.  
> If you fall off, the app says:  
> **No shame spiral. Pick one task. Restart now.** 🔥"

---

## 📞 Support

For issues, questions, or feature requests:
- Check the implementation plan above
- Review the build spec in `uploads/GrindOS_Android_Build_Spec.md`
- Refer to the PRD in `uploads/FAST_2027_App_Idea_Blueprint.md`

---

**Built with ❤️ for FAST 2027 success**

*Last updated: July 8, 2026*
