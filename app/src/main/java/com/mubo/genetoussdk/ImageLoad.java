package com.mubo.genetoussdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mubo.genetous_imageload.LazyLoad;
import com.mubo.genetoussdk.adapter.PhotoAdaptor;
import com.mubo.genetoussdk.models.Item;

import java.util.ArrayList;

public class ImageLoad extends AppCompatActivity {

    ListView lw;
    public static boolean flinging=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_load);
        lw=findViewById(R.id.lw);
        LazyLoad.create();
        ArrayList<Item> items=new ArrayList<>();
        for(int i=1; i<500; ++i){
            Item a=new Item("https://picsum.photos/id/"+String.valueOf(i)+"/200/300");
            items.add(a);
        }
        PhotoAdaptor c=new PhotoAdaptor(this,items);
        lw.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i != AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    flinging = false;
                } else {
                    flinging = true;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        lw.setAdapter(c);
    }
}