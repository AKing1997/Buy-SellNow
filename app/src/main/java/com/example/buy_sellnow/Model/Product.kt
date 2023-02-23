package com.example.buy_sellnow.Model

import com.example.buy_sellnow.Model.Enum.ProductStatus
import com.example.buy_sellnow.Model.Enum.Status

data class Product(
    var productId: String,
    var tituloDeProducto: String,
    var categoria: String,
    var subcategoria: String,
    var description: String,
    var precio: String,
    var peso: String,
    var tag: String,
    var dateCreated: String,
    var modifyDate: String,
    var image: ArrayList<String>,
    var color: String,
    var marca: String,
    var valoracion: String,
    var infoDeProducto: String,
    var status: Status,
    var estadoDeProducto: ProductStatus,
    var dilevery: Boolean,
    var userId: String,
)
