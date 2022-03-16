package com.mubo.genetoussdk.adapter;

import android.app.Activity;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mubo.genetous_imageload.imageLoad;
import com.mubo.genetous_imageload.imageLoadBuilder;
import com.mubo.genetoussdk.ImageLoad;
import com.mubo.genetoussdk.R;
import com.mubo.genetoussdk.models.Item;

import java.util.List;

public class PhotoAdaptor extends ArrayAdapter<Item> {
    private List<Item> feedItems;
    private Activity cx;
    pItemRow itemRow;
    private LayoutInflater layoutInflater;
    public PhotoAdaptor(Activity cx, List<Item> feedItems){
        super(cx, R.layout.activity_main, feedItems);
        this.feedItems = feedItems;
        this.cx = cx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            layoutInflater = (LayoutInflater) cx.getSystemService(cx.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(R.layout.image_item, parent, false);
            itemRow = new pItemRow();
            itemView.setTag(itemRow);
        } else {
            itemView = convertView;
            try {
                itemRow = (pItemRow) itemView.getTag();
            }catch (Exception ex){
                String exx=ex.toString();
            }
        }
        itemRow.img = (ImageView) itemView.findViewById(R.id.img);
        //if(!ImageLoad.flinging)
            new imageLoadBuilder()
                    .setActivity(cx)
                    .setImageUrl(feedItems.get(position).getUrl())
                    .setImageView(itemRow.img)
                    .setPlaceholder(R.mipmap.placeholder)
                    .setRequiredSize(new Size(100,100))
                    .load();
        return itemView;
    }
    private class pItemRow {
        ImageView img;
    }
}
