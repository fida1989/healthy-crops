package com.mobioapp.healthycrops;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {


    Intent intent;
    Button bUpdateForm, bPestInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bUpdateForm = (Button) findViewById(R.id.buttonForm);
        bPestInformation = (Button) findViewById(R.id.buttonPestInformation);

        bUpdateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MainFormActivity.class);
                startActivity(intent);
            }
        });
        bPestInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, PlaceInformationActivity.class);
                startActivity(intent);
            }
        });
    }



}
