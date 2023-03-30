package com.example.buy_sellnow.Connexions

import android.net.Uri
import com.example.buy_sellnow.Model.Product
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
    private fun getProductReference() {
        mDatabase =
            FirebaseDatabase.getInstance("https://buy-sell-now-an-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Productos");
    }

    fun createProduct(product: Product){
        this.getProductReference()
        mDatabase!!.child(product.productId.toString()).setValue(product);
    }

    fun uploadImages(images: ArrayList<Uri>): ArrayList<String> {
        var uri: ArrayList<String> = ArrayList();
        storage = Firebase.storage
        for(image in images){
            var storageRef = storage.reference.child("Productos ")
            var imageRef = storageRef.child(image.toString());
            var uploadImage = imageRef.putFile(image);
            uploadImage.addOnSuccessListener{
                imageRef.downloadUrl.addOnCompleteListener {
                    if(it.isSuccessful){
                        uri.add(it.toString());
                    }
                }
            }
        }
        return uri
    }
}