package com.example.video_game_collections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.video_game_collections.ui.theme.Video_Game_CollectionsTheme


import com.example.video_game_collections.Screens.BottomNavBar
import com.example.video_game_collections.Screens.navUtitlity
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.imageViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.allViewModels.ordersViewModel



class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myViewModel : fireBaseAuthViewModel by viewModels()
        val fireStoreViewModel : fireStoreViewModel by viewModels()
        val ui_viewModel : UI_ViewModel by viewModels()
        val locationViewModel : locationViewModel by viewModels()
        val imageViewModel : imageViewModel by viewModels()
        val ordersViewModel : ordersViewModel by viewModels()
        val BottomNavBar = BottomNavBar()


        enableEdgeToEdge()
        setContent {
            var navController = rememberNavController()

            Video_Game_CollectionsTheme (){
                val observedLoginStatus = myViewModel.loginStatusState.observeAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar.SelectNavBar(
                            myViewModel = myViewModel,
                            observedLoginStatus = observedLoginStatus,
                            navController = navController,
                            viewModel = ui_viewModel
                        )
                    }
                ) { innerPadding ->

                    navUtitlity(
                        myViewModel,
                        fireStoreViewModel,
                        ui_viewModel,
                        locationViewModel,
                        ordersViewModel = ordersViewModel,
                        imageViewModel = imageViewModel,
                        modifier = Modifier.padding( innerPadding),
                        navController = navController
                    )

                }
            }
        }
    }
}

