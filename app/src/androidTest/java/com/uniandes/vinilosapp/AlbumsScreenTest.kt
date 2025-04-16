package com.uniandes.vinilosapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.views.album.AlbumRow
import org.junit.Rule
import org.junit.Test
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class AlbumsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val albums = listOf(
        Album(1, "Buscando América", "https://fakeimage.com/1", "1984-08-11", "Desc", "Salsa", "Elektra"),
        Album(2, "Poeta del pueblo", "https://fakeimage.com/2", "1980-06-10", "Desc", "Salsa", "Elektra"),
        Album(3, "A Night at the Opera", "https://fakeimage.com/3", "1975-11-21", "Desc", "Rock", "EMI"),
        Album(4, "A Day at the Races", "https://fakeimage.com/4", "1976-12-10", "Desc", "Rock", "EMI")
    )

    @Test
    fun albumBuscandoAmericaIsDisplayed() {
        composeTestRule.setContent {
            AlbumsScreenPreview(albums)
        }

        composeTestRule.onNodeWithText("Buscando América").assertIsDisplayed()
    }

    @Test
    fun albumListDisplaysMinimumOf4Albums() {
        composeTestRule.setContent {
            AlbumsScreenPreview(albums)
        }

        composeTestRule.onAllNodesWithText("Ver").assertCountEquals(4)
    }

    @Test
    fun eachAlbumRowShowsNameLabelAndButton() {
        composeTestRule.setContent {
            AlbumsScreenPreview(albums)
        }

        albums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertIsDisplayed()
            composeTestRule.onAllNodesWithText(album.recordLabel).onFirst().assertIsDisplayed()
        }
        composeTestRule.onAllNodesWithText("Ver").assertCountEquals(4)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreenPreview(albums: List<Album>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Álbumes") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(albums) { album ->
                AlbumRow(
                    album = album,
                    onVerClick = {}
                )
                Divider()
            }
        }
    }
}