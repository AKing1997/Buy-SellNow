package com.example.buy_sellnow.Message

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMsgService: FirebaseMessagingService() {

        override fun onMessageReceived(remoteMessage: RemoteMessage) {

            if (remoteMessage.data.isNotEmpty()) {
            }
            remoteMessage.notification?.let {
            }
        }

        override fun onNewToken(token: String) {
        }
}