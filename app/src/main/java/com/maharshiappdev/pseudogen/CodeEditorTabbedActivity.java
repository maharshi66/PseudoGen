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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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

public class CodeEditorTabbedActivity extends AppCompatActivity{
    private final DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private DrawerLayout navDrawerEditor;
    String postTitle = "";
    String postPseudocode = "";
    String postDescription = "";
    String postInput = "";
    String postOutput = "";
    Fragment editorFrag;
    BottomNavigationView bottomNavEditShortCuts;
    TextView codeInputTextView;
    TextView codeOutputTextView;
    boolean shortcutsChecked = true;
    DatabaseHandler db;

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
        //TODO Remove this code as it does not work: No spinner created
        /*MenuItem item = menu.findItem(R.id.action_fontChange);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fonts_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
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
                break;
        }
        return false;
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

    public String getAlgorithmTitle() {
        Intent intent = getIntent();
        String newTitle = "";
        if(intent.hasExtra("fromEditPseudocodeTitle"))
        {
          newTitle = intent.getStringExtra("fromEditPseudocodeTitle");
        }else
        {
            newTitle = intent.getStringExtra("pseudocodeTitle");
        }

        return newTitle;
    }

    public String getInputCodeText() {
        Intent intent = getIntent();
        String newInputText = "";
        if(intent.hasExtra("fromEditInput"))
        {
         newInputText = intent.getStringExtra("fromEditInput");
        }else
        {
            newInputText = intent.getStringExtra("pseudocodeInputText");
        }
        return newInputText;
    }

    public String getCodeDescriptionText() {
        Intent intent = getIntent();
        String newDescriptionText = "";
        if(intent.hasExtra("fromEditPseudocodeDescription"))
        {
         newDescriptionText = intent.getStringExtra("fromEditPseudocodeDescription");
        }else
        {
            newDescriptionText = intent.getStringExtra("pseudocodeDescription");
        }
        return newDescriptionText;
    }

    public String getOutputCodeText() {
        Intent intent = getIntent();
        String newOutputText = "";
        if(intent.hasExtra("fromEditOutput"))
        {
            newOutputText = intent.getStringExtra("fromEditOutput");
        }else
        {
            newOutputText = intent.getStringExtra("pseudocodeOutputText");
        }
        return newOutputText;
    }

    public void checkAndSave() {
        //TODO Disallow duplicate names and overwrite Posts if user wants to. Cannot create duplicate

        final LineNumberedEditText inputCodeEditText = findViewById(R.id.inputCodeEditText);
        postPseudocode = inputCodeEditText.getText().toString();
        if (!postDescription.isEmpty() && !postTitle.isEmpty()) {
//            writeToDatabase(inputCodeTitle, inputCode);
            Posts post = new Posts(postTitle, postDescription, postPseudocode, postInput, postOutput);
            if(db.isDuplicateTitle(postTitle))
            {
                createAlertForOverwrite();
                db.close();
            }else
            {
                db.addPseudocodePost(post);
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }else{
            Toast.makeText(this, "Save Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO Change function parameters as development changes
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
        String[] dataStructuresInput = getResources().getStringArray(R.array.data_structures_input);
        String[] dataStructuresOutput = getResources().getStringArray(R.array.data_structures_output);

        ArrayAdapter<String> codeInputArrayAdapter = new ArrayAdapter<String>(CodeEditorTabbedActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructuresInput);
        ArrayAdapter<String> codeOutputArrayAdapter = new ArrayAdapter<String>(CodeEditorTabbedActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructuresOutput);

        codeInputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeOutputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        codeInputSpinner.setAdapter(codeInputArrayAdapter);
        codeOutputSpinner.setAdapter(codeOutputArrayAdapter);

        alertDialog.setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postTitle = titleEditText.getText().toString();
                        postDescription = descriptionEditText.getText().toString();
                        postInput = codeInputSpinner.getSelectedItem().toString();
                        postOutput = codeOutputSpinner.getSelectedItem().toString();

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

    public void createAlertForOverwrite()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CutomAlertDialog);
        alertDialog.setMessage("Do you want to overwrite previous save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Open Database and overwrite where title matches
                        db.overWritePost(postTitle, postDescription, postPseudocode, postOutput, postInput);
                        Toast.makeText(getApplicationContext(), "Overwritten!", Toast.LENGTH_SHORT).show();
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_editor_tabbed);
        Toolbar editorToolBar = findViewById(R.id.editorToolbar);
        setSupportActionBar(editorToolBar);
        postTitle = getAlgorithmTitle();

        setTitle(getAlgorithmTitle());
//        getSupportActionBar().setIcon(R.drawable.code_icon);
        db = new DatabaseHandler(this);
        bottomNavEditShortCuts = findViewById(R.id.bottomNavEditShortCuts);
        codeInputTextView = findViewById(R.id.codeInputTextView);
        codeOutputTextView = findViewById(R.id.codeOutputTextView);

        codeInputTextView.setText("Input: \t" + getInputCodeText());
        codeOutputTextView.setText("Output: \t" + getOutputCodeText());

        postDescription = getCodeDescriptionText();
        postInput = getInputCodeText();
        postOutput = getOutputCodeText();
        editorFrag =  new CodeEditorFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.editor_fragment_container, editorFrag).commit();
    }
}