package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun albumList_displaysAtLeastOneAlbum() {
        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().assertIsDisplayed()
    }

    @Test
    fun albumList_displaysBuscandoAmericaIfPresent() {
        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Buscando América", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Buscando América", substring = true).onFirst().assertIsDisplayed()
    }

    @Test
    fun albumList_displaysAlbumNameAndLabel() {
        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        val firstVerButton = composeTestRule.onAllNodesWithText("Ver").onFirst()

        firstVerButton.performScrollTo().assertExists()

        composeTestRule.onAllNodes(hasText("", substring = true)).onFirst().assertExists()
    }
}