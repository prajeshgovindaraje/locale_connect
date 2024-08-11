package com.example.video_game_collections.Screens



import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.loginStatus

@SuppressLint("SuspiciousIndentation")
@Composable
fun signInScreen(navController: NavController, viewModel: fireBaseAuthViewModel) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var role by remember {
        mutableStateOf("")
    }
    var context = LocalContext.current
    val observedLoginStatus  = viewModel.loginStatusState.observeAsState()

    LaunchedEffect(key1 = observedLoginStatus.value) {

        when(observedLoginStatus.value){
            //is loginStatus.Error -> TODO()
            is loginStatus.LoggedIn -> {


                viewModel.getUserRole {role ->
                    if(role == "customer"){
                        navController.navigate(customerPage)
                    }else if(role == "seller"){
                        navController.navigate(sellerPage)
                    }
                }

            }
            is loginStatus.LoggedOut -> {
                Toast.makeText(context,"error", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }



    }






    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "SignUp Page",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text(text = "Enter Email") }

            )

            Spacer(modifier = Modifier.height(24.dp))



            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "Enter Password") }

            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = role,
                onValueChange = {
                    role = it
                },
                label = { Text(text = "Enter Role") }

            )
            Spacer(modifier = Modifier.height(24.dp))


            Button(onClick = {

                viewModel.siginInUser(email, password, role)

            },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(text = "Sigin In")

            }


            TextButton(
                onClick = {

                    navController.navigate(loginPage)

                },
                modifier = Modifier.align(Alignment.CenterHorizontally)


            ) {

                Text(text = "Already have an account ? head back to Login")

            }


        }
    }

}