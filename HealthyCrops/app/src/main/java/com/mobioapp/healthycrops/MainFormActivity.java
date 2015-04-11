package com.mobioapp.healthycrops;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.mobioapp.healthycrops.helper.ConnectionDetector;
import com.mobioapp.healthycrops.helper.GPSTracker;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import nl.changer.polypicker.ImagePickerActivity;


/**
 * Created by arunavsikdershuvo on 4/7/15.
 */
public class MainFormActivity extends Activity {


    private TextView latitudeField, longitudeField, tvCurrentDate;
    private EditText etPestName, etAddressField, etCropAffected, etPestAttackTime, etCropAffectedPercentage, etPestControlInfo;
    double latitude, longitude;
    String stringImagePath;
    String cityName, stateName, countryName;
    String pest_name, user_address, crop_affected, pest_attack_time, crop_affected_percentage, pest_control_info, pest_image;
    Button bForSubmit;
    ConnectionDetector cd;
    Intent intent;
    ImageView ivPestImage;
    int INTENT_REQUEST_GET_IMAGES = 14;
    ProgressDialog mProgress;
    Calendar rightNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form_activity);

        cd = new ConnectionDetector(MainFormActivity.this);

        etPestName = (EditText) findViewById(R.id.editTextNamePest);
        etAddressField = (EditText) findViewById(R.id.editTextLocation);
        etCropAffected = (EditText) findViewById(R.id.editTextCropAffected);
        latitudeField = (TextView) findViewById(R.id.textViewLatitude);
        longitudeField = (TextView) findViewById(R.id.textViewLongitude);
        etPestAttackTime = (EditText) findViewById(R.id.editTextPestAttackTime);
        tvCurrentDate = (TextView) findViewById(R.id.textViewCurrentDate);
        etCropAffectedPercentage = (EditText) findViewById(R.id.editTextCropPercentage);
        etPestControlInfo = (EditText) findViewById(R.id.editTextControlInfo);
        ivPestImage = (ImageView) findViewById(R.id.image1);
        bForSubmit = (Button) findViewById(R.id.buttonFormSubmit);

        ivPestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });

        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude(); // returns latitude
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        latitudeField.setText("Latitude: " + String.valueOf(latitude));
        longitudeField.setText("Longitude :" + String.valueOf(longitude));


        if (cd.isConnectingToInternet()) {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                cityName = addresses.get(0).getAddressLine(0);
                stateName = addresses.get(0).getAddressLine(1);
                countryName = addresses.get(0).getAddressLine(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder builder = new StringBuilder();
            builder.append("CityName: ");
            builder.append(cityName);
            builder.append("\n");
            builder.append("StateName: ");
            builder.append(stateName);
            builder.append("\n");
            builder.append("CountryName: ");
            builder.append(countryName);
            etAddressField.setText(builder.toString());


        } else {
            Toast.makeText(getApplicationContext(), "cannot retrieve address\nNo Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        rightNow = Calendar.getInstance();
        int minute = rightNow.get(Calendar.MINUTE);
        int hour = rightNow.get(Calendar.HOUR);
        int date = rightNow.get(Calendar.DATE);
        //int month = rightNow.get(Calendar.MONTH);
        int year = rightNow.get(Calendar.YEAR);

        System.out.println(minute);
        System.out.println(String.valueOf(hour));
        System.out.println(date);
        //System.out.println(month);
        String month_name = (new SimpleDateFormat("MMMM").format(rightNow.getTime()));
        System.out.println(year);
        int ampm = rightNow.get(Calendar.AM_PM);
        String m = new String();
        System.out.println("AM_PM::: " + ampm);
        if (ampm == 0) {
            m = "AM";
        } else {
            m = "PM";
        }

        tvCurrentDate.setText("Current Time: " + String.valueOf(hour) + "::" + String.valueOf(minute) + " " + m + "\n" + "Current Date: " + String.valueOf(date) + " " + month_name + ", " + year);

        bForSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFormData();
            }
        });
    }


    private void getImages() {

        Intent intent = new Intent(MainFormActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 1);  // allow only upto 3 images to be selected.
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (parcelableUris == null) {
                    return;
                }
                // show images using uris returned.
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                System.out.println("all uris: " + uris);
                stringImagePath = uris[0].getPath().toString();
                if (uris != null) {
                    Picasso.with(MainFormActivity.this).load(new File(uris[0].getPath().toString())).error(R.drawable.upload_error).into(ivPestImage);
                }
            }
        }
    }

    public void getFormData() {

        pest_name = etPestName.getText().toString();
        user_address = etAddressField.getText().toString();
        crop_affected = etCropAffected.getText().toString();
        pest_attack_time = etPestAttackTime.getText().toString();
        crop_affected_percentage = etCropAffectedPercentage.getText().toString();
        pest_control_info = etPestControlInfo.getText().toString();
        pest_image = stringImagePath;


        if (pest_name.length() > 0 && user_address.length() > 0 && crop_affected.length() > 0 && pest_attack_time.length() > 0 && crop_affected_percentage.length() > 0 && pest_control_info.length() > 0 && stringImagePath != null) {

            doRegister();

        } else {

            Toast toast = Toast.makeText(MainFormActivity.this,
                    "Fill Required Info!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    private void doRegister() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("pest_name", pest_name);
        params.put("user_address", user_address);
        params.put("crop_affected", crop_affected);
        params.put("pest_attack_time", pest_attack_time);
        params.put("crop_affected_percentage", crop_affected_percentage);
        params.put("pest_control_info", pest_control_info);

        Bitmap bitmap = BitmapFactory.decodeFile(stringImagePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        params.put("pest_image", image_str);

        client.post(MainFormActivity.this, "http://mobioapp.net/apps/nandos/public/add_messsage", params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();

                        etPestName.setText("");
                        etAddressField.setText("");
                        etCropAffected.setText("");
                        etPestAttackTime.setText("");
                        etCropAffectedPercentage.setText("");
                        etPestControlInfo.setText("");
                        mProgress.dismiss();
                    }

                    @Override
                    public void onStart() {
                        // called before request is started
                        mProgress = new ProgressDialog(MainFormActivity.this);
                        mProgress.setMessage("Registering...");
                        mProgress.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] response) {


                        //   Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        // called when response HTTP status is "200 OK"
                        try {
                            JSONObject jsonResult = new JSONObject(new String(
                                    response));

                            System.out.println("Return JSON Object: "
                                    + jsonResult.toString());

                            String msg = jsonResult.getString("message");

                            if (msg.equals("success")) {

                                JSONObject user = jsonResult
                                        .getJSONObject("data");
                                System.out.println("Success JSON: "
                                        + user.toString());

                            /*    User.Id = user.getString("id");
                                User.Name = user.getString("name");
                                User.Address = user.getString("address");
                                User.Email = user.getString("email");
                                User.Phone = user.getString("phone_no");

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id",
                                        user.getString("id"));
                                // Storing
                                editor.putBoolean("log_track", true); // string
                                editor.commit(); // commit changes*/

                                Toast toast = Toast.makeText(
                                        getApplicationContext(),
                                        "Registration Successfull!",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        MainFormActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            } else {

                                Toast toast = Toast.makeText(MainFormActivity.this,
                                        "Registration Failure!",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            }

                        } catch (Exception e) {

                            Toast toast = Toast.makeText(getApplicationContext(), "Error!",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            //System.out.println("JSON Parse Error: " + result);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401,
                        // 403, 404)
                        Toast toast = Toast.makeText(MainFormActivity.this, "Error!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
    }


}