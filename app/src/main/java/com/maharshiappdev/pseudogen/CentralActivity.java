package com.maharshiappdev.pseudogen;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CentralActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    Fragment selectedFragment = null;
    String actionBarTitle = "";
    FloatingActionButton fab_addNew;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    selectedFragment = new HomeFragment();
                    switchFragmentHome(selectedFragment);
                    break;
                case R.id.action_code:
                    selectedFragment = new CodeListFragment();
                    switchFragmentCodeList(selectedFragment);
                    break;
                case R.id.action_premium:
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        alertUserForSignOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         switch(item.getItemId())
         {
             case R.id.signOut:
                 alertUserForSignOut();
                 break;
             case R.id.rateApp:
                 //TODO Rate App Alert Dialog
                 break;
             default:
                 break;
         }
         return true;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        BottomNavigationView navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.action_code);
        setTitle(actionBarTitle);

        fab_addNew = findViewById(R.id.fab_addNew);
        fab_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertForAddNew();
            }
        });
    }

    public void createAlertForAddNew()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.add_new_code_dialog, null);
        final EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        alertDialog.setTitle("Add New")
                .setView(dialogView)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Take the title, input and output for cloud storage and updating codesList

                        if(!titleEditText.getText().toString().isEmpty())
                        {
                            Intent intent = new Intent(CentralActivity.this, CodeEditorTabbedActivity.class);
                            intent.putExtra("pseudocodeTitle", titleEditText.getText().toString());
                            startActivity(intent);
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
    }
}