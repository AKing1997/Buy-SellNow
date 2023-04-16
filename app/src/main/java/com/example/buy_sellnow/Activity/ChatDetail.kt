package com.example.buy_sellnow.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Adapters.MsgAdapter
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Interface.FCMService
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.Message
import com.example.buy_sellnow.R
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
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
    companion object {
        private lateinit var map: GoogleMap
        private const val REQUEST_CODE = 1
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        private const val EXTERNAL_PERMISSION_REQUEST_CODE = 1002
        private var CAMERA_EXTERNAL = 0 //1 CAM - 2 EXTERNAL
    }
    var pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            conexion.sendMsg(Message(userId,"",""), CHAT_ID,imageUri)

            //imageUri = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
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
        chatCamera.setOnClickListener {
            if (!isCameraPermissionGranted()) {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Sinó farem l'intent de mostrar la càmera
                cameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
        }
        chatShareImage.setOnClickListener {
            if (isExternalPermissionGranted()) {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    EXTERNAL_PERMISSION_REQUEST_CODE
                )
            }
        }

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
                conexion.sendMsg(mensage!!, CHAT_ID,imageUri)
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
        service.sendNotification(notification).enqueue(object : Callback<Any> {
            override fun onResponse(call: retrofit2.Call<Any>, response: Response<Any>) {
                Log.i("TokenMessage", "Success ${response}----${response.isSuccessful}")
            }

            override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
                Log.i("TokenMessage", t.message.toString())
            }

        })
    }

    /** Resposta de la càmera */
    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val imageBitmap = intent?.extras?.get("data") as Bitmap
                //imageUris.add(intent)
                if (imageBitmap != null) {
                    imageUri =  bitmapToUri(imageBitmap)
                    conexion.sendMsg(Message(userId,"",""), CHAT_ID,imageUri)
                }
                //imageView.setImageBitmap(imageBitmap)
                CAMERA_EXTERNAL = 1
            }
        }
    private fun bitmapToUri(bitmap: Bitmap): Uri {
        // Obtener el directorio de la imagen en el almacenamiento externo de la aplicación
        val imagesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Crear un archivo temporal para guardar la imagen
        val file = File(imagesDirectory, "${UUID.randomUUID()}.jpg")

        // Comprimir la imagen y guardarla en el archivo temporal
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()

        // Devolver la Uri del archivo temporal
        return Uri.fromFile(file)
    }
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Permisos external storage */
    private fun isExternalPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Resposta a l'acció de l'usuari en validar o no els permisos */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            } else {
            }
        } else if (requestCode == EXTERNAL_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

}