package ie.dit.societiesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import layout.QRGenFragment;
import layout.QRScanFragment;
import layout.SocietyFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        QRScanFragment.OnFragmentInteractionListener,
        SocietiesListFragment.OnFragmentInteractionListener,
        QRGenFragment.OnFragmentInteractionListener,
        SocietyFragment.OnFragmentInteractionListener,
        ChairToolsFragment.OnFragmentInteractionListener,
        UserDetailsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Gets tools
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Set the user's email in the navbar */
        View header = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) header.findViewById(R.id.userNameView);

        // Get the user's email from shared preferences
        SharedPreferences userData = getSharedPreferences("userData", 0);
        String userEmail = userData.getString("email", "-1");

        // Set the username view in the nav bar to the user's email
        navEmail.setText(userEmail);
    }

    public void onResume()
    {
        super.onResume();
        // Load the search view by default
        SocietiesListFragment societiesListFragment = SocietiesListFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.relative_layout_for_fragment,
                societiesListFragment,
                societiesListFragment.getTag()
        ).commit();
        Log.d("FRAGDEBUG", "Aftercommit");
        Log.d("LIFECYCLEDEBUG", "onResume");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    /*
        Method that checks the navigation bar and listens for a clicked option
    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_qr:
            {
                Toast.makeText(this, "QR", Toast.LENGTH_SHORT).show();
                // Handle the QR action

                //Creates fragment
                QRScanFragment qrScanFragment = QRScanFragment.newInstance();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        qrScanFragment,
                        qrScanFragment.getTag()
                ).commit();
                break;
            }

            case R.id.nav_soclist:
            {
                SocietiesListFragment societiesListFragment = SocietiesListFragment.newInstance();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        societiesListFragment,
                        societiesListFragment.getTag()
                ).commit();
                break;
            }

            case R.id.nav_user_details:
            {
                UserDetailsFragment userDetailsFragment = UserDetailsFragment.newInstance();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        userDetailsFragment,
                        userDetailsFragment.getTag()
                ).addToBackStack(userDetailsFragment.getTag())
                        .commit();
                break;
            }

            case R.id.logout:
            {
                // Delete locally stored session data
                SharedPreferences userData = getSharedPreferences("userData", 0);
                SharedPreferences.Editor editor = userData.edit();

                editor.remove("session_id");
                editor.remove("member_id");
                editor.commit();

                // Go to login activity and clear the back stack
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //Listens for data in the QR reader.
    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

}
