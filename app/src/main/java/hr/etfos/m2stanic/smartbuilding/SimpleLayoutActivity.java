package hr.etfos.m2stanic.smartbuilding;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import hr.etfos.m2stanic.smartbuilding.Extra.Config;

public class SimpleLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ApartmentLayoutInitTask aptLayoutInitTask = null;
    private Switch livingRoom;
    private Switch kitchen;
    private Switch bathroom;
    private Switch bedroom;
    private Switch hallway;
    private Long apartmentId;
    private TextView textViewError;

    private static final String PXS_RXS_UPDATE = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        livingRoom = (Switch) findViewById(R.id.switch1);
        kitchen = (Switch) findViewById(R.id.switch2);
        bathroom = (Switch) findViewById(R.id.switch3);
        bedroom = (Switch) findViewById(R.id.switch4);
        hallway = (Switch) findViewById(R.id.switch5);

        livingRoom.setOnClickListener(clicks);
        kitchen.setOnClickListener(clicks);
        bathroom.setOnClickListener(clicks);
        bedroom.setOnClickListener(clicks);
        hallway.setOnClickListener(clicks);

        textViewError = (TextView) findViewById(R.id.textViewError);
        textViewError.setVisibility(View.GONE);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferenceUserData), Context.MODE_PRIVATE);
        String loggedInUser = sharedPref.getString("loggedInUser", "noData");
        aptLayoutInitTask = new ApartmentLayoutInitTask(loggedInUser, getApplicationContext());
        aptLayoutInitTask.execute((Void) null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_simple) {
            Intent intent = new Intent(getApplicationContext(), SimpleLayoutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_advanced) {
            Intent intent = new Intent(getApplicationContext(), AdvancedLayoutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cron_list) {
            Intent intent = new Intent(getApplicationContext(), CronListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            sharedPref.edit().remove(PXS_RXS_UPDATE).apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void switchClick(View v) {
//        int id = v.getId();
//        Switch sw = (Switch) findViewById(id);
//        switch (id){
//            case R.id.switch1: ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "living_room", sw.isChecked());break;
//            case R.id.switch2: ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "kitchen", sw.isChecked());break;
//            case R.id.switch3: ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "bathroom", sw.isChecked());break;
//            case R.id.switch4: ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "bedroom", sw.isChecked());break;
//            case R.id.switch5: ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "hallway", sw.isChecked());break;
//        }
//    }

    View.OnClickListener clicks = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Switch sw = (Switch) findViewById(id);
            switch (id) {
                case R.id.switch1:
                    ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "living_room", sw.isChecked());
                    break;
                case R.id.switch2:
                    ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "kitchen", sw.isChecked());
                    break;
                case R.id.switch3:
                    ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "bathroom", sw.isChecked());
                    break;
                case R.id.switch4:
                    ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "bedroom", sw.isChecked());
                    break;
                case R.id.switch5:
                    ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "hallway", sw.isChecked());
                    break;
                case 6:
                    int thumbColor;
                    if(sw.isChecked()){
                        thumbColor = Color.argb(255, 45, 73, 77);
                        sw.setChecked(true);
                    }
                    else{
                        thumbColor = Color.argb(255, 236, 236, 236);
                        sw.setChecked(false);
                    }
                    sw.getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                    ApartmentManager.changeRoomState(sw, getApplicationContext(), apartmentId, "motionDetection", sw.isChecked());
                    break;
            }

        }
    };

    public class ApartmentLayoutInitTask extends AsyncTask<Void, Void, Boolean> {

        private final String loggedInUser;
        private final Context context;
        private String apartmentLayout;
        private Integer statusCode;

        ApartmentLayoutInitTask(String loggedInUser, Context context) {
            this.loggedInUser = loggedInUser;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            JSONObject completeData = null;
            try {
                completeData = new JSONObject(loggedInUser);
                JSONObject apartmentData = completeData.getJSONObject("apartment");
                apartmentId = apartmentData.getLong("id");

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost;
            if(Config.productionDeploy){
                httppost = new HttpPost(Config.prodApiApartmentLayout);
            }else{
                httppost = new HttpPost(Config.apiApartmentLayout);
            }
            try {
                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("apartmentId", String.valueOf(apartmentId)));


                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                // Execute HTTP Post Request

                HttpResponse response = httpclient.execute(httppost);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    apartmentLayout = ParseResponse.iStream_to_String(is);
                    return true;
                }
                else{
                    return false;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                System.out.println("apartment layout: " + apartmentLayout);
                try {
                    JSONObject completeData = new JSONObject(apartmentLayout);
                    livingRoom.setChecked(completeData.getBoolean("livingRoom"));
                    hallway.setChecked(completeData.getBoolean("hallway"));
                    kitchen.setChecked(completeData.getBoolean("kitchen"));
                    bathroom.setChecked(completeData.getBoolean("bathroom"));
                    bedroom.setChecked(completeData.getBoolean("bedroom"));

                    boolean motionDetectionEnabled = completeData.getBoolean("motionDetectionEnabled");
                    if (motionDetectionEnabled){
                        RelativeLayout layout = (RelativeLayout) findViewById(R.id.simpleEditRelLayout);

                        Switch sw = new Switch(context);

                        sw.setId(Integer.valueOf(6));
                        sw.setText("Detektiranje pokreta");

                        //stuff for layout params
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0,100,0,0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_START);
                        params.addRule(RelativeLayout.BELOW, R.id.switch5);
                        sw.setLayoutParams(params);
                        sw.setOnClickListener(clicks);
                        sw.setTextColor(Color.parseColor("black"));

                        boolean motionDetection = completeData.getBoolean("motionDetection");
                        int trackColor = Color.argb(255, 0, 0, 0);
                        int thumbColor;
                        if(motionDetection){
                            thumbColor = Color.argb(255, 45, 73, 77);
                            sw.setChecked(true);
                        }
                        else{
                            thumbColor = Color.argb(255, 236, 236, 236);
                            sw.setChecked(false);
                        }

                        sw.getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                        sw.getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);
                        layout.addView(sw);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                livingRoom.setClickable(false);
                livingRoom.setEnabled(false);
                hallway.setClickable(false);
                hallway.setEnabled(false);
                kitchen.setClickable(false);
                kitchen.setEnabled(false);
                bathroom.setClickable(false);
                bathroom.setEnabled(false);
                bedroom.setClickable(false);
                bedroom.setEnabled(false);
                textViewError.setVisibility(View.VISIBLE);

            }
        }

    }
}
