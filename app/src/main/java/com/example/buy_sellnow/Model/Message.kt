package com.example.buy_sellnow.Model

import com.google.firebase.database.ServerValue

data class Message(
    var userID: String = "",
    var msg: String = "",
    var img: String= "",
    var sendTime: Any = ServerValue.TIMESTAMP
) {
    constructor() : this("", "","")
}

