package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vinilosapp.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateAlbumTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createAlbum_andGoBackToList() {
        composeTestRule.onNodeWithContentDescription("Albums", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Agregar álbum").performClick()

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Nombre").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Nombre").performTextInput("Test Álbum")
        composeTestRule.onNodeWithText("Descripción").performTextInput("Este es un álbum de prueba")
        composeTestRule.onNodeWithText("URL del Cover").performTextInput("https://fakeimage.com/album.jpg")

        composeTestRule.onNodeWithText("Selecciona un género").performClick()
        composeTestRule.onNodeWithText("Salsa").performClick()

        composeTestRule.onNodeWithText("Selecciona una disquera").performClick()
        composeTestRule.onNodeWithText("Elektra").performClick()

        composeTestRule.onNodeWithText("Guardar").performScrollTo().performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().assertIsDisplayed()
    }

    @Test
    fun cancelAlbumCreation_andReturnToList() {
        composeTestRule.onNodeWithContentDescription("Albums", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Agregar álbum").performClick()

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Nombre").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Cancelar").performScrollTo().performClick()

        composeTestRule.waitUntil(8000) {
            composeTestRule.onAllNodesWithText("Ver").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithText("Ver").onFirst().assertIsDisplayed()
    }
}