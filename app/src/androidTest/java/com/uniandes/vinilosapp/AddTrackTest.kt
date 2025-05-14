package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTrackTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addTrackToAlbum_andReturnToDetail() {
        composeTestRule.onNodeWithContentDescription("Albums", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Tracks").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Añadir track").performClick()

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Nombre").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Nombre").performTextInput("Track de prueba")
        composeTestRule.onNodeWithText("Duración").performTextInput("4:20")

        composeTestRule.onNodeWithText("Guardar").performScrollTo().performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Tracks").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Tracks").assertIsDisplayed()
    }

    @Test
    fun cancelAddTrack_shouldReturnToAlbumDetail() {
        composeTestRule.onNodeWithContentDescription("Albums", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Tracks").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Añadir track").performClick()

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Nombre").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Cancelar").performScrollTo().performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Tracks").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Tracks").assertIsDisplayed()
    }
}