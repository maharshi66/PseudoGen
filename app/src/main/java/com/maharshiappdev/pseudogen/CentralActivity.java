package com.maharshiappdev.pseudogen;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CentralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FragmentTransaction fragmentTransaction;
    Fragment selectedFragment = null;
    String actionBarTitle = "";
    FloatingActionButton fab_addNew;
    private DrawerLayout navDrawer;
    AutoCompleteTextView codeListSearchEditText;
    ArrayAdapter<String> autoCompleteArrayAdapter;
    List<String> listDataHeaders;
    BottomNavigationView navigation;
    BottomNavigationView codeItemNavigation;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_logOut:
                alertUserForSignOut();
                break;
            case R.id.nav_developer:
                //TODO Add alert giving background on the developer and why this app was made (succinctly)
                break;
            case R.id.nav_rateApp:
                //Opens  Playstore for rating the app
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                startActivity(intent);

                //TODO uncomment once app is on Playstore
/*                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
                break;
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_data_structures:
                    selectedFragment = new HomeFragment();
                    switchFragmentHome(selectedFragment);
                    break;
                case R.id.action_code:
                    selectedFragment = new CodeListFragment();
                    switchFragmentCodeList(selectedFragment);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private void switchFragmentHome(Fragment fragment)
    {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void switchFragmentCodeList(Fragment codeListFragment)
    {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, codeListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void createAlertForAddNew()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CutomAlertDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.add_new_code_dialog, null);
        final EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        final Spinner codeInputSpinner = dialogView.findViewById(R.id.codeInputSpinner);
        final Spinner codeOutputSpinner = dialogView.findViewById(R.id.codeOutputSpinner);
        String[] dataStructures = getResources().getStringArray(R.array.data_structures);

        ArrayAdapter<String> codeInputArrayAdapter = new ArrayAdapter<String>(CentralActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructures);
        ArrayAdapter<String> codeOutputArrayAdapter = new ArrayAdapter<String>(CentralActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructures);

        codeInputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeOutputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        codeInputSpinner.setAdapter(codeInputArrayAdapter);
        codeOutputSpinner.setAdapter(codeOutputArrayAdapter);

        alertDialog.setView(dialogView)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String codeTitleText = "";
                        String codeDescriptionText = "";
                        String codeInputText = "";
                        String codeOutputText = "";
                        codeTitleText = titleEditText.getText().toString();
                        codeDescriptionText = descriptionEditText.getText().toString();
                        codeInputText = codeInputSpinner.getSelectedItem().toString();
                        codeOutputText = codeOutputSpinner.getSelectedItem().toString();

                        if(!codeTitleText.isEmpty() && !codeDescriptionText.isEmpty() &&
                            !codeInputText.isEmpty() && !codeOutputText.isEmpty())
                        {
                            Intent intent = new Intent(CentralActivity.this, CodeEditorTabbedActivity.class);
                            intent.putExtra("pseudocodeTitle", codeTitleText);
                            intent.putExtra("pseudocodeDescription", codeDescriptionText);
                            intent.putExtra("pseudocodeInputText", codeInputText);
                            intent.putExtra("pseudocodeOutputText", codeOutputText);
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(CentralActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
/*        AlertDialog dialog = alertDialog.create();
        dialog.show();

        //TODO Helps Increase Size of the Dialog but doesnt extend the View with it. FIX!
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth);
        int dialogWindowHeight = (int) (displayHeight * 0.80f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);*/
    }

    public void alertUserForSignOut()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CentralActivity.this);
        alertDialog.setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void signOut()
    {
        //Sign out and go to login
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(CentralActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            alertUserForSignOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        setTitle(actionBarTitle);

        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.action_code);
        listDataHeaders = new ArrayList<>();
        fab_addNew = findViewById(R.id.fab_addNew);
        fab_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertForAddNew();
            }
        });

        Toolbar centralToolbar = findViewById(R.id.centralToolBar);
        navDrawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navDrawer, centralToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
}