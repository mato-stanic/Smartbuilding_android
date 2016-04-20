package hr.etfos.m2stanic.smartbuilding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvancedLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RadioButton rbOn;
    private RadioButton rbOff;
    private EditText etHours;
    private EditText etMinutes;
    private Button btnSave;
    private Button currentTime;
    private Spinner spRooms;
    private CheckBox mon, tue, wed, thu, fri, sat, sun;
    private final List<CheckBox> allCheckBoxes = new ArrayList<CheckBox>();

//    stuff for post method
    private String room = "";
    private String action = "";
    private List<String> days = new ArrayList<>();

    private Long apartmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rbOn = (RadioButton) findViewById(R.id.radioButton);
        rbOff = (RadioButton) findViewById(R.id.radioButton2);
        etHours = (EditText) findViewById(R.id.etHours);
        etMinutes = (EditText) findViewById(R.id.etMinutes);
        btnSave = (Button) findViewById(R.id.btnSave);
        spRooms = (Spinner) findViewById(R.id.spRooms);
        currentTime = (Button) findViewById(R.id.btnCrnTime);

//        checkboxes for days
        mon = (CheckBox) findViewById(R.id.checkBox1);
        allCheckBoxes.add(mon);
        tue = (CheckBox) findViewById(R.id.checkBox2);
        allCheckBoxes.add(tue);
        wed = (CheckBox) findViewById(R.id.checkBox3);
        allCheckBoxes.add(wed);
        thu = (CheckBox) findViewById(R.id.checkBox4);
        allCheckBoxes.add(thu);
        fri = (CheckBox) findViewById(R.id.checkBox5);
        allCheckBoxes.add(fri);
        sat = (CheckBox) findViewById(R.id.checkBox6);
        allCheckBoxes.add(sat);
        sun = (CheckBox) findViewById(R.id.checkBox7);
        allCheckBoxes.add(sun);



        etHours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    int val = Integer.parseInt(etHours.getText().toString());
                    if (val < 0 || val > 24) {
                        etHours.setText("");
                        Toast.makeText(getApplicationContext(), "Vrijednost za sate mora biti od 0 do 24", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });

        etMinutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    int val = Integer.parseInt(etMinutes.getText().toString());
                    if (val < 0 || val > 59) {
                        etMinutes.setText("");
                        Toast.makeText(getApplicationContext(), "Vrijednost za minute mora biti od 0 do 59", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferenceUserData), Context.MODE_PRIVATE);
        String loggedInUser = sharedPref.getString("loggedInUser", "noData");
        JSONObject completeData = null;
        try {
            completeData = new JSONObject(loggedInUser);
            JSONObject apartmentData = completeData.getJSONObject("apartment");
            apartmentId = apartmentData.getLong("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        currentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date curDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("hh");
                String hours = format.format(curDate);
                etHours.setText(hours);

                format = new SimpleDateFormat("mm");
                String minutes = format.format(curDate);
                etMinutes.setText(minutes);
            }
        });
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

    public void rbClick(View v) {
        int id = v.getId();
        RadioButton rb = (RadioButton) findViewById(id);
        if(rb.getId() == R.id.radioButton){
            rbOff.setChecked(false);
        }
        else if (rb.getId() == R.id.radioButton2){
            rbOn.setChecked(false);
        }
    }

    public void saveClick(View v) {
        int id = v.getId();
        boolean actionError = false;
        boolean daysError = false;
        boolean timeError = false;

//        get selected room
        String selectedRoom = spRooms.getSelectedItem().toString();

        switch (selectedRoom){
            case "Dnevna soba": room = "livingroom";break;
            case "Kuhinja": room = "kitchen";break;
            case "Kupaonica": room = "bathroom";break;
            case "Spavaća soba": room = "bedroom";break;
            case "Hodnik": room = "hallway";break;
        }

//        get selected action
        if(!rbOn.isChecked() && !rbOff.isChecked()){
            actionError = true;
        }
        else if(rbOn.isChecked()){
            action = "turnOn";
        }
        else if(rbOff.isChecked()){
            action = "turnOff";
        }

//        get selected days
        boolean oneChecked = isAnyCheckboxChecked(allCheckBoxes);
        if(!oneChecked){
            daysError = true;
        }

//        clean list of selected days first
        days.clear();

        if(oneChecked){
            for (CheckBox cb : allCheckBoxes) {
                if(cb.isChecked()){
                    switch (cb.getText().toString()){
                        case "Ponedjeljak": days.add("monday");break;
                        case "Utorak": days.add("tuesday");break;
                        case "Srijeda": days.add("wednesday");break;
                        case "Četvrtak": days.add("thursday");break;
                        case "Petak": days.add("friday");break;
                        case "Subota": days.add("saturday");break;
                        case "Nedjelja": days.add("sunday");break;
                    }
                }
            }
        }

        if(StringUtils.isEmpty(etHours.getText()) || StringUtils.isEmpty(etMinutes.getText())){
            timeError = true;
        }
        processPostMethod(actionError, daysError, timeError);

    }

    private void processPostMethod(Boolean actionError, Boolean daysError, Boolean timeError){
        if(actionError || daysError || timeError){
            if(actionError)
                Toast.makeText(getApplicationContext(), "Barem jedna akcija mora biti označena", Toast.LENGTH_LONG).show();
            if(daysError)
                Toast.makeText(getApplicationContext(), "Barem jedan dan mora biti odabran", Toast.LENGTH_LONG).show();
            if(timeError)
                Toast.makeText(getApplicationContext(), "Upišite vrijeme", Toast.LENGTH_LONG).show();
        }
        else{
            String time = etHours.getText() + ":" + etMinutes.getText();
            ApartmentLayout.advancedLayout(getApplicationContext(), days, apartmentId, room, action, time);
        }
    }

    public boolean isAnyCheckboxChecked(List<CheckBox> checkboxesList) {
        for(CheckBox checkbox : checkboxesList)
            if(checkbox.isChecked())
                return true;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.advanced_layout, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_simple) {
            Intent intent = new Intent(getApplicationContext(), SimpleLayoutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_advanced) {
            Intent intent = new Intent(getApplicationContext(), AdvancedLayoutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cron_list) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
