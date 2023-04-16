package com.example.buy_sellnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Adapters.RecycleViewList
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Product
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
        val nonChatProductMsg: TextView = view.findViewById(R.id.nonChatProductMsg)

        conexion.getAllChatByUserId(userId) { chatList ->
            if (chatList != null) {
                nonChatProductMsg.visibility = View.GONE
                var productIds: ArrayList<String> = ArrayList()
                var chatIds: ArrayList<String> = ArrayList()
                for (chat in chatList) {
                    chatIds.add(chat.chatId)
                    productIds.add(chat.productId)
                }

                    conexion.getAllPublicacion { productList ->
                        val filteredProductList: MutableMap<String, Product> = mutableMapOf()
                        if (productList != null && productList.size > 0) {
                            for (product in productList) {
                                val index = productIds.indexOf(product.productId)
                                if (index >= 0) {
                                    filteredProductList.put(chatIds[index], product)
                                }
                            }
                        }
                        chatRecyclerView.layoutManager =
                            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                        val adapter: RecycleViewList =
                            RecycleViewList(filteredProductList, view.context, chatList)
                        chatRecyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    nonChatProductMsg.visibility = View.VISIBLE
                }
        }
        return view;
    }
}