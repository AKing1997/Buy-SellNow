package com.example.buy_sellnow.Model

data class UserDetail(
    var userId: String,
    var Ubicacion: String,
    var puntuacion: String,
    var tVentas: ArrayList<Product>,
    var image: String,
    var tCompras: ArrayList<Product>,
    var pEnVenta: ArrayList<Product>,
    var suscripcion: String,
    var product: ArrayList<ProductFavorite>

)
