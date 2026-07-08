package com.grindos.app.domain.usecase

import com.grindos.app.domain.model.SprintMode
import javax.inject.Inject

class GetPanicSuggestionUseCase @Inject constructor() {

    data class PanicResult(
        val calmingMessage: String,
        val suggestedTask: String,
        val suggestedTimer: SprintMode
    )

    private val calmingMessages = listOf(
        "Breathe bro. One task. 25 minutes. We move.",
        "No shame spiral. Pick one task. Restart now.",
        "Reset. Refocus. One small win is all you need.",
        "You're not behind. You're recalibrating. Lock in.",
        "Chaos is temporary. One action changes everything."
    )

    private val tinyTasks = listOf(
        "Do 1 page of Quran",
        "Solve 1 math problem",
        "Read for 5 minutes",
        "Write down your top 3 tasks",
        "Do a 5-minute breathing exercise",
        "Review 1 error from your log"
    )

    operator fun invoke(): PanicResult {
        return PanicResult(
            calmingMessage = calmingMessages.random(),
            suggestedTask = tinyTasks.random(),
            suggestedTimer = SprintMode.MINI_RESET
        )
    }
}
