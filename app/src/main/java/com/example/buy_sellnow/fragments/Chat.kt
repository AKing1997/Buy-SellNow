package com.example.buy_sellnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Adapters.RecycleViewList
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.R
import com.google.firebase.auth.FirebaseAuth

class Chat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_chat, container, false);
        val conexion: FireBaseConexion = FireBaseConexion()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val chatRecyclerView: RecyclerView = view.findViewById(R.id.chatRecyclerView)

        conexion.getAllChatByUserId(userId){
            if(it != null){
                if(it.size>0){
                    var productIds: ArrayList<String> = ArrayList()
                    for (productId in it){
                        productIds.add(productId.productId)
                    }
                    conexion.getProductsByIds(productIds){pr->
                        if (pr != null){
                            chatRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false);
                            val adapter : RecycleViewList = RecycleViewList(pr, view.context);
                            chatRecyclerView.adapter = adapter;
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
        return view;
    }
}