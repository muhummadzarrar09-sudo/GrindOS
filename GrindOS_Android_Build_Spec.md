# GrindOS Native Android Build Spec ⚡📱

**App Name:** GrindOS  
**Platform:** Android only  
**Tech:** Kotlin + Gradle + Jetpack Compose  
**Purpose:** Personal operating system for FAST 2027 prep, prayers, Quran, books, golf, hackathon tasks, custom reminders, ADHD-friendly routine.

---

## 1. Recommended Android Stack

| Layer | Choice | Why |
|---|---|---|
| Language | Kotlin | Modern Android standard |
| Build | Gradle Kotlin DSL | Clean Android setup |
| UI | Jetpack Compose | Fast modern UI |
| Architecture | MVVM | Simple and scalable |
| Local DB | Room | Stores tasks, habits, error log, topics |
| Preferences | DataStore | Settings, notification mode, daily targets |
| Background work | WorkManager | Reliable scheduled background tasks |
| Exact alarms | AlarmManager | For prayer/custom time reminders where exact timing matters |
| Notifications | Android NotificationManager | Custom reminder channels |
| Navigation | Navigation Compose | Screen navigation |
| Date/time | java.time | Scheduling and streaks |

---

## 2. MVP Goal

Build the smallest usable version first.

### MVP Features

1. Dashboard screen
2. Daily task list
3. Add/edit/delete task
4. Custom notifications for tasks
5. Prayer tracker with manual prayer times first
6. Quran tracker
7. FAST roadmap topic tracker
8. Study sprint timer
9. Error log
10. Streak/XP basics
11. Settings screen for notification tone/mode

---

## 3. App Navigation

Bottom navigation tabs:

| Tab | Screens |
|---|---|
| Home | Dashboard, Today Mission |
| Study | FAST roadmap, topics, formula/error log |
| Deen | Prayer tracker, Quran tracker |
| Life | Books, golf, hackathon tasks |
| Settings | Notification mode, daily targets, theme |

---

## 4. Core Screens

## 4.1 Home Dashboard

Shows only today’s important stuff.

### Components

- Greeting: “Today’s Mission”
- Top 3 priority tasks
- Next upcoming reminder
- Prayer status row: Fajr, Dhuhr, Asr, Maghrib, Isha
- Study sprint card
- Quran progress card
- Hackathon deadline card
- Panic button
- XP/streak card

### Panic Button Behavior

When tapped:

1. Shows calming message
2. Suggests one tiny next task
3. Starts 5-minute or 25-minute timer

Example text:

> Breathe bro. One task. 25 minutes. We move.

---

## 4.2 Task Scheduler

### Task Fields

| Field | Type |
|---|---|
| id | Long |
| title | String |
| description | String? |
| category | Study / Prayer / Quran / Hackathon / Book / Golf / Personal |
| priority | 1, 2, 3 |
| date | LocalDate |
| time | LocalTime? |
| durationMinutes | Int? |
| isCompleted | Boolean |
| reminderEnabled | Boolean |
| notificationTone | Gentle / Hype / Roast / DeenFirst / ExamWar |
| repeatRule | None / Daily / Weekly |

---

## 4.3 Custom Notification System

### Notification Modes

| Mode | Example |
|---|---|
| Gentle | Time for your study sprint. Small win today. |
| Hype | BRO LOCK IN. Study sprint starts now 🔥 |
| Roast | Scholarship won’t spawn while you scroll reels 💀 |
| DeenFirst | Salah first. Everything else gets barakah after 🤲 |
| ExamWar | 40-sec MCQ mode. FAST is watching 🎯 |

### Notification Channels

Create separate Android channels:

| Channel | Purpose |
|---|---|
| Prayer Reminders | Salah reminders |
| Study Reminders | FAST prep reminders |
| Habit Reminders | Quran/books/golf |
| Hackathon Reminders | Project deadlines/tasks |
| Panic Reset | Recovery/reset notifications |

### Android Permissions

For Android 13+:

- POST_NOTIFICATIONS permission required.

For exact alarms:

- SCHEDULE_EXACT_ALARM may be needed depending Android version/device.

### Implementation Note

Use:

- WorkManager for normal reminders
- AlarmManager for exact time-sensitive reminders like prayer times

---

## 4.4 Prayer Tracker

### MVP Version
Manual prayer times first. Later add prayer time calculation/API.

### Prayer Fields

| Field | Type |
|---|---|
| prayerName | Fajr / Dhuhr / Asr / Maghrib / Isha |
| date | LocalDate |
| time | LocalTime |
| completed | Boolean |
| reminderMinutesBefore | Int |

### Features

- Checklist for 5 prayers
- Set prayer times manually
- Reminder 10/15/20 min before
- Jummah reminder on Friday
- No guilt wording if missed, just recover

---

## 4.5 Quran Tracker

### Fields

| Field | Type |
|---|---|
| date | LocalDate |
| targetPages | Int |
| pagesRead | Int |
| notes | String? |

### Features

- Daily pages target
- Mark pages read
- Streak tracking
- Optional reflection note

---

## 4.6 FAST Study Module

### Topic Status

| Status | Meaning |
|---|---|
| NotStarted | Not touched yet |
| Learning | Concept stage |
| Practicing | MCQ practice |
| Timed | Timed drills |
| Mastered | Revision loop only |

### Topic Fields

| Field | Type |
|---|---|
| id | Long |
| section | BasicMath / AdvancedMath / IQ / Analytical / English |
| name | String |
| status | TopicStatus |
| accuracy | Float? |
| notes | String? |

### Default Roadmap Order

1. Basic Math
2. Advanced Math
3. IQ
4. Analytical
5. English
6. Mixed mocks

