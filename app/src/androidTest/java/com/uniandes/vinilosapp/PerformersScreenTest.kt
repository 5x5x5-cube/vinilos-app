package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PerformersScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun performersList_hasAtLeastOneArtistVisible() {
        composeTestRule.onNodeWithContentDescription("Artists", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Ver")).fetchSemanticsNodes().isNotEmpty()
        }

        assert(
            composeTestRule.onAllNodes(hasText("Ver")).fetchSemanticsNodes().isNotEmpty()
        ) { "No se encontró ningún artista visible con botón Ver" }
    }

    @Test
    fun performersList_displaysArtistNameAndButton() {
        composeTestRule.onNodeWithContentDescription("Artists", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Ver")).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodes(hasText("Ver")).onFirst().assertIsDisplayed()

        composeTestRule.onAllNodes(hasText(" ", substring = true)).onFirst().assertIsDisplayed()
    }

    @Test
    fun performersList_hasMultipleVerButtons() {
        composeTestRule.onNodeWithContentDescription("Artists", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(hasText("Ver")).fetchSemanticsNodes().size >= 2
        }

        val count = composeTestRule.onAllNodes(hasText("Ver")).fetchSemanticsNodes().size
        assert(count > 1) { "Se esperaban múltiples botones 'Ver', pero se encontró solo $count" }
    }
}