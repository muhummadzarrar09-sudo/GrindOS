package com.grindos.app.domain.usecase

import com.grindos.app.util.XpCalculator
import javax.inject.Inject

class CalculateXpUseCase @Inject constructor() {
    fun getLevel(totalXp: Int): String = XpCalculator.getLevelForXp(totalXp)
    fun getProgress(totalXp: Int): Float = XpCalculator.getLevelProgress(totalXp)
    fun getNextThreshold(totalXp: Int): Int = XpCalculator.getNextLevelThreshold(totalXp)
}
