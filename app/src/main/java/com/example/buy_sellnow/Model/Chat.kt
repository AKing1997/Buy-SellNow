package com.example.buy_sellnow.Model

import com.google.firebase.database.ServerValue


data class Chat(
    var chatId: String,
    var userBuyerId: String,
    var userSelletId: String,
    var createdDateTime: Any = ServerValue.TIMESTAMP
) {
    constructor() : this("","","")
}

