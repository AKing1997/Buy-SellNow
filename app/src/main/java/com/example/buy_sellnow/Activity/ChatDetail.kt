package com.example.buy_sellnow.Activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Adapters.MsgAdapter
import com.example.buy_sellnow.Adapters.RecycleViewList
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Interface.FCMService
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.Message
import com.example.buy_sellnow.Model.Notification
import com.example.buy_sellnow.R
import com.google.firebase.auth.FirebaseAuth
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
    lateinit var PRODUCT_IMG: String
    /** Chat Header variables **/
    lateinit var chatDetailProductName: TextView
    lateinit var chatDetailBkBtn: ImageButton
    lateinit var chatDetailPImg: ImageView
    /** Chat Bottom variables **/
    lateinit var chatDetailAddBtn: ImageView
    lateinit var chatDetailTextEdit: EditText
    lateinit var chatDetailSendBtn: ImageView
    lateinit var msgRecyclerView: RecyclerView
    lateinit var imageUri: Uri
    var chat: Chat = Chat()
    var msgs: ArrayList<Message> = ArrayList()
    val conexion: FireBaseConexion = FireBaseConexion()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        //1:50
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        chatDetailProductName = findViewById(R.id.chatDetailProductName)
        chatDetailBkBtn = findViewById(R.id.chatDetailBkBtn)
        chatDetailPImg = findViewById(R.id.chatDetailPImg)

        msgRecyclerView = findViewById(R.id.msgRecyclerView)

        chatDetailAddBtn = findViewById(R.id.chatDetailAddBtn)
        chatDetailTextEdit = findViewById(R.id.chatDetailTextEdit)
        chatDetailSendBtn = findViewById(R.id.chatDetailSendBtn)

        PRODUCT_ID = intent.getStringExtra("PRODUCT_ID").toString()
        USER_ID = intent.getStringExtra("USER_ID").toString()
        PRODUCT_USER_ID = intent.getStringExtra("PRODUCT_USER_ID").toString()
        PRODUCT_TITLE = intent.getStringExtra("PRODUCT_TITLE").toString()
        PRODUCT_IMG = intent.getStringExtra("PRODUCT_IMG").toString()
        chatDetailProductName.setText(PRODUCT_TITLE)

        Glide.with(this).load(PRODUCT_IMG)
            .into(chatDetailPImg)

        chatDetailBkBtn.setOnClickListener {
            onBackPressed();
        }
        val chatID: String = (PRODUCT_USER_ID+"-"+USER_ID+"-"+PRODUCT_ID)

        chatDetailSendBtn.setOnClickListener {
            var msg = chatDetailTextEdit.text
            if(msg.isNotEmpty()){
                conexion.sendMsg(Message(userId,msg.toString(), ""), chatID)
                chatDetailTextEdit.setText("")
            }
        }


        conexion.getChatById(chatID){
            if(it!=null){
                chat = it
            }else{
                conexion.createChat(Chat(chatID, PRODUCT_ID , USER_ID,PRODUCT_USER_ID))
            }
        }

        conexion.getMsgByChatId(chatID){
            msgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                val adapter : MsgAdapter = MsgAdapter(it!!, this);
                msgRecyclerView.adapter = adapter;
                adapter.notifyDataSetChanged()
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