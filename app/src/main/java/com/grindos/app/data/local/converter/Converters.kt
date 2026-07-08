package com.grindos.app.data.local.converter

import androidx.room.TypeConverter
import com.grindos.app.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Converters {

    // LocalDate
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateStr: String?): LocalDate? = dateStr?.let { LocalDate.parse(it) }

    // LocalTime
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun toLocalTime(timeStr: String?): LocalTime? = timeStr?.let { LocalTime.parse(it) }

    // LocalDateTime
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @TypeConverter
    fun toLocalDateTime(dateTimeStr: String?): LocalDateTime? = dateTimeStr?.let { LocalDateTime.parse(it) }

    // TaskCategory
    @TypeConverter
    fun fromTaskCategory(category: TaskCategory): String = category.name

    @TypeConverter
    fun toTaskCategory(name: String): TaskCategory = TaskCategory.valueOf(name)

    // TaskPriority
    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): Int = priority.level

    @TypeConverter
    fun toTaskPriority(level: Int): TaskPriority = TaskPriority.entries.first { it.level == level }

    // NotificationTone
    @TypeConverter
    fun fromNotificationTone(tone: NotificationTone): String = tone.name

    @TypeConverter
    fun toNotificationTone(name: String): NotificationTone = NotificationTone.valueOf(name)

    // RepeatRule
    @TypeConverter
    fun fromRepeatRule(rule: RepeatRule): String = rule.name

    @TypeConverter
    fun toRepeatRule(name: String): RepeatRule = RepeatRule.valueOf(name)

    // PrayerName
    @TypeConverter
    fun fromPrayerName(name: PrayerName): String = name.name

    @TypeConverter
    fun toPrayerName(name: String): PrayerName = PrayerName.valueOf(name)

    // StudySection
    @TypeConverter
    fun fromStudySection(section: StudySection): String = section.name

    @TypeConverter
    fun toStudySection(name: String): StudySection = StudySection.valueOf(name)

    // TopicStatus
    @TypeConverter
    fun fromTopicStatus(status: TopicStatus): String = status.name

    @TypeConverter
    fun toTopicStatus(name: String): TopicStatus = TopicStatus.valueOf(name)

    // MistakeType
    @TypeConverter
    fun fromMistakeType(type: MistakeType): String = type.name

    @TypeConverter
    fun toMistakeType(name: String): MistakeType = MistakeType.valueOf(name)

    // ErrorStatus
    @TypeConverter
    fun fromErrorStatus(status: ErrorStatus): String = status.name

    @TypeConverter
    fun toErrorStatus(name: String): ErrorStatus = ErrorStatus.valueOf(name)

    // SprintMode
    @TypeConverter
    fun fromSprintMode(mode: SprintMode): String = mode.name

    @TypeConverter
    fun toSprintMode(name: String): SprintMode = SprintMode.valueOf(name)

    // KanbanStatus
    @TypeConverter
    fun fromKanbanStatus(status: KanbanStatus): String = status.name

    @TypeConverter
    fun toKanbanStatus(name: String): KanbanStatus = KanbanStatus.valueOf(name)
}
