package com.example.video_game_collections.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class productOrderModel(

    val pName : String,
    val pCost : Double,
    val quantity : Int,
    val totalProductCost : Double,
    val sellerID : String,
    val buyerID : String?,
    val imageURL : String?,
    var status : OrderStatus,
    var pID : String





){


     override fun equals(other: Any?): Boolean {


         if (other !is productOrderModel) return false

         if(other.pName == this.pName){
            return  true
        }

        return false

    }
}




enum class OrderStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}



