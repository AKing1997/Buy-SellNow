package com.example.buy_sellnow.Connexions

import android.net.Uri
import android.util.Log
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.Message
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.Model.tempUser
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
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
    private fun getReference(reference: String): DatabaseReference {
        return FirebaseDatabase.getInstance("https://buy-sell-now-an-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference(reference);
    }
    fun getUserById(userId: String, callback: (tempUser?) -> Unit) {
        mDatabase = this.getReference("Users");
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
    fun updateUserDetaill(user: tempUser, imageUri: Uri?) {
        mDatabase = this.getReference("Users");
        if (imageUri != null) {
            uploadImage(user.userImage,imageUri) { url ->
                user.userImage = url
                mDatabase!!.child(user.userId).setValue(user)
            }
        } else {
            mDatabase!!.child(user.userId).setValue(user)
        }
    }

    fun createProduct(product: Product, imageUris: ArrayList<Uri>) {
        mDatabase = this.getReference("Productos")
        uploadImages(imageUris) { urls ->
            product.image = urls
            mDatabase!!.child(product.productId.toString()).setValue(product);
        }
    }
    /** UPDATE PRODUCT */
    fun updateProduct(product: Product) {
        mDatabase = this.getReference("Productos")
        mDatabase!!.child(product.productId.toString()).setValue(product);
    }

    fun deleteProduct(id: String){
        mDatabase = this.getReference("Productos")
        mDatabase!!.child(id).removeValue();
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
        }.addOnFailureListener {
        }
    }

    private fun uploadImage(url:String,image: Uri, callback: (String) -> Unit) {
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
                if(url.isNotEmpty()) {
                    storage = FirebaseStorage.getInstance()
                    val ref = storage.getReferenceFromUrl(url)
                    ref.delete()
                        .addOnSuccessListener {
                            // El archivo ha sido eliminado exitosamente
                        }
                        .addOnFailureListener { e ->
                            // Ocurrió un error al intentar eliminar el archivo
                            e.message?.let { Log.e("DELETE_IMAGE_ERROR", it) }
                        }
                }
                callback(downloadUri)
            }
        }.addOnFailureListener {
        }
    }

    fun getAllPublicacion(callback: (ArrayList<Product>) -> Unit) {
        mDatabase = this.getReference("Productos");
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products: ArrayList<Product> = ArrayList()
                products.clear();
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
        mDatabase = this.getReference("Productos");
        mDatabase!!.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)
                if (product != null) {
                    callback(product)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("ERROR_FIREBASE", error.message)
            }
        });
    }

    fun getProductsByIds(ids: ArrayList<String>, callback: (ArrayList<Product>) -> Unit) {
        mDatabase = this.getReference("Productos")
        if(ids.size<1){
            return
        }
        /*val query = mDatabase!!.orderByChild("productId").equalTo(ids[0])
        for (i in 1 until ids.size) {
            query.equalTo(ids[i])
        }*/
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products: ArrayList<Product> = ArrayList<Product>()
                for (pro in snapshot.children) {
                    val product = pro.getValue(Product::class.java)
                    if(product?.productId in ids){
                        products.add(product!!)
                    }
                }
                callback(products) // Llamamos al callback cuando se hayan obtenido los datos
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("ERROR_FIREBASE", error.message)
            }
        })
    }

    fun getAllProductsByUserId(userId: String, callback: (ArrayList<Product>) -> Unit) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Productos")
        val query = mDatabase!!.orderByChild("userId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products: ArrayList<Product> = ArrayList<Product>()
                for (pro in snapshot.children) {
                    val product = pro.getValue(Product::class.java)
                    products.add(product!!)
                }
                callback(products)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("ERROR_FIREBASE", error.message)
            }
        })
    }


    /** Chat functions **/
    fun createChat(chat: Chat){
        mDatabase = this.getReference("Chats")
        mDatabase!!.child(chat.chatId).setValue(chat)
    }

    fun getChatById(chatID: String, callback: (Chat?) -> Unit) {
        mDatabase = this.getReference("Chats");
        mDatabase!!.child(chatID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val user = dataSnapshot.getValue(Chat::class.java)
                    callback(user)
                }
                callback(null)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun getAllChatByUserId(userId: String, callback: (ArrayList<Chat>?) -> Unit) {
        mDatabase = this.getReference("Chats");
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chats: ArrayList<Chat> = ArrayList()
                if(dataSnapshot.exists()){
                    for (data in dataSnapshot.children){
                        val chat = data.getValue(Chat::class.java)
                        if (chat != null) {
                            if(chat.userBuyerId == userId || chat.userSelletId == userId){
                                chats.add(chat)
                            }
                        }
                    }
                    callback(chats)
                }
                callback(null)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun sendMsg(msg: Message, chatID: String, image: Uri){
        mDatabase = this.getReference("Messages");
        if (image != null) {
            uploadImage("",image) { url ->
                msg.img = url
                mDatabase!!.child(chatID).child(UUID.randomUUID().toString()).setValue(msg)

            }
        } else {
            mDatabase!!.child(chatID).child(UUID.randomUUID().toString()).setValue(msg)
        }
    }

    fun getMsgByChatId(chatID: String, callback: (ArrayList<Message>?) -> Unit){
        mDatabase = this.getReference("Messages");
        mDatabase!!.child(chatID).orderByChild("sendTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val msgs: ArrayList<Message> = ArrayList()
                for (child in dataSnapshot.children){
                    val msg = child.getValue(Message::class.java)
                    if (msg != null) {
                        Log.i("pro12",msg.toString())
                        msgs.add(msg)
                    }
                }
                callback(msgs)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    /*fun sendMsg(msg: Message, chatID: String){
        mDatabase = this.getReference("Messages");
        mDatabase!!.child(chatID).child(UUID.randomUUID().toString()).setValue(msg)
    }

    fun getMsgByChatId(chatID: String, callback: (ArrayList<Message>?) -> Unit){
        mDatabase = this.getReference("Messages");
        mDatabase!!.child(chatID).orderByChild("sendTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val msgs: ArrayList<Message> = ArrayList()
                msgs.clear();
                for (child in dataSnapshot.children){
                    val msg = child.getValue(Message::class.java)
                    if (msg != null) {
                        Log.i("pro12",msg.toString())
                        msgs.add(msg)
                    }
                }
                callback(msgs)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }*/

    /** Get, Add & Remove Favorite product list **/
    fun addTFavorite(productId: String, userId: String) {
        mDatabase = this.getReference("favorites");
        mDatabase!!.child(userId).child(productId).setValue(true)
    }

    fun rmFFavorite(productId: String, userId: String) {
        mDatabase = this.getReference("favorites");
        mDatabase!!.child(userId).child(productId).removeValue()
    }

    fun getFavoriteByProductAndUserId(productId: String, userId: String, callback: (Boolean?) -> Unit) {
        mDatabase = this.getReference("favorites");
        mDatabase!!.child(userId).child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isFavorite = dataSnapshot.exists() && dataSnapshot.getValue(Boolean::class.java) == true
                callback(isFavorite)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun getAllFavoritesByUserId(userId: String, callback: (ArrayList<String>?) -> Unit){
        mDatabase = this.getReference("favorites");
        mDatabase!!.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productsIds: ArrayList<String>  = ArrayList()
                for (pro in dataSnapshot.children) {
                    productsIds.add(pro.key.toString())
                    Log.i("favoritos: ",pro.key.toString())
                }
                callback(productsIds)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}