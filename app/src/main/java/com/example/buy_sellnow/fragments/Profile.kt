package com.example.buy_sellnow.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Login
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient


class Profile(): Fragment() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001;
        private const val EXTERNAL_PERMISSION_REQUEST_CODE = 1002;
        private var CAMERA_EXTERNAL = 0; //1 CAM - 2 EXTERNAL
        private lateinit var googleSignInClient : GoogleSignInClient;
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false);
        var prom_list_view: RecyclerView = view.findViewById(R.id.prom_list_view);
        var productLis = ArrayList<GridViewProduct>();
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

    // Permisos camera photo
    private fun isCameraPermissionGranted(v: View): Boolean {
        return ContextCompat.checkSelfPermission(v.context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Profile.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening camera
            } else {
                // Permission denied, handle accordingly
            }
        }else if(requestCode == Profile.EXTERNAL_PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening camera
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}