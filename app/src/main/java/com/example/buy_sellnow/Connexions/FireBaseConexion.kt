package com.example.buy_sellnow.Connexions

import android.net.Uri
import android.util.Log
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.Model.tempUser
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*


class FireBaseConexion {
    private var mDatabase: DatabaseReference? = null
    lateinit var storage: FirebaseStorage;

    /**
     * Creando una connexion con base de datos firebase
     *
     * @Return DatabaseReference
     */
    private fun getReference(reference: String) {
        mDatabase =
            FirebaseDatabase.getInstance("https://buy-sell-now-an-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference(reference);
    }

    fun updateUserDetaill(user: tempUser, imageUri: Uri?) {
        this.getReference("Users");
        if (imageUri != null) {
            uploadImage(imageUri) { url ->
                user.userImage = url
                mDatabase!!.child(user.userId).setValue(user)
            }
        } else {
            mDatabase!!.child(user.userId).setValue(user)
        }
    }

    fun createProduct(product: Product, imageUris: ArrayList<Uri>) {
        this.getReference("Productos")
        uploadImages(imageUris) { urls ->
            product.image = urls
            mDatabase!!.child(product.productId.toString()).setValue(product);
        }
    }

    fun updateChat(chat: Chat){
        this.getReference("Chats")
        mDatabase!!.child(chat.userBuyerId+"-"+chat.userSelletId).setValue(chat)
    }

    private fun uploadImages(images: ArrayList<Uri>, callback: (ArrayList<String>) -> Unit) {
        val uriList = ArrayList<String>()
        val tasks = mutableListOf<Task<Uri>>()
        val storageRef = Firebase.storage.reference.child("Productos")

        for (image in images) {
            val imageRef = storageRef.child("${UUID.randomUUID()}.jpeg")
            val uploadTask = imageRef.putFile(image)
            val task = Tasks.whenAllComplete(uploadTask)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }
            tasks.add(task)
        }
        Tasks.whenAllSuccess<Uri>(tasks).addOnSuccessListener { uriResult ->
            for (uri in uriResult) {
                uriList.add(uri.toString())
                callback(uriList)
            }
        }.addOnFailureListener { exception ->
        }
    }

    private fun uploadImage(image: Uri, callback: (String) -> Unit) {
        val storageRef = Firebase.storage.reference.child("User")
        val imageRef = storageRef.child("${UUID.randomUUID()}.jpeg")
        val uploadTask = imageRef.putFile(image)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                callback(downloadUri)
            }
        }.addOnFailureListener { exception ->
            // Manejar la excepción
        }
    }

    fun getAllPublicacion(callback: (ArrayList<Product>) -> Unit) {
        val products: ArrayList<Product> = ArrayList<Product>()
        this.getReference("Productos");
        products.clear();
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (pro in snapshot.children) {
                    val product = pro.getValue(Product::class.java)
                    products.add(product!!);
                }
                callback(products) // Llamamos al callback cuando se hayan obtenido los datos
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("ERROR_FIREBASE", error.message)
            }
        });
    }

    fun getProductById(id: String, callback: (Product) -> Unit) {
        this.getReference("Productos");
        mDatabase!!.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // if (snapshot.exists()) {
                val product = snapshot.getValue(Product::class.java)
                callback(product!!)
                //} else {
                //}
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("ERROR_FIREBASE", error.message)
            }
        });
    }

    fun getUserById(userId: String, callback: (tempUser?) -> Unit) {
        this.getReference("Users");
        mDatabase!!.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(tempUser::class.java)
                callback(user)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun getChatById(chatID: String, callback: (Chat?) -> Unit) {
        this.getReference("Chats");
        mDatabase!!.child(chatID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(Chat::class.java)
                callback(user)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}