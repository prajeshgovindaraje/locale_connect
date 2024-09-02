package com.example.video_game_collections.Screens.CustomerScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.Style
import androidx.navigation.NavController
import com.example.video_game_collections.R
import com.example.video_game_collections.Screens.AutoResizedText
import com.example.video_game_collections.Screens.NavigationPages
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    fireStoreViewModel: fireStoreViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(
                text = "Profile",
                style = MaterialTheme.typography.displayMedium
            ) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )
        },
    )
    { innerpadding ->
        Box(modifier = Modifier.offset(y = (-50).dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.98f)
                    .padding(innerpadding),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .border(width = 5.dp, color = Color.DarkGray, shape = CircleShape)
                            .padding(10.dp)
                            .background(Color.Gray, CircleShape)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.4f)
                        .offset(x = 30.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier= Modifier
                            .width(350.dp)
                            .height(50.dp)
                            .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,


                    ){
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        Spacer(modifier = Modifier.width(15.dp))
                        AutoResizedText(text = "suriyansrcse@gmail.com", style = MaterialTheme.typography.titleLarge)
                    }
                    Row(
                        modifier= Modifier
                            .width(350.dp)
                            .height(50.dp)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,

                    ){
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Location", style = MaterialTheme.typography.headlineSmall)
                    }
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            MaterialTheme.colorScheme.onSecondaryContainer,
                            CutCornerShape(400.dp)
                        ),
                    contentAlignment = Alignment.Center,

                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(.6f)
                        ,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Suriyan",style= MaterialTheme.typography.displaySmall)
                        Text(text = "Customer", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth(.68f)
                    .height(150.dp)
                    .padding(bottom = 90.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(20.dp)
                    ).clickable {
                        fireBaseAuthViewModel.signOut()
                        navController.navigate(NavigationPages.loginPage)
                    }
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = null, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "LogOut", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}