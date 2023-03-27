package com.example.buy_sellnow.Activity

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Adapters.ImageRecycleView
import com.example.buy_sellnow.R

class FullScreenImageView: AppCompatActivity() {
     lateinit var imageUris: ArrayList<Bitmap>;
    companion object {
        lateinit var adapter: ImageRecycleView;
        lateinit var recycleView: RecyclerView;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image_view)
        imageUris = intent.extras?.getParcelableArrayList("listas")!!
        recycleView = findViewById(R.id.FullScreenImageViewRecy);
        recycleView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        adapter = ImageRecycleView(imageUris, this, false);
        recycleView.adapter = adapter;
    }
}