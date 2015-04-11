package com.mobioapp.healthycrops.adapter;


import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.healthycrops.R;
import com.mobioapp.healthycrops.model.PestModel;
import com.squareup.picasso.Picasso;

public class PestInformationAdapter extends BaseAdapter {


    private Context context;
    private List<PestModel> pestList;
    LayoutInflater inflater;


    private static class ViewHolder {
        ImageView pestImageView;
        TextView pestNameTextView;
    }

    public PestInformationAdapter(Context context, List<PestModel> pests) {
        this.context = context;
        this.pestList = pests;
        notifyDataSetChanged();


    }

    @Override
    public int getCount() {
        return pestList.size();
    }

    @Override
    public Object getItem(int position) {

        return pestList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup container) {
        ViewHolder viewHolder = null;
        if (convertview == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.pest_item, null);

            viewHolder = new ViewHolder();

            viewHolder.pestNameTextView = (TextView) convertview.findViewById(R.id.textViewPestName);
            viewHolder.pestImageView = (ImageView) convertview.findViewById(R.id.imageViewPest);

            convertview.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertview.getTag();
        }

        PestModel pest = pestList.get(position);
        viewHolder.pestNameTextView.setText(pest.getPestName());

        Picasso.with(context).load(pest.getPestImage()).into(viewHolder.pestImageView);
        return convertview;
    }


}