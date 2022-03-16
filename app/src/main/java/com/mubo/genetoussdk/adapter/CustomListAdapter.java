package com.mubo.genetoussdk.adapter;

import android.app.Activity;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mubo.genetous_imageload.imageLoadBuilder;
import com.mubo.genetoussdk.R;
import com.mubo.genetoussdk.models.Item;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Activity act; //context
    private ArrayList<Item> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Activity act, ArrayList<Item> items) {
        this.act = act;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(act).
                    inflate(R.layout.image_item, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
        ImageView img = (ImageView)
                convertView.findViewById(R.id.img);
        new imageLoadBuilder()
                .setActivity(act)
                .setImageUrl(items.get(position).getUrl())
                .setImageView(img)
                .setPlaceholder(R.mipmap.placeholder)
                .setRequiredSize(new Size(100,100))
                .load();

        // returns the view for the current row
        return convertView;
    }
}
