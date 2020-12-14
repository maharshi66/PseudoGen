package com.maharshiappdev.pseudogen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeEditorFragment extends Fragment{
    private final DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
    View view;
    LineNumberedEditText inputCodeEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_code_editor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onActivityCreated(savedInstanceState);
        inputCodeEditText = view.findViewById(R.id.inputCodeEditText);

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra("fromEditPseudocode"))
        {
            inputCodeEditText.setText(intent.getStringExtra("fromEditPseudocode"));
        }

        inputCodeEditText.addTextChangedListener(new TextWatcher() {
            ColorScheme keywords = new ColorScheme(
                    Pattern.compile(
                            "\\b(process|endprocess|define|initialize|solve|each|void|char|int|long|double|float|const|static|volatile|byte|boolean|class|interface|private|protected|public|final|abstract|enum|instanceof|assert|if|else|then|switch|case|default|break|goto|return|for|while|endloop|do|continue|throw|try|catch|finally|this|implements|import|true|false|null|N|Graph|List|Set|Queue|Stack|Array|BinaryTree|LinkedList|Heap|heapify|fetch|get|find)\\b"),
                    getActivity().getResources().getColor(R.color.dark_orange)
            );

            ColorScheme numbers = new ColorScheme(
                    Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)"),
                    Color.BLUE
            );
            final ColorScheme[] schemes = { keywords, numbers };
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                removeSpans(s, ForegroundColorSpan.class);
                for (ColorScheme scheme : schemes) {
                    for(Matcher m = scheme.pattern.matcher(s); m.find();) {
                        s.setSpan(new ForegroundColorSpan(scheme.color),
                                m.start(),
                                m.end(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
                CharacterStyle[] spans = e.getSpans(0, e.length(), type);
                for (CharacterStyle span : spans) {
                    e.removeSpan(span);
                }
            }

            class ColorScheme {
                final Pattern pattern;
                final int color;

                ColorScheme(Pattern pattern, int color) {
                    this.pattern = pattern;
                    this.color = color;
                }
            }
        });

        BottomNavigationView navigation = getActivity().findViewById(R.id.bottomNavEditShortCuts);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //Shortcuts Listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int start, end;
            switch (item.getItemId()) {
                case R.id.action_tab:
                    String tab = "\t\t";
                    start = Math.max(inputCodeEditText.getSelectionStart(), 0);
                    end = Math.max(inputCodeEditText.getSelectionEnd(), 0);
                    inputCodeEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                            tab, 0, tab.length());
                    break;
                case R.id.action_semicolon:
                    String semicolon = ";";
                    start = Math.max(inputCodeEditText.getSelectionStart(), 0);
                    end = Math.max(inputCodeEditText.getSelectionEnd(), 0);
                    inputCodeEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                            semicolon, 0, semicolon.length());
                    break;
                case R.id.action_ifelse:
                    String ifElse = "if\nelse";
                    start = Math.max(inputCodeEditText.getSelectionStart(), 0);
                    end = Math.max(inputCodeEditText.getSelectionEnd(), 0);
                    inputCodeEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                            ifElse, 0, ifElse.length());
                    break;
                case R.id.action_for:
                    String for_str = "for\nendloop";
                    start = Math.max(inputCodeEditText.getSelectionStart(), 0);
                    end = Math.max(inputCodeEditText.getSelectionEnd(), 0);
                    inputCodeEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                            for_str, 0, for_str.length());
                    break;
                case R.id.action_while:
                    String while_str = "while\nendloop";
                    start = Math.max(inputCodeEditText.getSelectionStart(), 0);
                    end = Math.max(inputCodeEditText.getSelectionEnd(), 0);
                    inputCodeEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                            while_str, 0, while_str.length());
                    break;
                default:
                    break;
            }
            return true;
        }
    };
}