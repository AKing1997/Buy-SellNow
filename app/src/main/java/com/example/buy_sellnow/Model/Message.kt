package com.example.buy_sellnow.Model

data class Message(
    var userID: String,
    var msg: String,

) {
    constructor() : this("", "")
}

