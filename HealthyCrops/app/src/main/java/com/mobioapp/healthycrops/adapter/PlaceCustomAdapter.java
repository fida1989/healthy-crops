package com.mobioapp.healthycrops.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.healthycrops.R;
import com.squareup.picasso.Picasso;


public class PlaceCustomAdapter extends BaseAdapter {

    int[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public PlaceCustomAdapter(Activity Activity,
                              int[] placesName, int[] placesImage) {
        // TODO Auto-generated constructor stub
        result = placesName;
        context = Activity;
        this.imageId = placesImage;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // Holder holder = new Holder();
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.place_list_item_layout, parent, false);
        }

        TextView tv = (TextView) rowView.findViewById(R.id.textViewRowItem);
        ImageView img = (ImageView) rowView.findViewById(R.id.imageViewPlace);

        // if (holder.img != null) {
        //new BitmapWorkerTask(holder.img).execute(imageId[position]);
        //}

        Picasso.with(context).load(imageId[position]).resize(150,150).into(img);
        tv.setText(result[position]);
        //img.setImageResource(imageId[position]);


        return rowView;
    }
}
