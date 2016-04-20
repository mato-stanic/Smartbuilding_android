package hr.etfos.m2stanic.smartbuilding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CronListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ApartmentCronInitTask apartmentCronInitTask = null;
    private Long apartmentId;
    private ListView lvCrons;
    ListCronsAdapter listCronsAdapter;
    Context context;
    private TextView tvNoCrons;

    private static final String PXS_RXS_UPDATE = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cron_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        context = this;
        tvNoCrons = (TextView) findViewById(R.id.tvNoCrons);
        tvNoCrons.setVisibility(View.GONE);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferenceUserData), Context.MODE_PRIVATE);
        String loggedInUser = sharedPref.getString("loggedInUser", "noData");
        apartmentCronInitTask = new ApartmentCronInitTask(loggedInUser, this);
        apartmentCronInitTask.execute((Void) null);
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

    public class ApartmentCronInitTask extends  AsyncTask<Void, Void, Boolean>{

        private final String loggedInUser;
        private final Context context;
        private String apartmentCrons;
        private Integer statusCode;

        ApartmentCronInitTask(String loggedInUser, Context context){
            this.loggedInUser = loggedInUser;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
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
            HttpPost httppost = new HttpPost("http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/cronList");
//            HttpPost httppost = new HttpPost("http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/cronList");
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
                    apartmentCrons = ParseResponse.iStream_to_String(is);
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
                List<ApartmentCronJob> cronList = new ArrayList<>();
                try {
                    JSONArray jsonarray = new JSONArray(apartmentCrons);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        ApartmentCronJob acj = new ApartmentCronJob();

                        Long id = jsonobject.getLong("id");
                        acj.setId(id);

                        String time = jsonobject.getString("time");
                        acj.setTime(time);

                        String action = jsonobject.getString("action");
                        if(action.equals("turnOn"))
                            acj.setAction("Upali");
                        else
                            acj.setAction("Ugasi");

                        String room = jsonobject.getString("room");
                        switch (room) {
                            case "livingroom":
                                acj.setRoom("Dnevna soba");
                                break;
                            case "kitchen":
                                acj.setRoom("Kuhinja");
                                break;
                            case "bathroom":
                                acj.setRoom("Kupaonica");
                                break;
                            case "bedroom":
                                acj.setRoom("Spavaća soba");
                                break;
                            case "hallway":
                                acj.setRoom("Hodnik");
                                break;
                        }

                        JSONArray listOfDays = jsonobject.getJSONArray("days");
                        List<String> correctedDays = new ArrayList<>();
                        for (int j = 0; j < listOfDays.length(); j++) {
                            String day = listOfDays.getString(j);
                            switch (day) {
                                case "monday":
                                    correctedDays.add("Ponedjeljak");
                                    break;
                                case "tuesday":
                                    correctedDays.add("Utorak");
                                    break;
                                case "wednesday":
                                    correctedDays.add("Srijeda");
                                    break;
                                case "thursday":
                                    correctedDays.add("Četvrtak");
                                    break;
                                case "friday":
                                    correctedDays.add("Petak");
                                    break;
                                case "saturday":
                                    correctedDays.add("Subota");
                                    break;
                                case "sunday":
                                    correctedDays.add("Nedjelja");
                                    break;
                            }
                        }
                        acj.setDays(correctedDays);

                        cronList.add(acj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                lvCrons = (ListView) findViewById(R.id.listView);
                listCronsAdapter = new ListCronsAdapter((CronListActivity) context, cronList);
                lvCrons.setAdapter(listCronsAdapter);
            }
            else {
                if(statusCode != null && statusCode == HttpStatus.SC_NOT_FOUND) {
                    tvNoCrons.setText(getResources().getString(R.string.error_nocrons));
                    tvNoCrons.setVisibility(View.VISIBLE);
                }
                else{
                    tvNoCrons.setText(getResources().getString(R.string.error));
                    tvNoCrons.setVisibility(View.VISIBLE);
                }

            }
        }
    }


}
