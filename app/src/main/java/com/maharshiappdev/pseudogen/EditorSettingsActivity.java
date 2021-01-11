package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.prefs.PreferenceChangeEvent;

public class EditorSettingsActivity extends AppCompatActivity {
    private AdView settingsAdViewBanner;
    SharedPreferences mPref;
    Spinner themeSpinner;
    Spinner fontStyleSpinner;
    Spinner fontSizeSpinner;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_editor_settings );
        Toolbar editorSettingsToolBar = findViewById ( R.id.editorSettingsToolbar );
        setSupportActionBar ( editorSettingsToolBar );
        setTitle ( "Editor Settings" );
        MobileAds.initialize(this);
        settingsAdViewBanner = findViewById(R.id.settingsAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        settingsAdViewBanner.loadAd(adRequest);

        mPref = PreferenceManager.getDefaultSharedPreferences ( this );
        themeSpinner = findViewById ( R.id.themeSpinner );
        fontStyleSpinner = findViewById ( R.id.fontStyleSpinner );
        fontSizeSpinner = findViewById ( R.id.fontSizeSpinner );
        String[] themes = {"Blackboard", "Whiteboard"};
        String[] fontStyle = {"Fira","Kalam","Ubuntu", "Source Code"};
        String[] fontSize = {"Small","Big"};
        ArrayAdapter <String> themesAdapter = new ArrayAdapter<String>( EditorSettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, themes);
        ArrayAdapter <String> fontStyleAdapter = new ArrayAdapter<String>( EditorSettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, fontStyle);
        ArrayAdapter <String> fontSizeAdapter = new ArrayAdapter<String>( EditorSettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, fontSize);
        themeSpinner.setAdapter ( themesAdapter );
        fontStyleSpinner.setAdapter ( fontStyleAdapter );
        fontSizeSpinner.setAdapter ( fontSizeAdapter );

        String savedTheme = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("theme","Blackboard");
        for(int i=0;i<themes.length;i++)
            if(savedTheme.equals(themeSpinner.getItemAtPosition(i).toString())){
                themeSpinner.setSelection(i);
                break;
            }

        String savedFontStyle =PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("fontStyle","Source Code");
        for(int i=0;i<fontStyle.length;i++)
            if(savedFontStyle.equals(fontStyleSpinner.getItemAtPosition(i).toString())){
                fontStyleSpinner.setSelection(i);
                break;
            }

        String savedFontSize=PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("fontSize","Small");

        for(int i=0;i<fontSize.length;i++)
            if(savedFontSize.equals(fontSizeSpinner.getItemAtPosition(i).toString())){
                fontSizeSpinner.setSelection(i);
                break;
            }
    }

    public void setEditorSettingsClicked ( View view ) {
        SharedPreferences.Editor prefEditor = mPref.edit();
        prefEditor.putString("theme",themeSpinner.getSelectedItem ().toString());
        prefEditor.putString("fontStyle",fontStyleSpinner.getSelectedItem().toString());
        prefEditor.putString("fontSize",fontSizeSpinner.getSelectedItem().toString());
        prefEditor.commit();
        Toast.makeText ( this , "Changed Editor Settings" , Toast.LENGTH_SHORT ).show ( );
    }
}