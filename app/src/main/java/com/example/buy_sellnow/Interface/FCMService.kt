package com.example.buy_sellnow.Interface

import com.google.firebase.messaging.RemoteMessage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMService {
    @Headers("Content-Type:application/json", "Authorization:key=BKV4tqfLZ8M5h4K9Nf5EdKTCL1V5EukWF_9T6AY5EeI1zAEqo3k0mgm0ITfkhFE-eU0k3vmLZzbEv1ecje2wpwg")
    @POST("fcm/send")
    fun sendNotification(@Body notification: RemoteMessage): Call<Any>
}