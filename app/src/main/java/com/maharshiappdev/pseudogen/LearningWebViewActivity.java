package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class LearningWebViewActivity extends AppCompatActivity {
    WebView learningWebView;

    public String getArrayOperationTable()
    {   String table = "<table border=2>" +
            "<tr>" +
            "<td></td>" +
            "<td><b>Worst Case</b></td>" +
            "</tr>" +
            "<tr>" +
            "<td>Space</td>" +
            "<td><i>O(n)</i></td>" +
            "</tr>" +
            "<td>Access/Lookup</td>" +
            "<td><i>O(1)</i></td>" +
            "</tr>" +
            "<td>Append</td>" +
            "<td><i>O(1)</i></td>" +
            "</tr>" +
            "<td>Insert</td>" +
            "<td><i>O(n)</i></td>" +
            "</tr>" +
            "<td>Delete</td>" +
            "<td><i>O(n)</i></td>" +
            "</tr>" +
            "</table>";
        return table;
    }

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
                            "<ul><b>Pros</b>" + "<li>" + getResources().getString(R.string.arrays_pro1)+"</li>"
                            + "<li>" + getResources().getString(R.string.arrays_pro2) + "</li>" +
                            "</ul>" +
                            "<ul><b>Cons</b>" + "<li>" + getResources().getString(R.string.arrays_con1) +"</li>" +
                            "<li>" + getResources().getString(R.string.arrays_con2) + "</li>" +
                            "</ul>" +
                            //Add Table
                            getArrayOperationTable() +
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