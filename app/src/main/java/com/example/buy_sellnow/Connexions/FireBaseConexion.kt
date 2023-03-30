package com.example.buy_sellnow.Connexions

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class FireBaseConexion {
    private var mDatabase: DatabaseReference? = null
    lateinit var storage: FirebaseStorage;
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

    public fun uploadImages(images: ArrayList<Uri>){
        var uri: ArrayList<String> = ArrayList();
        storage = Firebase.storage
        for(image in images){
            var storageRef = storage.reference.child("Image ").putFile(image);
            storageRef.addOnSuccessListener {

            }.addOnFailureListener {
                Log.e("Firebase", "Image Upload External KO")
            }
        }
    }
}