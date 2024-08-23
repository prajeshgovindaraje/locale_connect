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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.video_game_collections.R
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
    fun CustomerBNB(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        viewModel: UI_ViewModel,

    ) {
        // Observe the current back stack entry
        val currentScreen by viewModel.currentScreen.collectAsState()

        val selectedItemIndexState by remember {
            derivedStateOf {
                viewModel.listNavItems.indexOfFirst { it.route == currentScreen }
                    .takeIf { it != -1 } ?: 0
            }
        }

        var selectedItemIndex by remember { mutableStateOf(selectedItemIndexState) }

        LaunchedEffect(currentScreen) {
            selectedItemIndex = selectedItemIndexState
        }

            Box(
                modifier = Modifier
                    .fillMaxWidth()

                    .height(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    viewModel.listNavItems.forEachIndexed { index, navItem ->
                        val isSelected = selectedItemIndex == index
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(5f)
                                .clickable {
                                    selectedItemIndex = index

                                    when (navItem.route) {
                                        is NavigationPages.customerPage -> navController.navigate(
                                            NavigationPages.customerPage
                                        )

                                        is NavigationPages.addToCartPage -> navController.navigate(
                                            NavigationPages.addToCartPage
                                        )

                                        is NavigationPages.myOrdersPage -> navController.navigate(
                                            NavigationPages.myOrdersPage
                                        )

                                        else -> Unit
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                if (isSelected) Modifier.offset(y = (-12).dp) else Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    Modifier
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                                            CircleShape
                                        )
                                        .size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(navItem.icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
                                    )
                                }
                                AnimatedVisibility(isSelected) {
                                    Text(
                                        text = navItem.title,
                                        modifier = Modifier.padding(top = 4.dp),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
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
//Hopefully no conflicts and this gets added