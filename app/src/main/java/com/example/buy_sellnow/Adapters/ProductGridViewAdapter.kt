package com.example.buy_sellnow.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R

// on below line we are creating an
// adapter class for our grid view.
internal class ProductGridViewAdapter(
    // on below line we are creating two
    // variables for course list and context
    private val productList: ArrayList<Product>,
    private val context: Context
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var product_title_card: TextView
    private lateinit var product_price_card: TextView
    private lateinit var product_img_card: ImageView
    private lateinit var product_fav_btn_card: ImageButton

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return productList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var view = view;
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (view == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            view = layoutInflater!!.inflate(R.layout.product_view_card_item, null);
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        product_price_card = view!!.findViewById(R.id.product_price_card);
        product_title_card = view!!.findViewById(R.id.product_title_card);
        product_fav_btn_card = view!!.findViewById(R.id.product_fav_btn_card);
        product_img_card = view!!.findViewById(R.id.product_img_card);
        // on below line we are setting image for our course image view.
        product_price_card.setText(productList.get(position).precio + "€");
        // on below line we are setting text in our course text view.
        product_title_card.setText(productList.get(position).tituloDeProducto);
        // at last we are returning our convert view.

        var cardView: CardView = view.findViewById(R.id.card_sell);
        cardView.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(
                    view.context, productList[position].tituloDeProducto + " selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return view;
    }
}
