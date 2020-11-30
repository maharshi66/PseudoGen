package com.maharshiappdev.pseudogen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListFragment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodeListFragment extends Fragment implements OnItemClickListener{
    View view;
    Menu deleteMenu;
    ExpandableListView codeListExpandableListView;
    CodeListExapandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.codeListExpandableListView)
        {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.code_list_menu, menu);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Old Way for adding Print all odd to ListView
        /*        ArrayList<String> codeList = new ArrayList<>();
        codeList.add("Print all odd integers from 1 to n");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, codeList);
        codeListView.setAdapter(arrayAdapter);

        codeListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    Intent intent = new Intent(getActivity(), CodeEditorTabbedActivity.class);
                    intent.putExtra("defaultPrintHundredOddClicked", true);
                    getActivity().startActivity(intent);
                }
            }
        });*/
        codeListExpandableListView = getView().findViewById(R.id.codeListExpandableListView);
        prepareDataListDefault();
        listAdapter = new CodeListExapandableListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);
        // setting list adapter
        codeListExpandableListView.setAdapter(listAdapter);
        registerForContextMenu(codeListExpandableListView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code_list, container, false);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void prepareDataListDefault()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding header data
        listDataHeader.add("Print All Odd");
        // Adding child data
        List<String> pseudocodePostList = new ArrayList<String>();
        pseudocodePostList.add("Description: Print all odd integers from 1 to n");
        pseudocodePostList.add("Input: Void");
        pseudocodePostList.add("Ouput: Array");
        pseudocodePostList.add("Time: O(N)");
        pseudocodePostList.add("Space: O(1)");
        listDataChild.put(listDataHeader.get(0), pseudocodePostList); // Header, Child data
    }

}