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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodeEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodeEditorFragment extends Fragment {
    private final DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String defaultPrintHundredOddCode="";

    public CodeEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodeEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CodeEditorFragment newInstance(String param1, String param2) {
        CodeEditorFragment fragment = new CodeEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        //TODO:Send editText content to activity.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_code_editor, container, false);
        return root;
    }

    //Fixed! How to send shared preferences data for PrintAllOdd to CodeEditText
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LineNumberedEditText inputCodeEditText = getActivity().findViewById(R.id.inputCodeEditText);
        updateEditTextViewForPrintOddIntegers(inputCodeEditText);

        inputCodeEditText.addTextChangedListener(new TextWatcher() {
            Map<String,Integer> map = new HashMap<>();
            ForegroundColorSpan span;
            public void setUpMap()
            {
                map.put("startprocess",Color.RED);
                map.put("endprocess",Color.RED);
                map.put("char",Color.GREEN);
                map.put("string",Color.GREEN);
                map.put("integer",Color.GREEN);
                map.put("for",Color.BLUE);
                map.put("while",Color.BLUE);
                map.put("do",Color.BLUE);
                map.put("endloop",Color.BLUE);
                map.put("if",Color.BLUE);
                map.put("else",Color.BLUE);
                map.put("endif",Color.BLUE);
                map.put("endif",Color.BLUE);
            }

            public void clearMap()
            {
                map.clear();
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
                String[] splitStrArray = editString.split("\\s+");
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
        //registerForContextMenu(getActivity().findViewById(R.id.codesListview));
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.codelist_item_context_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
/*        switch (item.getItemId()) {
            case R.id.a_item:
                Log.i("ContextMenu", "Item 1a was chosen");
                return true;
            case R.id.b_item:
                Log.i("ContextMenu", "Item 1b was chosen");
                return true;
        }*/
        return super.onContextItemSelected(item);
    }

    public void updateEditTextViewForPrintOddIntegers(LineNumberedEditText inputCodeEditText)
    {
        getFromSharedPref();
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        boolean isPrintAllOddsSelected = intent.getBooleanExtra("defaultPrintHundredOddClicked", false);

        if(isPrintAllOddsSelected)
        {
            inputCodeEditText.setText(defaultPrintHundredOddCode);
        }
    }

    //Get code from shared preferences for PrintAllOdd!
    public void getFromSharedPref()
    {
        SharedPreferences appSharedPref = getActivity().getSharedPreferences("com.maharshiappdev.pseudogen", Context.MODE_PRIVATE);
        defaultPrintHundredOddCode = appSharedPref.getString("inputCode", "");
    }
}