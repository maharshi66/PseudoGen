package com.maharshiappdev.pseudogen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    BottomNavigationView bottomNavEditShortCuts;
    TextView codeInputTextView;
    TextView codeOutputTextView;
    boolean shortcutsChecked = true;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem action_shortcuts = menu.findItem(R.id.action_shortcuts);
        action_shortcuts.setChecked(shortcutsChecked);
        return true;
    }

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
            case R.id.action_editDetails:
                createAlertForEditDetails();
                break;
            case R.id.action_backHome:
                createAlertForBackHome();
                break;
            case R.id.action_shortcuts:
                shortcutsChecked = !item.isChecked();
                item.setChecked(shortcutsChecked);
                if(shortcutsChecked)
                {
                    bottomNavEditShortCuts.setVisibility(View.VISIBLE);
                }else{
                    bottomNavEditShortCuts.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public void createAlertForBackHome()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit Editor")
                .setCancelable(true)
                .setMessage("Do you want to exit the editor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CodeEditorTabbedActivity.this, CentralActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit Editor")
                .setCancelable(true)
                .setMessage("Do you want to exit the editor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CodeEditorTabbedActivity.this, CentralActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    public String getCodeDescriptionText() {
        Intent intent = getIntent();
        String newInputText = "";
        newInputText = intent.getStringExtra("pseudocodeDescription");
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

        if (!isPrintHundredOddClicked && !postTitle.isEmpty() && !postPseudocode.isEmpty()) {
//            writeToDatabase(inputCodeTitle, inputCode);
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            //TODO Change id, time and space as per content in encountered in app
            Posts post = new Posts(10, postTitle, postDescription, postPseudocode, postInput, postOutput, postTime, postSpace);
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

    public void createAlertForEditDetails()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CutomAlertDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.edit_post_details_dialog, null);
        final EditText titleEditText = dialogView.findViewById(R.id.postTitleEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.postDescriptionEditText);
        final Spinner codeInputSpinner = dialogView.findViewById(R.id.postInputSpinner);
        final Spinner codeOutputSpinner = dialogView.findViewById(R.id.postOutputSpinner);
        String[] dataStructures = getResources().getStringArray(R.array.data_structures);

        ArrayAdapter<String> codeInputArrayAdapter = new ArrayAdapter<String>(CodeEditorTabbedActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructures);
        ArrayAdapter<String> codeOutputArrayAdapter = new ArrayAdapter<String>(CodeEditorTabbedActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructures);

        codeInputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeOutputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        codeInputSpinner.setAdapter(codeInputArrayAdapter);
        codeOutputSpinner.setAdapter(codeOutputArrayAdapter);

        alertDialog.setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!titleEditText.getText().toString().isEmpty() ||
                                !descriptionEditText.getText().toString().isEmpty())
                        {
                            postTitle = titleEditText.getText().toString();
                            postDescription = descriptionEditText.getText().toString();
                            postInput = codeInputSpinner.getSelectedItem().toString();
                            postOutput = codeOutputSpinner.getSelectedItem().toString();
                        }

                        if(!postTitle.isEmpty() && !postInput.isEmpty() && !postOutput.isEmpty())
                        {
                            getSupportActionBar().setTitle(postTitle);
                            codeInputTextView.setText("Input: \t" + postInput);
                            codeOutputTextView.setText("Output: \t" + postOutput);
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

        if (isPrintOddClicked()) {
            postTitle = defaultPrintHundredOddTitle;
        } else {
            postTitle = getTabActivityTitle();
        }

        setTitle(postTitle);
        bottomNavEditShortCuts = findViewById(R.id.bottomNavEditShortCuts);
        codeInputTextView = findViewById(R.id.codeInputTextView);
        codeOutputTextView = findViewById(R.id.codeOutputTextView);

        codeInputTextView.setText("Input: \t" + getInputCodeText());
        codeOutputTextView.setText("Output: \t" + getOutputCodeText());

        postDescription = getCodeDescriptionText();
        postInput = getInputCodeText();
        postOutput = getOutputCodeText();
        //TODO Change this after Analysis code written, placeholders
        postTime = "Time";
        postSpace = "Space";
    }
}