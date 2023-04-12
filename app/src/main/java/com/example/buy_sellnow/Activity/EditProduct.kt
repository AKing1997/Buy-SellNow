package com.example.buy_sellnow.Activity

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Enum.ProductStatus
import com.example.buy_sellnow.Model.Enum.Status
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class EditProduct : AppCompatActivity() {
    companion object {
        lateinit var editproductStatusSpinner: Spinner
        lateinit var editProductDeliverySpinner: Spinner
        lateinit var editProductCategoriSpinner: Spinner

        lateinit var deleteProductBtnEdit: Button

        /* Layouts etc*/
        lateinit var editProductToolbar: MaterialToolbar
        lateinit var editProductBottomNav: BottomNavigationView

        /* Variables para spinner item selected */
        var productStatusSpinnerSelected: Int = 0
        var editProductDeliverySpinnerSelected: Int = 0
        lateinit var editProductCategoriSpinnerSelected: String
        var categoriaId: Int = 0

        /* Form input variables */
        lateinit var editTexteditProductBrand: EditText
        lateinit var editTexteditProductName: EditText
        lateinit var editTexteditProductDescription: EditText
        lateinit var editTexteditProductWeight: EditText
        lateinit var editTexteditProductPrice: EditText

        /* text view variables error message */
        lateinit var editTexteditProductBrandErMsg: TextView
        lateinit var editTexteditProductNameErMsg: TextView
        lateinit var editTexteditProductDescriptionErMsg: TextView
        lateinit var editTexteditProductWeightErMsg: TextView
        lateinit var editTexteditProductPriceErMsg: TextView
        lateinit var productStatusSpinnerErMsg: TextView
        lateinit var editProductDeliverySpinnerErMsg: TextView
        lateinit var editProductCategoriSpinnerErMsg: TextView

        /* Validation variable boolean for form*/
        var editTexteditProductNameBol: Boolean = false
        var editTexteditProductBrandBol: Boolean = false
        var editTexteditProductDescriptionBol: Boolean = false
        var editTexteditProductWeightBol: Boolean = false
        var editTexteditProductPriceBol: Boolean = false
        var productStatusSpinnerSelectedBol: Boolean = false
        var editProductDeliverySpinnerSelectedBol: Boolean = false
        var editProductCategoriSpinnerSelectedBol: Boolean = false
        lateinit var productId: String;
        lateinit var product: Product;
        val conexion: FireBaseConexion =  FireBaseConexion()
    }


    private fun validateForm(): Boolean {
        editTexteditProductNameBol = editTexteditProductName.text.isNotEmpty()
        editTexteditProductBrandBol = editTexteditProductBrand.text.isNotEmpty()
        editTexteditProductDescriptionBol = editTexteditProductDescription.text.isNotEmpty()
        editTexteditProductWeightBol = editTexteditProductWeight.text.isNotEmpty()
        editTexteditProductPriceBol = editTexteditProductPrice.text.isNotEmpty()
        productStatusSpinnerSelectedBol = (productStatusSpinnerSelected>0)
        editProductDeliverySpinnerSelectedBol = (editProductDeliverySpinnerSelected>0)
        editProductCategoriSpinnerSelectedBol = !(editProductCategoriSpinnerSelected.contains('!'))
        return (editTexteditProductBrandBol &&
                editTexteditProductNameBol &&
                editTexteditProductDescriptionBol &&
                editTexteditProductWeightBol &&
                editTexteditProductPriceBol &&
                productStatusSpinnerSelectedBol &&
                editProductDeliverySpinnerSelectedBol &&
                editProductCategoriSpinnerSelectedBol)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        productId = intent.getStringExtra("productId").toString();
        getProduct()
        editproductStatusSpinner = findViewById(R.id.editproductStatusSpinner)
        editProductDeliverySpinner = findViewById(R.id.editProductDeliverySpinner)
        editProductCategoriSpinner = findViewById(R.id.editProductCategoriSpinner)
        editProductToolbar = findViewById(R.id.editProductToolbar)
        editProductBottomNav = findViewById(R.id.editProductBottomNav)
        val editProductBtn: Button = findViewById(R.id.editProductBtn)
        deleteProductBtnEdit = findViewById(R.id.deleteProductBtnEdit)
        deleteProductBtnEdit.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Producto - ${product.tituloDeProducto}")
            builder.setMessage("¿Estas seguro de que deseas eliminar el producto ${product.tituloDeProducto}?")
            builder.setPositiveButton("Yes"){dialog, which ->
                conexion.deleteProduct(productId)
                onBackPressed()
            }
            builder.setNegativeButton("No") { dialog, which ->
                // No hacer nada
            }
            val dialog = builder.create()
            dialog.show()
        }
        /** Variales de Error Message  **/
        editTexteditProductBrandErMsg = findViewById(R.id.editTexteditProductBrandErMsg)
        editTexteditProductNameErMsg = findViewById(R.id.editTexteditProductNameErMsg)
        editTexteditProductDescriptionErMsg = findViewById(R.id.editTexteditProductDescriptionErMsg)
        editTexteditProductWeightErMsg = findViewById(R.id.editTexteditProductWeightErMsg)
        editTexteditProductPriceErMsg = findViewById(R.id.editTexteditProductPriceErMsg)
        productStatusSpinnerErMsg = findViewById(R.id.editproductStatusSpinnerErMsg)
        editProductDeliverySpinnerErMsg = findViewById(R.id.editProductDeliverySpinnerErMsg)
        editProductCategoriSpinnerErMsg = findViewById(R.id.editProductCategoriSpinnerErMsg)

        /** Variables de inputs **/
        editTexteditProductBrand = findViewById(R.id.editTexteditProductBrand)
        editTexteditProductName = findViewById(R.id.editTexteditProductName)
        editTexteditProductDescription = findViewById(R.id.editTexteditProductDescription)
        editTexteditProductWeight = findViewById(R.id.editTexteditProductWeight)
        editTexteditProductPrice = findViewById(R.id.editTexteditProductPrice)

        editProductBtn.setOnClickListener {
            if (!validateForm()) {
                showErrorMsg()
            }else{
                showErrorMsg()
                val date = LocalDateTime.now()
                product = Product(
                    product.productId,
                    editTexteditProductName.text.toString(),
                    editProductCategoriSpinnerSelected,
                    "",
                    editTexteditProductDescription.text.toString(),
                    editTexteditProductPrice.text.toString(),
                    editTexteditProductWeight.text.toString(),
                    "",
                    product.dateCreated,
                    date.toString(),
                    product.image,
                    "",
                    editTexteditProductBrand.text.toString(),
                    "0",
                    "",
                    Status.Activeted,

                    (if(1== productStatusSpinnerSelected) ProductStatus.Nuevo else
                        if(2== productStatusSpinnerSelected) ProductStatus.BuenEstado else
                            if(3== productStatusSpinnerSelected) ProductStatus.Bien else
                                if(4== productStatusSpinnerSelected) ProductStatus.Mejorar else TODO()),
                    editProductDeliverySpinnerSelected==1,
                    product.userId,
                    productStatusSpinnerSelected,
                    categoriaId
                )
                conexion.updateProduct(product)
                resetForm()
                onBackPressed()
            }
        }

        editProductToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getSpinnerItemSelected(
            editproductStatusSpinner,
            resources.getStringArray(R.array.status_product_list)
        )
        getSpinnerItemSelected(
            editProductDeliverySpinner,
            resources.getStringArray(R.array.delivery_list)
        )
        getSpinnerItemSelected(
            editProductCategoriSpinner,
            resources.getStringArray(R.array.category_array_list)
        )
    }

    private fun resetForm(){
        editTexteditProductName.text.clear()
        editTexteditProductDescription.text.clear()
        editTexteditProductPrice.text.clear()
        editTexteditProductWeight.text.clear()
        editTexteditProductBrand.text.clear()
    }

    private fun showErrorMsg() {
        editTexteditProductBrandErMsg.visibility = if (editTexteditProductBrandBol) View.GONE else View.VISIBLE
        editTexteditProductNameErMsg.visibility = if (editTexteditProductNameBol) View.GONE else View.VISIBLE
        editTexteditProductDescriptionErMsg.visibility = if (editTexteditProductDescriptionBol) View.GONE else View.VISIBLE
        editTexteditProductWeightErMsg.visibility = if (editTexteditProductWeightBol) View.GONE else View.VISIBLE
        editTexteditProductPriceErMsg.visibility = if (editTexteditProductPriceBol) View.GONE else View.VISIBLE
        productStatusSpinnerErMsg.visibility = if (productStatusSpinnerSelectedBol) View.GONE else View.VISIBLE
        editProductDeliverySpinnerErMsg.visibility = if (editProductDeliverySpinnerSelectedBol) View.GONE else View.VISIBLE
        editProductCategoriSpinnerErMsg.visibility = if (editProductCategoriSpinnerSelectedBol) View.GONE else View.VISIBLE
    }

    private fun getSpinnerItemSelected(s: Spinner, array: Array<String>) {
        if (s != null) {
            val aA = ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
            aA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            s.adapter = aA;
            s.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val item = parent!!.getItemAtPosition(position).toString();
                    val selectedSpinnerId = parent?.id ?: return
                    when (selectedSpinnerId) {
                        R.id.editproductStatusSpinner -> {
                            productStatusSpinnerSelected = position}
                        R.id.editProductDeliverySpinner -> {
                            editProductDeliverySpinnerSelected = position}
                        R.id.editProductCategoriSpinner -> {
                            editProductCategoriSpinnerSelected = item
                            categoriaId =position
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun getProduct(){
        conexion.getProductById(productId){
            product = it
            setValues()
        }
    }

    private fun setValues(){
        resources.getStringArray(R.array.status_product_list)
        resources.getStringArray(R.array.delivery_list)
        resources.getStringArray(R.array.category_array_list)

        editproductStatusSpinner.setSelection(product.estado)
        editProductDeliverySpinner.setSelection( if(product.delivery) 1 else 2)
        editProductCategoriSpinner.setSelection(product.categoriaId)
        editTexteditProductBrand.setText(product.marca)
        editTexteditProductName.setText(product.tituloDeProducto)
        editTexteditProductDescription.setText(product.description)
        editTexteditProductWeight.setText(product.peso)
        editTexteditProductPrice.setText(product.precio)
    }
}