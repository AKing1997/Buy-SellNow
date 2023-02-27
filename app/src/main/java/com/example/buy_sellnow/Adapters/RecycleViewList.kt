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
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
//import com.bumptech.glide.Glide
//import es.kankit.camkit.DB.DBHelper
//import es.kankit.camkit.Interficie.CryptoGecko
import kotlin.math.roundToInt

class RecycleViewList(
    listas: ArrayList<GridViewProduct>,
    context: Context?,
) : RecyclerView.Adapter<RecycleViewList.ViewHolder>() {
    var listas: ArrayList<GridViewProduct> = listas;
    var context: Context? = context;
    // Return cantidad de items en el recycler/lista
    override fun getItemCount(): Int {
        return listas.size;
    }

    //Asociar la información que queremos mostrar en el item list
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.product_price_card_promo.setText(listas[position].precio);
        holder.product_title_card_promo.setText(listas[position].productTitle)
    }

    //Devueve el item que voy a mostrar en el recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  layoutInflater = LayoutInflater.from(parent.context);
        return ViewHolder(layoutInflater.inflate(R.layout.product_card_item_prom, parent, false))
    }


    //Definir los campos de mi item list
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var product_img_card_promo: ImageView = view.findViewById(R.id.product_img_card_promo);
        var product_price_card_promo: TextView = view.findViewById(R.id.product_price_card_promo);
        var product_title_card_promo: TextView = view.findViewById(R.id.product_title_card_promo);
    }
}