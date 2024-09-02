import com.example.video_game_collections.dataModels.OrderStatus
import com.example.video_game_collections.dataModels.productOrderModel

fun listOfMapsToProductOrderModels(maps: MutableList<Map<String, Any>>): MutableList<productOrderModel> {
    return maps.map { map ->
        productOrderModel(
            pName = map["pname"] as? String ?: "",
            pCost = (map["pcost"] as? Number)?.toDouble() ?: 0.0,
            quantity = (map["quantity"] as? Number)?.toInt() ?: 0,
            totalProductCost = (map["totalProductCost"] as? Number)?.toDouble() ?: 0.0,
            sellerID = map["sellerID"] as? String ?: "",
            buyerID = map["buyerID"] as? String,
            imageURL = map["imageURL"] as? String,
            status = OrderStatus.valueOf(map["status"] as? String ?: "mmmbu"), // Handle default value
            pID = map["pid"] as? String ?: ""
        )
    }.toMutableList()
}



fun productOrderModelsToListOfMaps(products: MutableList<productOrderModel>): MutableList<Map<String, Any>> {
    return products.map { product ->
        mapOf(
            "pname" to product.pName as Any,
            "pcost" to product.pCost as Any,
            "quantity" to product.quantity as Any,
            "totalProductCost" to product.totalProductCost as Any,
            "sellerID" to product.sellerID as Any,
            "buyerID" to (product.buyerID ?: "" as Any), // Handle nullable value
            "imageURL" to (product.imageURL ?: "" as Any), // Handle nullable value
            "status" to product.status.name as Any, // Assuming OrderStatus is an enum
            "pid" to product.pID as Any
        )
    }.toMutableList()
}