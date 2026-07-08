package com.grindos.app.domain.usecase

import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor() {
    fun calculateStreak(consecutiveDays: Int): Int = consecutiveDays

    fun getStreakLabel(streak: Int): String = when {
        streak == 0 -> "No streak yet"
        streak == 1 -> "Day 1 — let's build"
        streak < 7 -> "$streak days — keep going"
        streak < 14 -> "$streak days — week warrior"
        streak < 30 -> "$streak days — locked in"
        streak < 60 -> "$streak days — demon mode"
        streak < 100 -> "$streak days — unstoppable"
        else -> "$streak days — LEGENDARY 🔥"
    }

    fun getStreakEmoji(streak: Int): String = when {
        streak == 0 -> "💤"
        streak < 3 -> "🌱"
        streak < 7 -> "🔥"
        streak < 14 -> "⚡"
        streak < 30 -> "💎"
        else -> "👑"
    }
}
