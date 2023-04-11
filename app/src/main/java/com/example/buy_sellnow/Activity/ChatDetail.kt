package com.example.buy_sellnow.Activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Interface.FCMService
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.Notification
import com.example.buy_sellnow.R
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatDetail : AppCompatActivity() {
    /** Activity init variables **/
    lateinit var PRODUCT_ID: String
    lateinit var USER_ID: String
    lateinit var PRODUCT_TITLE: String
    lateinit var PRODUCT_USER_ID: String

    /** Chat Header variables **/
    lateinit var chatDetailProductName: TextView
    lateinit var chatDetailBkBtn: ImageButton
    lateinit var chatDetailPImg: ImageView
    /** Chat Bottom variables **/
    lateinit var chatDetailAddBtn: ImageView
    lateinit var chatDetailTextEdit: EditText
    lateinit var chatDetailSendBtn: ImageView
    var chat: Chat = Chat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        chatDetailProductName = findViewById(R.id.chatDetailProductName)
        chatDetailBkBtn = findViewById(R.id.chatDetailBkBtn)
        chatDetailPImg = findViewById(R.id.chatDetailPImg)

        PRODUCT_ID = intent.getStringExtra("PRODUCT_ID").toString()
        USER_ID = intent.getStringExtra("USER_ID").toString()
        PRODUCT_USER_ID = intent.getStringExtra("PRODUCT_USER_ID").toString()
        PRODUCT_TITLE = intent.getStringExtra("PRODUCT_TITLE").toString()
        chatDetailProductName.setText(PRODUCT_TITLE)

        Glide.with(this).load(intent.getStringExtra("PRODUCT_IMG").toString())
            .into(chatDetailPImg)

        chatDetailBkBtn.setOnClickListener {
            onBackPressed();
        }

        val conexion: FireBaseConexion = FireBaseConexion()
        conexion.getChatById(USER_ID+"-"+PRODUCT_ID){
            if(it!=null){
                chat = it
            }
        }
    }

    private fun sendNotification(token: String, title: String, message: String) {
        val notification = Notification(
            to = token,
            data = mapOf(
                "title" to title,
                "message" to message
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FCMService::class.java)

        service.sendNotification(notification).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, "Notificación enviada correctamente")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error al enviar la notificación", t)
            }
        })
    }

}