---

## 4.7 Sprint Timer

### Modes

| Mode | Duration |
|---|---:|
| Mini Reset | 5 min |
| Focus Sprint | 25 min |
| Deep Work | 50 min |
| MCQ Speed Drill | Custom, e.g. 20 min |

### Timer Features

- Start/pause/reset
- Link timer to task/topic
- Completion gives XP

---

## 4.8 Error Log

### Fields

| Field | Type |
|---|---|
| id | Long |
| date | LocalDate |
| section | String |
| topic | String |
| question | String |
| mistakeType | Concept / Formula / Calculation / Time / Panic / Silly |
| whyItHappened | String |
| correctMethod | String |
| shortcut | String? |
| redoDate | LocalDate? |
| status | Open / Fixed |

---

## 4.9 Hackathon Module

### Simple Kanban

Columns:

- Backlog
- Doing
- Done

### Hackathon Task Fields

| Field | Type |
|---|---|
| title | String |
| description | String? |
| deadline | LocalDateTime? |
| status | Backlog / Doing / Done |
| priority | Int |

---

## 4.10 Books + Golf

Keep this simple.

### Books

- Book name
- Pages read today
- Daily target
- Notes

### Golf

- Practice date
- Drill name
- Duration
- Notes

---

## 5. Room Database Entities

Suggested entities:

1. TaskEntity
2. PrayerEntity
3. QuranEntryEntity
4. StudyTopicEntity
5. ErrorLogEntity
6. SprintSessionEntity
7. HackathonTaskEntity
8. BookEntryEntity
9. GolfEntryEntity
9. AppSettingsEntity or DataStore settings

---

## 6. Suggested Project Structure

```text
app/
  src/main/java/com/grindos/app/
    MainActivity.kt
    GrindOSApp.kt
    navigation/
      AppNavGraph.kt
      BottomNavBar.kt
    data/
      local/
        GrindOsDatabase.kt
        dao/
        entity/
      repository/
    domain/
      model/
      usecase/
    ui/
      home/
      study/
      deen/
      life/
      settings/
      components/
      theme/
    notifications/
      NotificationScheduler.kt
      NotificationReceiver.kt
      NotificationChannels.kt
    util/
      DateTimeUtils.kt
      XpCalculator.kt
```

---

## 7. Design Style

### Vibe

Dark mode, focused, clean, gamer/productivity feel.

### Colors

| Role | Color Idea |
|---|---|
| Background | Very dark navy/black |
| Primary | Electric blue |
| Accent | Neon green or orange |
| Deen cards | Emerald green |
| Danger/urgent | Red/orange |
| Completed | Green |

### UI Rule

No clutter. Dashboard should show only what matters today.

---

## 8. Build Sprints

| Sprint | Build |
|---|---|
| Sprint 1 | Project setup + Compose navigation + dashboard dummy UI |
| Sprint 2 | Room database + task CRUD |
| Sprint 3 | Notifications + notification modes |
| Sprint 4 | Prayer tracker manual times |
| Sprint 5 | Quran tracker + streaks |
| Sprint 6 | FAST roadmap + topic tracker |
| Sprint 7 | Sprint timer + XP |
| Sprint 8 | Error log |
| Sprint 9 | Hackathon/books/golf modules |
| Sprint 10 | Polish + APK build |

---

## 9. First Version Acceptance Criteria

The app is usable if:

- User can open dashboard
- User can add tasks for today
- User can receive custom notification reminders
- User can mark prayers complete
- User can track Quran pages
- User can see FAST roadmap topics
- User can run a study timer
- User can add mistakes to error log
- Data persists after app closes

---

## 10. Prompt For AI Builder

Copy/paste this into the coding AI:

```text
Build a native Android app named GrindOS using Kotlin, Gradle, Jetpack Compose, Room, DataStore, MVVM, Navigation Compose, and Android notifications.

The app is a personal command center for FAST 2027 prep, prayer tracking, Quran reading, hackathon tasks, books, golf, custom reminders, ADHD-friendly daily planning, sprint timers, and error logging.

Start with MVP only:
1. Home dashboard with Today's Mission, Top 3 tasks, prayer status, study sprint card, Quran card, streak/XP card, panic button.
2. Task CRUD with category, priority, date, time, duration, completion, reminder toggle, notification tone, repeat rule.
3. Local notifications with modes: Gentle, Hype, Roast, DeenFirst, ExamWar.
4. Prayer tracker with manual prayer times and checklist for Fajr, Dhuhr, Asr, Maghrib, Isha.
5. Quran tracker with daily target pages, pages read, notes, streak.
6. FAST roadmap topic tracker with sections: Basic Math, Advanced Math, IQ, Analytical, English.
7. Sprint timer with 5, 25, 50 minute modes and custom MCQ mode.
8. Error log with mistake type, why it happened, correct method, shortcut, redo date, status.
9. Settings screen for theme, notification mode, default reminder minutes.

Use a clean dark Jetpack Compose UI. Follow MVVM architecture. Persist data locally using Room and settings using DataStore. Create separate notification channels for Prayer, Study, Habit, Hackathon, and Panic Reset. Make the first version compile and run as an APK.
```

---

## 11. Important Android Notes

- Android 13+ needs notification permission.
- Some phones kill background tasks aggressively; exact reminders may need AlarmManager and battery optimization warning.
- For prayer times, start manual first, then add calculation/API later.
- APK sideloading is okay for personal use, but users must allow install from unknown sources.

---

# Final Philosophy

GrindOS should not be a guilt machine.

It should be a comeback machine.

If the user falls off, the app says:

> No shame spiral. Pick one task. Restart now.

🔥
