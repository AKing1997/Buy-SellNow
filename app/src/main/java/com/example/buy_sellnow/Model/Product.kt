package com.example.buy_sellnow.Model

import com.example.buy_sellnow.Model.Enum.ProductStatus
import com.example.buy_sellnow.Model.Enum.Status

data class Product(
    var productId: String? = null,
    var tituloDeProducto: String = "",
    var categoria: String = "",
    var subcategoria: String = "",
    var description: String = "",
    var precio: String = "",
    var peso: String = "",
    var tag: String = "",
    var dateCreated: String = "",
    var modifyDate: String = "",
    var image: ArrayList<String> = ArrayList(),
    var color: String = "",
    var marca: String = "",
    var valoracion: String = "",
    var infoDeProducto: String = "",
    var status: Status = Status.Activeted,
    var estadoDeProducto: ProductStatus = ProductStatus.Nuevo,
    var delivery: Boolean = false,
    var userId: String = ""
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", ArrayList(), "", "", "", "", Status.Activeted, ProductStatus.Nuevo, false, "")
}
