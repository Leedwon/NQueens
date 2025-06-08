package com.ledwon.jakub.nqueens.features.leaderboard

object LeaderboardTestTags {
    const val BOARD_SIZE_PICKER = "board_size_picker"
    const val BACK_BUTTON = "back_button"

    fun createBoardSizePickerItemTag(size: Int): String {
        return "board_size_picker_item_$size"
    }
}
