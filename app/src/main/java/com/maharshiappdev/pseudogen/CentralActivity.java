package com.maharshiappdev.pseudogen;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CentralActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
//        setTitle(bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()).getTitle());
        setTitle("");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_code)
                {
                    Intent intent = new Intent(CentralActivity.this, CodeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

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
        FirebaseAuth.getInstance().signOut();
        //Sign out and go to login
        finish();
        Intent intent = new Intent(CentralActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}