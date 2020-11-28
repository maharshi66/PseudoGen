package com.maharshiappdev.pseudogen;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{
    View view;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExpandableListView expandableListView = view.findViewById(R.id.homeExpandableListView);
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);
        // setting list adapter
        expandableListView.setAdapter(listAdapter);

        //set to expand for each group in expandableListView
        for(int i = 0; i < listAdapter.getGroupCount(); i++ )
        {
            expandableListView.expandGroup(i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Intro to Pseudocode");
        listDataHeader.add("Data Structures");
        listDataHeader.add("Analysis of Algorithms");

        // Adding child data
        List<String> introToPseudocodeHeader = new ArrayList<String>();
        introToPseudocodeHeader.add("Motivation & Insight");
        introToPseudocodeHeader.add("Syntax Used");
        introToPseudocodeHeader.add("Examples");

        List<String> dataStructuresHeader = new ArrayList<String>();
        dataStructuresHeader.add("Arrays");
        dataStructuresHeader.add("Strings");
        dataStructuresHeader.add("Linked Lists");
        dataStructuresHeader.add("Stack");
        dataStructuresHeader.add("Queue");
        dataStructuresHeader.add("Set");
        dataStructuresHeader.add("Map");
        dataStructuresHeader.add("Hash Tables");
        dataStructuresHeader.add("Priority Queue");
        dataStructuresHeader.add("Heaps");
        dataStructuresHeader.add("Binary Tree");
        dataStructuresHeader.add("Binary Search Tree");
        dataStructuresHeader.add("Graphs");

        List<String> analysisOfAlgorithmsHeader = new ArrayList<String>();
        analysisOfAlgorithmsHeader.add("Time & Space Analysis");
        analysisOfAlgorithmsHeader.add("Designing Algorithms");
        analysisOfAlgorithmsHeader.add("Divide & Conquer");
        analysisOfAlgorithmsHeader.add("Greedy Algorithms");
        analysisOfAlgorithmsHeader.add("Dynamic Programming");
        analysisOfAlgorithmsHeader.add("Searching & Sorting");

        listDataChild.put(listDataHeader.get(0), introToPseudocodeHeader); // Header, Child data
        listDataChild.put(listDataHeader.get(1), dataStructuresHeader);
        listDataChild.put(listDataHeader.get(2), analysisOfAlgorithmsHeader);
    }
}