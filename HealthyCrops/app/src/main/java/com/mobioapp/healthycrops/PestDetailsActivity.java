package com.mobioapp.healthycrops;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.healthycrops.model.PestModel;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by arunavsikdershuvo on 4/8/15.
 */
public class PestDetailsActivity extends Activity {

    TextView tvPestName, tvPestDescription;
    ImageView ivPestImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pest_details_activity);

        tvPestName = (TextView) findViewById(R.id.pest_details_pest_name_tv);
        tvPestDescription = (TextView) findViewById(R.id.textView_pest_description);
        ivPestImage = (ImageView) findViewById(R.id.pest_details_pest_image_iv);

        String pestName = getIntent().getStringExtra("pest_name");
        String pestImage  = getIntent().getStringExtra("pest_image");
        String pestDescription = getIntent().getStringExtra("pest_description");

        tvPestName.setText(pestName);
        tvPestDescription.setText(pestDescription);
        Picasso.with(PestDetailsActivity.this).load(pestImage).into(ivPestImage);
    }
}
