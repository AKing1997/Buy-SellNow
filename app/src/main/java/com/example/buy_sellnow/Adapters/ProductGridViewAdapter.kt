package com.example.buy_sellnow.Adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Activity.FullScreenImageView
import com.example.buy_sellnow.Activity.ProductDetail
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
internal class ProductGridViewAdapter(
    private val productList: ArrayList<Product>,
    private val context: Context
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var product_title_card: TextView
    private lateinit var product_price_card: TextView
    private lateinit var product_img_card: ImageView
    private lateinit var product_fav_btn_card: ImageButton

    override fun getCount(): Int {
        return productList.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var view = view;
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        }
        if (view == null) {
            view = layoutInflater!!.inflate(R.layout.product_view_card_item, null);
        }
        product_price_card = view!!.findViewById(R.id.product_price_card);
        product_title_card = view!!.findViewById(R.id.product_title_card);
        product_fav_btn_card = view!!.findViewById(R.id.product_fav_btn_card);
        product_img_card = view!!.findViewById(R.id.product_img_card);

        Glide.with(context!!).load(productList.get(position).image[0])
            .into(product_img_card)
        product_price_card.setText(productList.get(position).precio + " €");
        product_title_card.setText(productList.get(position).tituloDeProducto);
        var cardView: CardView = view.findViewById(R.id.card_sell);
        cardView.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                var redrige = Intent(context, ProductDetail::class.java)
                redrige.putExtra("productId", productList[position].productId)
                context.startActivity(redrige);
            }
        })
        return view;
    }
}
