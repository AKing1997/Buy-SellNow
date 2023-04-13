package com.example.buy_sellnow.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
//import com.bumptech.glide.Glide
//import es.kankit.camkit.DB.DBHelper
//import es.kankit.camkit.Interficie.CryptoGecko
import kotlin.math.roundToInt

class RecycleViewList(
    val chatList: ArrayList<Product>,
    val context: Context?,
) : RecyclerView.Adapter<RecycleViewList.ViewHolder>() {
    // Return cantidad de items en el recycler/lista
    override fun getItemCount(): Int {
        return chatList.size;
    }

    //Asociar la información que queremos mostrar en el item list
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(context!!).load(chatList[position].image[0]).into(holder.img)
        holder.title.text = chatList[position].tituloDeProducto
    }

    //Devueve el item que voy a mostrar en el recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  layoutInflater = LayoutInflater.from(parent.context);
        return ViewHolder(layoutInflater.inflate(R.layout.activity_chat_member_list, parent, false))
    }

    //Definir los campos de mi item list
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var img: ImageView = view.findViewById(R.id.chatProdctImage);
        var title: TextView = view.findViewById(R.id.chatProductName);
    }
}