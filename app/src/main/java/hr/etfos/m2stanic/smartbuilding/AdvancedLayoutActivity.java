package hr.etfos.m2stanic.smartbuilding;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AdvancedLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RadioButton rbOn;
    private RadioButton rbOff;
    private EditText etHours;
    private EditText etMinutes;

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
