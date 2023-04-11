package com.example.buy_sellnow.Model

data class Notification(
    val to: String,
    val data: Map<String, String>
)