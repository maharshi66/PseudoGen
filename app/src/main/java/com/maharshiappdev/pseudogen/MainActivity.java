package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ImageView loadingImageView;
    TextView appNameTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void startLoadingAnimation()
    {
        loadingImageView.animate().rotation(3600).setDuration(2000);
    }

    public Boolean checkIfLoggedIn(FirebaseAuth user) {
        if(user.getCurrentUser() != null)
        {
            Toast.makeText(this, "Logged In!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTitle("");

        loadingImageView = findViewById(R.id.loadingImageView);
        appNameTextView = findViewById(R.id.appNameTextView);
        startLoadingAnimation();
        handleLoginAndSignUp();
    }

    public void handleLoginAndSignUp() {
        new CountDownTimer(2500,1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                loadingImageView.setVisibility(View.INVISIBLE);
                if(checkIfLoggedIn(mAuth))
                {
                    //TODO: Move to central activity
                    Intent intent = new Intent(getApplicationContext(), CentralActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        }.start();
    }
}