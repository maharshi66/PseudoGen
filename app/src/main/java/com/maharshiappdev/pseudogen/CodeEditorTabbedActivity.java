package com.maharshiappdev.pseudogen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maharshiappdev.pseudogen.ui.main.SectionsPagerAdapter;

public class CodeEditorTabbedActivity extends AppCompatActivity/* implements NavigationView.OnNavigationItemSelectedListener */{
    private final DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private DrawerLayout navDrawerEditor;
    String postTitle = "";
    String postPseudocode = "";
    String postDescription = "";
    String postInput = "";
    String postOutput = "";
    String postTime = "";
    String postSpace = "";
    String defaultPrintHundredOddTitle = "";
    String defaultPrintHundredOddCode = "";
    Boolean isPrintHundredOddClicked = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.code_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.checkAndSave:
                checkAndSave();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Discard draft")
                .setCancelable(true)
                .setMessage("Do you want to discard this draft?")
                .setPositiveButton("Yes, discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CodeEditorTabbedActivity.this, CentralActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No, continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public String getTabActivityTitle() {
        Intent intent = getIntent();
        String newTitle = "";
        newTitle = intent.getStringExtra("pseudocodeTitle");
        return newTitle;
    }

    public String getInputCodeText() {
        Intent intent = getIntent();
        String newInputText = "";
        newInputText = intent.getStringExtra("pseudocodeInputText");
        return newInputText;
    }

    public String getOutputCodeText() {
        Intent intent = getIntent();
        String newOutputText = "";
        newOutputText = intent.getStringExtra("pseudocodeOutputText");
        return newOutputText;
    }

    public Boolean isPrintOddClicked() {
        getFromSharedPref();
        Intent intent = getIntent();
        isPrintHundredOddClicked = intent.getBooleanExtra("defaultPrintHundredOddClicked", false);
        return isPrintHundredOddClicked;
    }

    public void getFromSharedPref() {
        SharedPreferences appSharedPref = getSharedPreferences("com.maharshiappdev.pseudogen", Context.MODE_PRIVATE);
        defaultPrintHundredOddTitle = appSharedPref.getString("inputCodeTitle", "");
        defaultPrintHundredOddCode = appSharedPref.getString("inputCode", "");
    }

    public void checkAndSave() {
        final LineNumberedEditText inputCodeEditText = findViewById(R.id.inputCodeEditText);
        postPseudocode = inputCodeEditText.getText().toString();
        postDescription = "Description";
        postInput = "Input";
        postOutput = "Output";
        postTime = "Time";
        postSpace = "Space";

        if (!isPrintHundredOddClicked && !postTitle.isEmpty() && !postPseudocode.isEmpty()) {
//            writeToDatabase(inputCodeTitle, inputCode);
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            //TODO Change id, description as per content in encountered in app
            Posts post = new Posts(4, postTitle, postDescription, postPseudocode,postInput, postOutput, postTime, postSpace);
            db.addPseudocodePost(post);
            db.close();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        } else if (isPrintHundredOddClicked) {
            inputCodeEditText.setText(defaultPrintHundredOddCode);
        }
    }

    //TODO Change function parameters as development changes
    //TODO Add complexities as well (Space and Time)
    public void writeToDatabase(String title, String code) {
        //Write Pseudocode Title and corresponding code to Database
        firebaseDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts").child("Code List").child(title).setValue(code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_code_editor_tabbed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.code_icon);
        tabs.getTabAt(1).setIcon(R.drawable.analysis_icon);
        FloatingActionButton fab = findViewById(R.id.compileCodeButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Add action for Code Analysis button clicked
            }
        });

        Toolbar editorToolBar = findViewById(R.id.editorToolbar);
        setSupportActionBar(editorToolBar);
//        navigationViewEditor.setNavigationItemSelectedListener(this);

        if (isPrintOddClicked()) {
            postTitle = defaultPrintHundredOddTitle;
        } else {
            postTitle = getTabActivityTitle();
        }

        setTitle(postTitle);
        TextView codeInputTextView = findViewById(R.id.codeInputTextView);
        TextView codeOutputTextView = findViewById(R.id.codeOutputTextView);

        codeInputTextView.setText("Input: \t" + getInputCodeText());
        codeOutputTextView.setText("Output: \t" + getOutputCodeText());
    }
}