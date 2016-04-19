package hr.etfos.m2stanic.smartbuilding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ApartmentLayoutInitTask aptLayoutInitTask = null;
    private Switch livingRoom;
    private Switch kitchen;
    private Switch bathroom;
    private Switch bedroom;
    private Switch hallway;
    private Long apartmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferenceUserData), Context.MODE_PRIVATE);
        String loggedInUser = sharedPref.getString("loggedInUser", "noData");
        aptLayoutInitTask = new ApartmentLayoutInitTask(loggedInUser);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_simple) {
            // Handle the camera action
        } else if (id == R.id.nav_advanced) {

        } else if (id == R.id.nav_cron_list) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchClick(View v) {
        int id = v.getId();
        Switch sw = (Switch) findViewById(id);
        switch (id){
            case R.id.switch1: ApartmentLayout.changeRoomState(getApplicationContext(), apartmentId, "living_room", sw.isChecked());break;
            case R.id.switch2: ApartmentLayout.changeRoomState(getApplicationContext(), apartmentId, "kitchen", sw.isChecked());break;
            case R.id.switch3: ApartmentLayout.changeRoomState(getApplicationContext(), apartmentId, "bathroom", sw.isChecked());break;
            case R.id.switch4: ApartmentLayout.changeRoomState(getApplicationContext(), apartmentId, "bedroom", sw.isChecked());break;
            case R.id.switch5: ApartmentLayout.changeRoomState(getApplicationContext(), apartmentId, "hallway", sw.isChecked());break;
        }
    }

    public class ApartmentLayoutInitTask extends AsyncTask<Void, Void, Boolean> {

        private final String loggedInUser;
        private String apartmentLayout;
        private Integer statusCode;

        ApartmentLayoutInitTask(String loggedInUser) {
            this.loggedInUser = loggedInUser;
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
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout");
//            HttpPost httppost = new HttpPost("http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout");
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

                }
                else{
                    return false;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), loginSuccessful, Toast.LENGTH_LONG).show();
            }
//            else {
//                if(statusCode == HttpStatus.SC_NOT_ACCEPTABLE){
//
//                }
//                else{
//
//                }
//
//            }
        }

    }
}
