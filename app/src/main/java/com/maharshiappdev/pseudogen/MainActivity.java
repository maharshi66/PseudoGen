package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView loadingImageView;
    public void startLoadingAnimation()
    {
        loadingImageView.animate().rotation(7200).setDuration(5000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingImageView = findViewById(R.id.loadingImageView);
        startLoadingAnimation();

        /*Check Sign-In STATE
          If signed in, move to CodeEditor Activity
          Else Move to Log-in Activity
        */
/*        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);*/
    }
}