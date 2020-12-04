package com.maharshiappdev.pseudogen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
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
        super.onActivityCreated(savedInstanceState);
        inputCodeEditText = view.findViewById(R.id.inputCodeEditText);

        inputCodeEditText.addTextChangedListener(new TextWatcher() {
            Map<String,Integer> map = new HashMap<>();
            ForegroundColorSpan span;
            public void setUpMap()
            {
                map.put("process",Color.RED);
                map.put("endProcess",Color.RED);
                map.put("char",Color.GREEN);
                map.put("string",Color.GREEN);
                map.put("int",Color.GREEN);
                map.put("for",Color.BLUE);
                map.put("while",Color.BLUE);
                map.put("do",Color.BLUE);
                map.put("endLoop",Color.BLUE);
                map.put("if",Color.BLUE);
                map.put("else",Color.BLUE);
                map.put("endIf",Color.BLUE);
                map.put("endIf",Color.BLUE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                setUpMap();
                String editString = editable.toString();
                //Split on white space
                String[] splitStrArray = editString.split("\\s");
                int start = 0, beginIdx = 0, endIdx = 0, color = 0, count = 0;

                for (int i = 0; i < splitStrArray.length; i++) {
                    String splitStr = splitStrArray[i];
                    if (map.containsKey(splitStr)) {
                        count = (editString.length() - editString.replaceAll(splitStr, "").length()) / splitStr.length();
                        color = map.get(splitStr);
                        span = new ForegroundColorSpan(color);
                        for(int j = 0; j < count; j++)
                        {
                            beginIdx = editString.indexOf(splitStr, start);
                            endIdx = beginIdx + splitStr.length();
                            start = endIdx;
                            if (beginIdx > 0)
                                editable.setSpan(
                                        span,
                                        beginIdx,
                                        endIdx,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
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
                default:
                    break;
            }
            return true;
        }
    };
}