package com.uniandes.vinilosapp.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vinilosapp.views.album.Navigation
import com.uniandes.vinilosapp.views.album.AlbumsScreen
//import com.uniandes.vinilosapp.views.ui.theme.VinilosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }
}
