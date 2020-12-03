package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class LearningWebViewActivity extends AppCompatActivity {
    WebView learningWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_web_view);
        learningWebView = findViewById(R.id.learningWebView);
        String loadArrayData = "";
        String title = "";
        Intent intent = getIntent();
        title = intent.getStringExtra("groupName");
        int groupPos = intent.getIntExtra("groupPosition", -1);
        int childPos = intent.getIntExtra("childPosition", -1);

        if(groupPos == 1)
        {
            switch (childPos)
            {
                case 0:
                    loadArrayData = "<!DOCTYPE html>\n" +"<html>\n" +
                            "<title>Arrays</title>\n" +
                            "<body>\n" +
                            "\n" +
                            "<h1>Arrays</h1>\n" +
                            "<p>" + getResources().getString(R.string.arrays_intro) +
                            "</p>\n" +
                            "</body>\n" +
                            "</html>";
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
        setTitle(title);
        learningWebView.loadData(loadArrayData,"text/html", "UTF-8" );
    }
}