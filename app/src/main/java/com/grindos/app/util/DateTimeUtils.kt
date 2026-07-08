package com.grindos.app.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object DateTimeUtils {

    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    private val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")
    private val fullDateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")

    fun formatTime(time: LocalTime): String = time.format(timeFormatter)

    fun formatDate(date: LocalDate): String = date.format(dateFormatter)

    fun formatFullDate(date: LocalDate): String = date.format(fullDateFormatter)

    fun getToday(): LocalDate = LocalDate.now()

    fun isToday(date: LocalDate): Boolean = date == LocalDate.now()

    fun isTomorrow(date: LocalDate): Boolean = date == LocalDate.now().plusDays(1)

    fun isYesterday(date: LocalDate): Boolean = date == LocalDate.now().minusDays(1)

    fun getRelativeDateLabel(date: LocalDate): String = when {
        isToday(date) -> "Today"
        isTomorrow(date) -> "Tomorrow"
        isYesterday(date) -> "Yesterday"
        else -> formatDate(date)
    }

    fun isFriday(date: LocalDate): Boolean = date.dayOfWeek == DayOfWeek.FRIDAY

    fun getNextFriday(): LocalDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY))

    fun daysBetween(start: LocalDate, end: LocalDate): Long =
        ChronoUnit.DAYS.between(start, end)

    fun getStartOfWeek(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    fun getEndOfWeek(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    fun getGreeting(): String {
        val hour = LocalTime.now().hour
        return when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            hour < 21 -> "Good Evening"
            else -> "Good Night"
        }
    }
}
