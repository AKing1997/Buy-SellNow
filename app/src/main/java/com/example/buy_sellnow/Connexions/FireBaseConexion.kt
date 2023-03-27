package com.example.buy_sellnow.Connexions

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FireBaseConexion {
    private var mDatabase: DatabaseReference? = null

    interface OnGetBooksListener {
        fun onSuccess()
        fun onFailure(message: String?)
    }


    /**
     * Creando una connexion con base de datos firebase
     *
     * @Return DatabaseReference
     */
    private fun getReference() {
        mDatabase =
            FirebaseDatabase.getInstance()
                .getReference("Libros")
    }
}