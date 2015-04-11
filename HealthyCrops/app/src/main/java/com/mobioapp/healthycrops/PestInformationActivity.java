package com.mobioapp.healthycrops;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobioapp.healthycrops.adapter.PestInformationAdapter;
import com.mobioapp.healthycrops.helper.ConnectionDetector;
import com.mobioapp.healthycrops.helper.JSONParser;
import com.mobioapp.healthycrops.model.PestModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arunavsikdershuvo on 4/8/15.
 */
public class PestInformationActivity extends Activity {

    private static final String TAG = PestInformationActivity.class.getSimpleName();


    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();


    SwipeRefreshLayout mSwipeRefreshLayout;
    List<PestModel> pestsList;
    ConnectionDetector cd;

    JSONArray pests = null;
    JSONParser jsonParser;
    String json = null;
    String path = null;
    public static JSONObject c;
    JSONObject json1;
    boolean boolProgressDialog = false;

    PestInformationAdapter pestInformationAdapter;
    ListView lvPestInformation;
    ProgressBar pestProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pest_information_activity);

        pestsList = new ArrayList<PestModel>();
        cd = new ConnectionDetector(PestInformationActivity.this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        pestProgressBar = (ProgressBar) findViewById(R.id.progressBarPestInformation);
        pestProgressBar.setVisibility(View.GONE);
        lvPestInformation = (ListView) findViewById(R.id.listViewPestInformation);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                refreshContent();

            }
        });

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        lvPestInformation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PestInformationActivity.this, PestDetailsActivity.class);

                String pest_name = pestsList.get(position).getPestName();
                System.out.println(pest_name);
                String pest_image = pestsList.get(position).getPestImage();
                System.out.println(pest_image);
                String pest_description = pestsList.get(position).getPestDescription();
                intent.putExtra("pest_name", pest_name);
                intent.putExtra("pest_image", pest_image);
                intent.putExtra("pest_description", pest_description);
                startActivity(intent);
            }
        });

        path = "jsondata/pest_data.json";

        if (cd.isConnectingToInternet() || !cd.isConnectingToInternet()) {

            new pestParser().execute();
        }
    }


    public class pestParser extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);


            lvPestInformation.setAdapter(pestInformationAdapter);

                pestProgressBar.setVisibility(View.GONE);
                boolProgressDialog = true;



        }

        @Override
        protected Void doInBackground(Void... params) {


            if (cd.isConnectingToInternet()) {

                jsonParser = new JSONParser();
                json1 = jsonParser.getJSONFromUrl("http://www.mocky.io/v2/552930b922258f5600a37894");


                try {
                    System.out.print(json1.toString());
                    pests = (JSONArray) json1.getJSONArray("details");
                    for (int i = 0; i < pests.length(); i++) {
                        c = pests.getJSONObject(i);


                        PestModel pest = new PestModel();

                        pest.setPestId(c.getString("pest_id"));
                        pest.setPestName(c.getString("pest_name"));
                        pest.setPestImage(c.getString("pest_image"));
                        pest.setPestDescription(c.getString("pest_description"));
                        pestsList.add(pest);
                    }


                    Log.d(TAG, pests.toString());


                    pestInformationAdapter = new PestInformationAdapter(PestInformationActivity.this, pestsList);


                } catch (JSONException e1) {

                    e1.printStackTrace();
                }
            } else {

                try {
                    json = convertJsonToStringFromAssetFolder(path,
                            getApplicationContext());
                } catch (IOException e1) {

                    e1.printStackTrace();
                }

                JSONObject jsonobject;
                try {
                    System.out.print(json.toString());
                    jsonobject = new JSONObject(json);

                    pests = (JSONArray) jsonobject.getJSONArray("details");
                    for (int i = 0; i < pests.length(); i++) {
                        c = pests.getJSONObject(i);


                        PestModel pest = new PestModel();

                        pest.setPestId(c.getString("pest_id"));
                        pest.setPestName(c.getString("pest_name"));
                        pest.setPestImage(c.getString("pest_image"));
                        pest.setPestDescription(c.getString("pest_description"));
                        pestsList.add(pest);
                    }


                    Log.d(TAG, pests.toString());


                    pestInformationAdapter = new PestInformationAdapter(PestInformationActivity.this, pestsList);


                } catch (JSONException e1) {

                    e1.printStackTrace();
                }
            }

            return null;

        }

        private String convertJsonToStringFromAssetFolder(String path, Context context) throws IOException {
            AssetManager manager = context.getAssets();
            InputStream file = manager.open(path);

            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            return new String(data);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            if (boolProgressDialog == false) {
                pestProgressBar.setVisibility(View.VISIBLE);
            }
            super.onPreExecute();


        }


    }


   /* private void refreshContent() {

        pestsList.removeAll(pestsList);
        //pestInformationAdapter.notifyDataSetChanged();
        new pestParser().execute();
        mSwipeRefreshLayout.setRefreshing(false);
    }*/

    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pestsList.removeAll(pestsList);
                new pestParser().execute();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 3000);

    }
}