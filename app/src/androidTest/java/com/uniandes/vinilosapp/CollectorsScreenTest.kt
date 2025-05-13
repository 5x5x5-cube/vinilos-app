package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun collectorsList_hasAtLeastOneCollectorVisible() {
        composeTestRule.onNodeWithContentDescription("Collectors", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Tel:", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        assert(
            composeTestRule.onAllNodes(hasText("Tel:", substring = true)).fetchSemanticsNodes().isNotEmpty()
        ) { "No se encontró ningún colector visible con número telefónico" }
    }

    @Test
    fun collectorsList_showsNamePhoneEmail() {
        composeTestRule.onNodeWithContentDescription("Collectors", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Tel:", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodes(hasText("Tel:", substring = true)).onFirst().assertIsDisplayed()
        composeTestRule.onAllNodes(hasText("Email:", substring = true)).onFirst().assertIsDisplayed()
    }

    @Test
    fun collectorsList_hasVerButton() {
        composeTestRule.onNodeWithContentDescription("Collectors", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithText("Ver").assertAny(hasText("Ver"))
    }
}