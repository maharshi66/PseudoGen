package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void signInWithEmailClicked(View view)
    {
        Intent intent = new Intent(this, EmailSignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
          FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Sign In");
    }
}