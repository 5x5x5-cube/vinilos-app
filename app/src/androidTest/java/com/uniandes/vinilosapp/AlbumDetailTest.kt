package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumDetailTest {

    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigateToAlbumDetail_andShowTitle() {

        composeTestRule.waitUntil(timeoutMillis = 18000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        composeTestRule.waitUntil(timeoutMillis = 18000) {
            composeTestRule
                    .onAllNodes(hasText("Buscando América", substring = true))
                    .fetchSemanticsNodes()
                    .isNotEmpty()
        }

        composeTestRule
                .onAllNodesWithText("Buscando América", substring = true)
                .onFirst()
                .assertIsDisplayed()
    }

    @Test
    fun albumDetail_displaysTrack() {
        val longerTimeout = 30000L

        composeTestRule.waitUntil(timeoutMillis = longerTimeout) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        // First ensure we've navigated to the right album
        composeTestRule.waitUntil(timeoutMillis = longerTimeout) {
            composeTestRule
                    .onAllNodes(hasText("Buscando América", substring = true))
                    .fetchSemanticsNodes()
                    .isNotEmpty()
        }

        // Then look for tracks section
        composeTestRule.waitUntil(timeoutMillis = longerTimeout) {
            composeTestRule
                    .onAllNodes(hasText("Decisiones", substring = true))
                    .fetchSemanticsNodes()
                    .isNotEmpty()
        }

        // Finally check for specific track
        composeTestRule.onNodeWithText("Decisiones").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun albumDetail_goBackToList() {
        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule
                    .onAllNodes(hasText("Descripción", substring = true))
                    .fetchSemanticsNodes()
                    .isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().assertIsDisplayed()
    }
}
