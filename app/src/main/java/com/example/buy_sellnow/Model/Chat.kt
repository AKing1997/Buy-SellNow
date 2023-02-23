package com.example.buy_sellnow.Model

import java.time.chrono.ChronoLocalDateTime

data class Chat(
    var id: String,
    var userBuyerId: String,
    var userSelletId: String,
    var productId: String,
    var dateTime: String
)
