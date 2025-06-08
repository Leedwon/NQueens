package com.ledwon.jakub.nqueens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule

abstract class BaseTestCase {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    open fun setUp() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("nqueens.db")
        hiltRule.inject()
    }
}
