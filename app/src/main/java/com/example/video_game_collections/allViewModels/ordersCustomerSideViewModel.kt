package com.example.video_game_collections.allViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.OrderStatus
import com.example.video_game_collections.dataModels.productOrderModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ordersCustomerSideViewModel : ViewModel(){

    var db = FirebaseFirestore.getInstance()

    //track total cost of all the ordered products
    private var _totalCost = MutableLiveData<Double>(0.0)
    var totalCost : LiveData<Double> = _totalCost

    //list to store all the added products
    private var _productsInCartState = MutableLiveData<MutableList<productOrderModel>>()
    var productsInCartState : LiveData<MutableList<productOrderModel>> = _productsInCartState


    //Map to increase and decrease count of individual items
    private var _productsCountMapState = MutableLiveData<MutableMap<String,Int>>()
    var productsCountMapState : LiveData<MutableMap<String,Int>> = _productsCountMapState

    //Represents Orders stored in DB
    private var _ordersMapState = MutableLiveData<MutableList<Map<String,Any>>>()
    var ordersMapState : LiveData<MutableList<Map<String,Any>>> = _ordersMapState

    //used to display current products in a order from order page
    private var _displayProductsInCurrentOrderListState = MutableLiveData<MutableList<Map<String,Any>>>()
    var displayProductsInCurrentOrderListState : LiveData<MutableList<Map<String,Any>>> = _displayProductsInCurrentOrderListState


    var tempProductsInCartList = mutableListOf<productOrderModel>() //an empty list
    fun addOrUpdateToProductsInCartList(productInCart : productOrderModel){

        Log.i("orderIncDec","inside addorUpdate cart list function")

        if(tempProductsInCartList.isEmpty()){
            tempProductsInCartList.add(productInCart)
            Log.i("orderIncDec","list empty so product added")
        }else if(tempProductsInCartList.contains(productInCart)){
            var index = tempProductsInCartList.indexOf(productInCart)
            tempProductsInCartList.set(index, productInCart)
            Log.i("orderIncDec","same name product present to list updated")
        }else{
            tempProductsInCartList.add(productInCart)
            Log.i("orderIncDec","new product so added")
        }

        Log.i("orderIncDec","the list is ${tempProductsInCartList}")

        _productsInCartState.value = tempProductsInCartList.toMutableList()

    }


    fun removeProductsInCartList(productInCart: productOrderModel){
        tempProductsInCartList.remove(productInCart)

        _productsInCartState.value = tempProductsInCartList.toMutableList()

    }



    var tempProductsCountMap = mutableMapOf<String,Int>()
    fun incrementOrderCount(pName : String, currCount : Int ){

        tempProductsCountMap.set(pName,currCount+1)

        _productsCountMapState.value = tempProductsCountMap.toMutableMap()

    }



    fun decrementCount(pName : String, currCount : Int ){

        tempProductsCountMap.set(pName,currCount-1)

        _productsCountMapState.value = tempProductsCountMap.toMutableMap()

    }

    fun getCurrCount(pName : String): Int{

        return tempProductsCountMap.get(pName) ?: 0

    }


    fun addToTotalCost(pCost : Double){
        _totalCost.value = _totalCost.value?.plus(pCost)
    }

    fun reduceFromTotalCost(pCost : Double){
        _totalCost.value = _totalCost.value?.minus(pCost)
    }



    fun generateRandomAlphanumericString(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    //used while checkout button is clicked
    fun addIntoOrders(
        orderList: MutableList<productOrderModel>,
        totalCost: Double,
        context: Context,
        buyerID : String,
        status : String,
        sellerID : String,
    ){

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        val orderID = generateRandomAlphanumericString(20)

        val orderDocument = mapOf(
            "orderList" to orderList,  // Store the list as a field
            "totalOrderCost" to totalCost,
            "buyerID" to buyerID,
            "timestamp" to currentTime,
            "orderId" to orderID,
            "sellerID" to sellerID,
            "status" to status
        )

        db.collection("orders").add(orderDocument)
            .addOnSuccessListener {
               // Log.i("orderProd",_ordersMapState.value.toString())
                Toast.makeText(context,"Order Placed",Toast.LENGTH_LONG).show()
                tempProductsInCartList.clear()
                _productsInCartState.value = tempProductsInCartList.toMutableList()
                _totalCost.value = 0.0
                tempProductsCountMap.clear()
                _productsCountMapState.value = tempProductsCountMap.toMutableMap()


            }
            .addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }

    }


    var tempOrderedProductMap = mutableListOf<Map<String,Any>>()
    fun displayCurrentUserOrders(
        buyerId : String
    ){
        db.collection("orders")
            .whereEqualTo("buyerID",buyerId)
            .get()
            .addOnSuccessListener {

                tempOrderedProductMap.clear()

                Log.i("orderProd","fetch  success")


                for (doc in it){
                    tempOrderedProductMap += doc.data as Map<String,Any>
                    Log.i("orderProd","tempOrderedProductMap update  success")

                    _ordersMapState.value = tempOrderedProductMap.toMutableList()



                }


            }
            .addOnFailureListener {
                Log.i("orderProd",it.message.toString())
            }
    }



    fun deleteEntireOrder(
        orderID: String,
        currentTime: String,
        buyerID: String,
        orderList : MutableList<Map<String,Any>>,
        totalOrderCost : Double,
        status : String,
        sellerID: String

    ){

        db.collection("orders")
            .whereEqualTo("orderId",orderID)
            .get()
            .addOnSuccessListener {

                if(it != null && !it.isEmpty){

                    tempOrderedProductMap.remove(it.documents[0].data)
                    var docID = it.documents[0].id
                    db.collection("orders").document(docID).delete()
                        .addOnSuccessListener {
                            Log.i("deleteOrder","before delete"+_ordersMapState.value.toString())

                            _ordersMapState.value = tempOrderedProductMap.toMutableList()
                            Log.i("deleteOrder","after delete"+_ordersMapState.value.toString())

                        }
                }

            }


    }




    var tempDisplayProductsInCurrentOrderList = (mutableListOf<Map<String,Any>> ())
    fun displayProductsInCurrentOrder(orderedList : MutableList<Map<String,Any>>){

        tempDisplayProductsInCurrentOrderList.clear()

        tempDisplayProductsInCurrentOrderList += orderedList

        _displayProductsInCurrentOrderListState.value = tempDisplayProductsInCurrentOrderList.toMutableList()



    }

    fun countStatus(
        productsInOrderList:List<Map<String,Any>>,
        onSuccess:(MutableList<Int>) -> Unit
    ){
        var totalProductsInTheOrder = productsInOrderList.size

        var countList = mutableListOf(0,0,0)


        for (product in productsInOrderList) {
            when (product["status"]) {

                OrderStatus.PENDING.toString() -> countList[0] = countList[0] + 1
                OrderStatus.ACCEPTED.toString() -> countList[1] = countList[1] + 1
                OrderStatus.REJECTED.toString() -> countList[2] = countList[2] + 1
            }
        }

        onSuccess(countList)
    }




    










}