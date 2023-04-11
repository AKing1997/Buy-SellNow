package com.example.buy_sellnow.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Activity.AddProduct
import com.example.buy_sellnow.Login
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.R
import com.example.buy_sellnow.Activity.EditUserDetail
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.google.android.gms.auth.api.signin.GoogleSignInClient


class Profile(): Fragment() {
    companion object {
        private lateinit var googleSignInClient : GoogleSignInClient;
        /** User Text View Detail variables **/
        private lateinit var profile_user_name: TextView
        private lateinit var profile_user_sell_detail: TextView
        private lateinit var profile_user_geo: TextView
        private lateinit var profile_rating_bar: RatingBar
        private lateinit var profile_user_img: ImageView

        private lateinit var preferences: SharedPreferences;
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false);
        var prom_list_view: RecyclerView = view.findViewById(R.id.prom_list_view);
        var productLis = ArrayList<GridViewProduct>();
        val profile_config_btn: Button = view.findViewById(R.id.profile_config_btn)
        preferences = view.context.getSharedPreferences(getString(R.string.sesionPref), Context.MODE_PRIVATE);
        val userId = preferences.getString("userId", null).toString()

        profile_user_name = view.findViewById(R.id.profile_user_name)
        profile_user_sell_detail = view.findViewById(R.id.profile_user_sell_detail)
        profile_user_geo = view.findViewById(R.id.profile_user_geo)
        profile_user_img = view.findViewById(R.id.profile_user_img)
        val conexion: FireBaseConexion = FireBaseConexion()
        conexion.getUserById(userId){
            if(it != null){
                profile_user_name.setText(it.firstName+" "+it.lastName)
                profile_user_geo.setText(it.direccion)
                Glide.with(view.context).load(it.userImage)
                    .into(profile_user_img)
            }
        }

        profile_config_btn.setOnClickListener {
            var redrige = Intent(view.context, EditUserDetail::class.java);
            startActivity(redrige);
        }

        var logOutBtn: ImageButton = view.findViewById(R.id.log_out);
        logOutBtn.setOnClickListener {
            signOut(view);
        }

        //prom_list_view.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        //val adapter : RecycleViewList = RecycleViewList(productLis, context);
        //prom_list_view.adapter = adapter;
        return view;
    }

    private fun signOut(view: View) {
        googleSignInClient.signOut().addOnCompleteListener {
            startActivity(Intent(view.context, Login()::class.java));
        }
    }

}