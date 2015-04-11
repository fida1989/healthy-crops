package com.mobioapp.healthycrops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobioapp.healthycrops.adapter.PlaceCustomAdapter;

/**
 * Created by arunavsikdershuvo on 4/8/15.
 */
public class PlaceInformationActivity extends Activity {

    ListView lvPestInformation;
    int[] placesName = { R.string.dhaka, R.string.barisal, R.string.chittagong, R.string.sylhet, R.string.rajsahi, R.string.khulna, R.string.mymensingh };
    int[] placesImage = { R.drawable.dhaka, R.drawable.barisal, R.drawable.chittagong, R.drawable.sylhet, R.drawable.rajshahi, R.drawable.khulna, R.drawable.mymensingh };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_information_activity);

        lvPestInformation = (ListView) findViewById(R.id.listViewPestInformation);
        lvPestInformation.setAdapter(new PlaceCustomAdapter(PlaceInformationActivity.this, placesName,
                placesImage));

        lvPestInformation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlaceInformationActivity.this, PestInformationActivity.class);
                startActivity(intent);
            }
        });
    }
}
