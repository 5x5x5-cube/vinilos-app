package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorDetailTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigateToCollectorDetail_andShowEmail() {
        composeTestRule.onNodeWithText("Coleccionistas", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Email:", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodes(hasText("Email:", substring = true)).onFirst().assertIsDisplayed()
    }

    @Test
    fun navigateToCollectorDetail_andGoBackToList() {
        composeTestRule.onNodeWithText("Coleccionistas", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Email:", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().assertIsDisplayed()
    }
}
