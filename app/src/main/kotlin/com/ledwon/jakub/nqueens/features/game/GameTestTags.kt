package com.ledwon.jakub.nqueens.features.game

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.ledwon.jakub.nqueens.features.game.GameTestTags.HasConflictKey

object GameTestTags {
    const val QUEEN = "queen"
    val HasConflictKey = SemanticsPropertyKey<Boolean>("hasConflict")
}

var SemanticsPropertyReceiver.hasConflict by HasConflictKey
