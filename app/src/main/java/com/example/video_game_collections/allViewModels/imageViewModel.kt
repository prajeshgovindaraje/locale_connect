package com.example.video_game_collections.allViewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class imageViewModel : ViewModel(){


    fun uploadImageAndSaveProduct(
        name: String,
        price: Double,
        sellerId : String,
        imageUri: Uri?,
        fireStoreViewModel: fireStoreViewModel,

    ) {
        // Obtain a FirebaseStorage instance
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("product_images/${UUID.randomUUID()}.jpg")

        // Upload the image
        if (imageUri != null) {
            imagesRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        // Save the product data in Firestore
                        fireStoreViewModel.addProductsIntoDB(name, price, sellerId=sellerId ,imageURL = imageUrl)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseStorage", "Image upload failed", exception)
                }
        }else{
            fireStoreViewModel.addProductsIntoDB(name, price, sellerId=sellerId ,imageURL = null)

        }
    }


    fun uploadShopImage(
        fireStoreViewModel: fireStoreViewModel,
        imageUri: Uri?,
        userID: String,
        onSuccess:() -> Unit
    ){
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("shop_images/${UUID.randomUUID()}.jpg")

        if (imageUri != null) {
            imagesRef.putFile(imageUri)
                .addOnSuccessListener {it->

                    it.storage.downloadUrl.addOnSuccessListener {

                        val imageUrl = it.toString()
                        fireStoreViewModel.db.collection("users").document(userID)
                            .update("shopImage",imageUrl)
                            .addOnCompleteListener {
                                onSuccess()
                            }



                    }

                }
        }

    }


    fun getCurrentShopImageURL(
        userID: String,
        fireBaseAuthViewModel: fireBaseAuthViewModel,
        onSuccess:(shopImage:String) -> Unit
    ){

        fireBaseAuthViewModel.db.collection("users")
            .document(userID)
            .addSnapshotListener { value, error ->

                value?.get("shopImage")?.let { onSuccess(it.toString()) }

            }



    }













}