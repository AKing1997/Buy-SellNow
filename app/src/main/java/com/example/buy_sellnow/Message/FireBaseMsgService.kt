package com.example.buy_sellnow.Message

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMsgService: FirebaseMessagingService() {

        override fun onMessageReceived(remoteMessage: RemoteMessage) {

            if (remoteMessage.data.isNotEmpty()) {
               // Toast.makeText(MainActivity@this, remoteMessage.data, Toast.LENGTH_SHORT).show()
            }
            remoteMessage.notification?.let {
            }
        }

        override fun onNewToken(token: String) {
        }
}