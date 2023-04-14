package com.example.buy_sellnow.Activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Adapters.MsgAdapter
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Interface.FCMService
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.Message
import com.example.buy_sellnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ChatDetail : AppCompatActivity() {
    val fIntance = FirebaseMessaging.getInstance()
    lateinit var adapter: MsgAdapter
    /** Activity init variables **/
    lateinit var PRODUCT_ID: String
    lateinit var USER_ID: String
    lateinit var PRODUCT_TITLE: String
    lateinit var PRODUCT_USER_ID: String
    lateinit var PRODUCT_IMG: String
    var CHAT_ID: String = ""

    /** Chat Header variables **/
    lateinit var chatDetailProductName: TextView
    lateinit var chatDetailBkBtn: ImageButton
    lateinit var chatDetailPImg: ImageView
    lateinit var chatShareImage: ImageView
    lateinit var chatCamera: ImageView

    /** Chat Bottom variables **/
    lateinit var chatDetailAddBtn: ImageView
    lateinit var chatDetailTextEdit: EditText
    lateinit var chatDetailSendBtn: ImageView
    lateinit var msgRecyclerView: RecyclerView
    lateinit var imageUri: Uri
    lateinit var chat: Chat
    var mensage: Message? = null
    val conexion: FireBaseConexion = FireBaseConexion()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        fIntance.isAutoInitEnabled = true
        fIntance.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                Log.d(TAG, "Token de registro del dispositivo: $token")
            } else {
                Log.e(TAG, "Error al obtener el token de registro del dispositivo", task.exception)
            }
        }
        chatDetailProductName = findViewById(R.id.chatDetailProductName)
        chatDetailBkBtn = findViewById(R.id.chatDetailBkBtn)
        chatDetailPImg = findViewById(R.id.chatDetailPImg)

        msgRecyclerView = findViewById(R.id.msgRecyclerView)

        chatShareImage = findViewById(R.id.chatShareImage)
        chatCamera = findViewById(R.id.chatCamera)
        chatDetailTextEdit = findViewById(R.id.chatDetailTextEdit)
        chatDetailSendBtn = findViewById(R.id.chatDetailSendBtn)


        loadInfo()


    }

    private fun loadEvents() {
        chatDetailBkBtn.setOnClickListener {
            onBackPressed()
        }

        chatDetailSendBtn.setOnClickListener {
            var msg = chatDetailTextEdit.text
            if (msg.isNotEmpty()) {
                mensage = Message(userId, msg.toString(), "")
                conexion.sendMsg(mensage!!, CHAT_ID)
                val ids = CHAT_ID.split("-")
                if(userId == ids[0]){
                    getUserToken(ids[1])
                }else{
                    getUserToken(ids[0])
                }
                chatDetailTextEdit.setText("")
            }
        }

        if (CHAT_ID in "null") {
            CHAT_ID = (PRODUCT_USER_ID + "-" + USER_ID + "-" + PRODUCT_ID)
            conexion.createChat(Chat(CHAT_ID, PRODUCT_ID, USER_ID, PRODUCT_USER_ID))
            getMsgByChatId()
        } else {
            getMsgByChatId()
        }


    }

    private fun getUserToken(id: String) {
        conexion.getUserById(id){
            if (it != null) {
                sendNotification(it.userToken, PRODUCT_TITLE, mensage!!.msg)
            }
        }
    }

    private fun getMsgByChatId() {
        conexion.getMsgByChatId(CHAT_ID) {

            msgRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = MsgAdapter(it!!, this)
            val lastPosition = adapter.itemCount - 1
            msgRecyclerView.scrollToPosition(lastPosition)
            msgRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadInfo() {
        PRODUCT_ID = intent.getStringExtra("PRODUCT_ID").toString()
        USER_ID = intent.getStringExtra("USER_ID").toString()
        PRODUCT_USER_ID = intent.getStringExtra("PRODUCT_USER_ID").toString()
        PRODUCT_TITLE = intent.getStringExtra("PRODUCT_TITLE").toString()
        PRODUCT_IMG = intent.getStringExtra("PRODUCT_IMG").toString()
        CHAT_ID = intent.getStringExtra("CHAT_ID").toString()
        chatDetailProductName.setText(PRODUCT_TITLE)
        Glide.with(this).load(PRODUCT_IMG)
            .into(chatDetailPImg)
        loadEvents()
    }

    private fun sendNotification(token: String, title: String, message: String) {
        val notification = RemoteMessage.Builder(token).setData(
            mapOf(
            "title" to title,
            "message" to message
            )
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/notification/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FCMService::class.java)

        /*service.sendNotification(notification).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.e("Notification Failure", "Error sending notification: ${t.message}")
            }
        })
    }*/
        service.sendNotification(notification).enqueue(object : Callback<Any> {
            override fun onResponse(call: retrofit2.Call<Any>, response: Response<Any>) {
                Log.i("TokenMessage", "Success ${response}----${response.isSuccessful}")
            }

            override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
                Log.i("TokenMessage", t.message.toString())
            }

        })
    }

}