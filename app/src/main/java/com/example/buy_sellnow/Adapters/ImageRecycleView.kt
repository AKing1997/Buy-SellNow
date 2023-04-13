package com.example.buy_sellnow.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Activity.FullScreenImageView
import com.example.buy_sellnow.R

class ImageRecycleView(
    listas: ArrayList<Uri>,
    context: Context,
    b: Boolean,
) : RecyclerView.Adapter<ImageRecycleView.ViewHolder>() {
    var listas: ArrayList<Uri> = listas
    var context: Context = context
    var b: Boolean = b
    /** Return cantidad de items en el recycler/lista */
    override fun getItemCount(): Int {
        return listas.size
    }

    /** Asociar la información que queremos mostrar en el item list */
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(holder.itemView)
            .load(listas.get(position))
            .into(holder.imageView)
        //holder.imageView.setImageURI(listas.get(position))
        if(b){
            holder.imageView.setOnClickListener {
                var redrige = Intent(context, FullScreenImageView::class.java)
                redrige.putExtra("listas", listas)
                context.startActivity(redrige)
            }
        }else{
            holder.imageView.setOnClickListener {
                (context as? Activity)?.finish()
            }
        }
    }
    /** Devueve el item que voy a mostrar en el recycler */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.image_item_view, parent, false))
    }


    /** Definir los campos de mi item list */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var imageView: ImageView = view.findViewById(R.id.image_item_view)
    }
}