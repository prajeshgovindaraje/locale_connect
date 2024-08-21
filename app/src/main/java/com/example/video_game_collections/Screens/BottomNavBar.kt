package com.example.video_game_collections.Screens

import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.video_game_collections.R
import com.example.video_game_collections.ui.theme.GreenJC
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.loginStatus

class BottomNavBar {

    @Composable
    fun SelectNavBar(modifier: Modifier = Modifier,myViewModel: fireBaseAuthViewModel,observedLoginStatus: State<loginStatus?>,navController: NavHostController,viewModel: UI_ViewModel) {
        var roler =  remember{ mutableStateOf<String?>(null) }
        LaunchedEffect(key1 = observedLoginStatus.value) {
            when (observedLoginStatus.value) {
                is loginStatus.LoggedIn -> {
                    myViewModel.getUserRole { role ->
                        roler.value = role
                    }
                }
                else -> Unit
            }
        }
        when (observedLoginStatus.value) {
            //is loginStatus.Error -> TODO()
            is loginStatus.LoggedIn -> {
                if(roler.value== "customer"){
                    this.CustomerBNB(navController = navController, viewModel = viewModel)
                }
                if (roler.value=="seller"){
                    this.SellerBNB(navController = navController,viewModel = viewModel)
                }
            }

            else -> Unit
        }

    }


    @Composable
    fun CustomerBNB(modifier: Modifier = Modifier,navController: NavHostController,viewModel: UI_ViewModel) {
        var selectIndex by remember { mutableStateOf(viewModel.select) }
        BottomAppBar(
            containerColor = Color.Transparent,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        color = GreenJC,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                     viewModel.listNavItems.forEachIndexed { index, navItem ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(20f)
                                .clickable {
                                    selectIndex = index
                                    navController.navigate(navItem.page)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                if (selectIndex == index) Modifier.offset(y = (-8).dp) else Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    Modifier
                                        .background(
                                            if (selectIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            CircleShape
                                        )
                                        .size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(navItem.icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (selectIndex == index) Color.Black else Color.White
                                    )
                                }
                                AnimatedVisibility(selectIndex == index) {
                                    Text(
                                        text = navItem.title,
                                        modifier = Modifier.padding(top = 4.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }


                        }
                    }
                }

            }

        }

    }
    @Composable
    fun SellerBNB(modifier: Modifier = Modifier,navController: NavHostController, viewModel: UI_ViewModel) {

    }
}