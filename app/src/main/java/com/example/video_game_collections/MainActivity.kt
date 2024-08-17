package com.example.video_game_collections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.video_game_collections.Screens.navUtitlity
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.imageViewModel
import com.example.video_game_collections.allViewModels.locationViewModel

import com.example.video_game_collections.ui.theme.Video_Game_CollectionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myViewModel : fireBaseAuthViewModel by viewModels()
        val fireStoreViewModel : fireStoreViewModel by viewModels()
        val ui_viewModel : UI_ViewModel by viewModels()
        val locationViewModel : locationViewModel by viewModels()
        val imageViewModel : imageViewModel by viewModels()


        enableEdgeToEdge()
        setContent {
            Video_Game_CollectionsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    navUtitlity(
                        myViewModel,
                        fireStoreViewModel,
                        ui_viewModel,
                        locationViewModel,
                        imageViewModel = imageViewModel
                    )

                }
            }
        }
    }
}

