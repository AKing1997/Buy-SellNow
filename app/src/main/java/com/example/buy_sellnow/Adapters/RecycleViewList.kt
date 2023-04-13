package com.example.buy_sellnow.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Activity.ChatDetail
import com.example.buy_sellnow.Activity.ProductDetail
import com.example.buy_sellnow.Model.Chat
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
import com.google.firebase.auth.FirebaseAuth
//import com.bumptech.glide.Glide
//import es.kankit.camkit.DB.DBHelper
//import es.kankit.camkit.Interficie.CryptoGecko
import kotlin.math.roundToInt

class RecycleViewList(
    val productList: ArrayList<Product>,
    val context: Context?,
    val chatList: ArrayList<Chat>

) : RecyclerView.Adapter<RecycleViewList.ViewHolder>() {
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    // Return cantidad de items en el recycler/lista
    override fun getItemCount(): Int {
        return productList.size;
    }

    //Asociar la información que queremos mostrar en el item list
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(context!!).load(productList[position].image[0]).into(holder.img)
        holder.title.text = productList[position].tituloDeProducto

        holder.chatItemR.setOnClickListener {
            if(productList[position] !=null){
                val redrige = Intent(context, ChatDetail::class.java);
                redrige.putExtra("PRODUCT_ID",  productList[position].productId)
                redrige.putExtra("CHAT_ID",  chatList[position].chatId)
                redrige.putExtra("USER_ID", userId)
                redrige.putExtra("PRODUCT_TITLE",  productList[position].tituloDeProducto)
                redrige.putExtra("PRODUCT_IMG",  productList[position].image[0])
                redrige.putExtra("PRODUCT_USER_ID",  productList[position].userId)
                context.startActivity(redrige)
            }
        }
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
        var chatItemR: CardView = view.findViewById(R.id.chatItemR)
    }
}