package com.example.buy_sellnow.Model


data class Chat(
    var userBuyerId: String,
    var userSelletId: String,
    var productId: String,
    var productImg: String,
    var productTitle: String,
    var messages: Message,
    var dateTime: String
) {
    constructor() : this("","","", "", "", Message(), "")
}

