package com.example.video_game_collections.allViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.usersModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class fireBaseAuthViewModel : ViewModel(){



    private val _loginStatusState = MutableLiveData<loginStatus>()

    var loginStatusState:LiveData<loginStatus>  = _loginStatusState


    var auth = FirebaseAuth.getInstance()
    var db = Firebase.firestore

    init {
        checkUserLoginStatus()
    }

    fun checkUserLoginStatus(){

        if(auth.currentUser != null){
            _loginStatusState.value = loginStatus.LoggedIn
        }else{
            _loginStatusState.value = loginStatus.LoggedOut
        }

    }



    fun loginUser(email : String, password : String){
        Log.i("response","inside login User")
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {

                _loginStatusState.value = loginStatus.LoggedIn
                Log.i("response","inside login success")

            }
            .addOnFailureListener {
                Log.i("response","inside login failure")

                _loginStatusState.value = loginStatus.LoggedOut
            }

    }


    fun siginInUser(
        email: String,
        password: String,
        role: String,
        userName: String,
        shopName: String?,
        shopType : String?

    ){

        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.i("response","inside create user success")
                var id = it.user?.uid
                var tempUsersModel  = usersModel(
                    userID = id,
                    email = email,
                    role = role,
                    userName = userName,
                    shopName = shopName,
                    shopType = shopType
                )


                db.collection("users")
                    .add(tempUsersModel)
                    .addOnSuccessListener {
                        Log.i("response","inside into db success")

                    }


                _loginStatusState.value = loginStatus.LoggedIn
            }
            .addOnFailureListener {
                _loginStatusState.value = loginStatus.LoggedOut
            }

    }


     fun getUserRole(callback:(String) -> Unit){



         db.collection("users").addSnapshotListener { value, error ->

             if (value != null) {
                 for(it in value){

                    // auth.currentUser?.let { it1 -> Log.i("response", it1.uid) }


                     if(it["userID"].toString() == auth.currentUser?.uid ?: null){
                         Log.i("response","match found curr user in db ${it["userID"].toString()} ")

                         var role = it["role"].toString()
                         callback(role)

                     }

                 }
             }

         }



    }



    fun signOut(){

        auth.signOut()
        _loginStatusState.value = loginStatus.LoggedOut

    }













}



sealed class loginStatus{

    data object LoggedIn : loginStatus()
    data object LoggedOut: loginStatus()

    data class Error(var errorMessage: String ): loginStatus()


}