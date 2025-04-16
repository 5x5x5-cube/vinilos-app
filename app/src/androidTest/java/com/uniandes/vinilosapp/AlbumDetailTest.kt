package com.uniandes.vinilosapp

import androidx.compose.material3.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.models.Track
import com.uniandes.vinilosapp.views.album.AlbumDetailContent
import org.junit.Rule
import org.junit.Test
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.get
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.NavigatorProvider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule

fun createTestNavGraph(navController: TestNavHostController): NavGraph {
    val navGraph = navController.createGraph(startDestination = "albumList") {
        composable("albumList") {}
        composable("albumDetail") {}
    }
    return navGraph
}

class AlbumDetailScreenTest {

    // Álbum de prueba fijo (lo que devuelve normalmente el backend)
    private val fakeAlbum = AlbumDetails(
        albumId = 1,
        name = "Buscando América",
        cover = "https://fakeimage.com/cover",
        releaseDate = "01 de agosto de 1984",
        description = "Buscando América es el primer álbum de Rubén Blades y Seis del Solar lanzado en 1984.",
        genre = "Salsa",
        recordLabel = "Elektra",
        performers = listOf(
            Performer(
                performerID = 1,
                name = "Rubén Blades Bellido de Luna",
                image = "https://fakeimage.com/ruben",
                description = "Cantante panameño de salsa y política"
            )
        ),
        tracks = listOf(
            Track(trackId = 1, name = "Decisiones", duration = "5:05"),
            Track(trackId = 2, name = "Desapariciones", duration = "6:29")
        )
    )

    @Test
    fun albumTitleIsDisplayed() {
        composeTestRule.setContent {
            AlbumDetailScreenPreview(fakeAlbum)
        }

        composeTestRule.onNodeWithText("Buscando América").assertIsDisplayed()
    }

    @Test
    fun albumDescriptionIsDisplayed() {
        composeTestRule.setContent {
            AlbumDetailScreenPreview(fakeAlbum)
        }

        composeTestRule.onNodeWithText("Buscando América es el primer álbum", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun albumTracksAreDisplayed() {
        composeTestRule.setContent {
            AlbumDetailScreenPreview(fakeAlbum)
        }

        composeTestRule.onNodeWithText("Decisiones")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Desapariciones")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun goBackButtonTriggersNavigation() {
        val navController = TestNavHostController(composeTestRule.activity)

        composeTestRule.setContent {
            navController.navigatorProvider.addNavigator(
                androidx.navigation.compose.ComposeNavigator()
            )

            val graph = navController.createGraph(startDestination = "albumList") {
                composable("albumList") {}
                composable("albumDetail") {}
            }
            navController.graph = graph

            navController.navigate("albumDetail")

            AlbumDetailNavTestScreen(fakeAlbum, navController)
        }

        composeTestRule
            .onNodeWithContentDescription("Go back")
            .performClick()

        composeTestRule.waitForIdle()
        assert(navController.currentDestination?.route == "albumList")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreenPreview(album: AlbumDetails) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(album.name) })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AlbumDetailContent(album)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailNavTestScreen(album: AlbumDetails, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(album.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AlbumDetailContent(album)
        }
    }
}