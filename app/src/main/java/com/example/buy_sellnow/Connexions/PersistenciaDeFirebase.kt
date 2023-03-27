package com.example.buy_sellnow.Connexions

import android.app.Application
import com.google.firebase.database.FirebaseDatabase


class PersistenciaFirebase : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance("https://m9firebase-e14c1-default-rtdb.europe-west1.firebasedatabase.app/")
            .setPersistenceEnabled(true)
    }
}
