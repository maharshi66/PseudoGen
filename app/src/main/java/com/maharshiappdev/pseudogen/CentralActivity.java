package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class CentralActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
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