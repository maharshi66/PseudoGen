package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ExamplesActivity extends AppCompatActivity {
    List<String> listDataHeader;
    HashMap <String, List <String> > listDataChild;
    ExpandableListView listView;
    ExamplesExpandableListAdapter listAdapter;
    private AdView examplesAdViewBanner;
    private int lastExpandedPostion = -1;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_examples );
        listView = findViewById(R.id.examplesExpandableListView);
        prepareList ();
        listAdapter = new ExamplesExpandableListAdapter (getApplicationContext(), listDataHeader, listDataChild);
        listView.setAdapter ( listAdapter );
        listAdapter.notifyDataSetChanged ();

        //Collapses other list items except for the selected one
        listView.setOnGroupExpandListener ( new ExpandableListView.OnGroupExpandListener ( ) {
            @Override
            public void onGroupExpand ( int groupPosition ) {
                if(lastExpandedPostion != -1 && groupPosition != lastExpandedPostion)
                {
                    listView.collapseGroup ( lastExpandedPostion );
                }
                lastExpandedPostion = groupPosition;
            }
        } );

        examplesAdViewBanner = findViewById(R.id.examplesAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        examplesAdViewBanner.loadAd(adRequest);
    }

    public void loadExamplePosts()
    {
        //load posts from database. Preferably on device
    }

    public void prepareList()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<Posts> uploadedPosts = new ArrayList <Posts> (  );
        Posts p1 = new Posts (  );
        p1.setTitle ( "Reverse Linked List" );
        p1.setInput ( "Singly Linked List" );
        p1.setOutput ( "Singly Linked List" );
        p1.setDescription ( "Given a singly linked list of elements, reverse its contents and return the new list." );
        uploadedPosts.add ( p1 );
        int i = 0;

        for(Posts p :  uploadedPosts)
        {
            listDataHeader.add(p.getTitle());
            List<String> postChildrenList = new ArrayList<String>();
            postChildrenList.add("Description: " + p.getDescription());
            postChildrenList.add("Input: " + p.getInput());
            postChildrenList.add("Output: " + p.getOutput());
            listDataChild.put(listDataHeader.get(i), postChildrenList);
            i++;
        }
    }
}