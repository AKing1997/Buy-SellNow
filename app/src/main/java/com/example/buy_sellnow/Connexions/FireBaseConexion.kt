package com.example.buy_sellnow.Connexions

import android.net.Uri
import android.util.Log
import com.example.buy_sellnow.Model.Product
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList


class FireBaseConexion {
    private var mDatabase: DatabaseReference? = null
    lateinit var storage: FirebaseStorage;


    /**
     * Creando una connexion con base de datos firebase
     *
     * @Return DatabaseReference
     */
    private fun getProductReference(reference: String) {
        mDatabase =
            FirebaseDatabase.getInstance("https://buy-sell-now-an-default-rtdb.europe-west1.firebasedatabase.app").getReference(reference);
    }

    fun createProduct(product: Product, imageUris: ArrayList<Uri>){
        this.getProductReference("Productos")
        var imageUrls :ArrayList<String> = ArrayList();
            uploadImages(imageUris, "Productos ", product.productId.toString(), {
              product.image.addAll(imageUrls)
            })
        Log.i("prueba", imageUrls.toString())

        product.image.addAll(imageUrls);
        mDatabase!!.child(product.productId.toString()).setValue(product);
    }

    fun uploadImages(images: List<Uri>, carpeta: String, nombres: String, callback: (List<String>) -> Unit) {
        storage = Firebase.storage
        val storageRef = storage.reference.child(carpeta)
        val urls = mutableListOf<String>()
        val tasks = mutableListOf<Task<Uri>>()

        for (i in 0 until images.size) {
            val imageRef = storageRef.child("${UUID.randomUUID()}_${nombres}.jpeg")
            val uploadTask = imageRef.putFile(images[i])
            // esto me sale con error....  tasks.add(uploadTask)
        }

        Tasks.whenAllSuccess<Uri>(tasks)
            .addOnSuccessListener { uris ->
                uris.forEach { uri ->
                    urls.add(uri.toString())
                }
                callback(urls)
            }
            .addOnFailureListener {
                // handle failure
            }
    }
}