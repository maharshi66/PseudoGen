package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChallengesActivity extends AppCompatActivity {
    List< Pair <String, Boolean> > listDataHeader;
    HashMap < Pair < String, Boolean >, List < String > > listDataChild;
    ExpandableListView listView;
    ChallengesExpandableListAdapter listAdapter;
    private AdView examplesAdViewBanner;
    private int lastExpandedPostion = -1;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_challenges );
        listView = findViewById(R.id.examplesExpandableListView);
        prepareList ();
        listAdapter = new ChallengesExpandableListAdapter (getApplicationContext(), listDataHeader, listDataChild);
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
        listDataHeader = new ArrayList <> ();
        listDataChild = new HashMap < Pair < String, Boolean >, List < String > > ();
        List<Posts> uploadedPosts = new ArrayList <Posts> (  );
        String revll =
                "process\n" +
                "LinkedList has nodes with a value and a pointer to next element\n"+
                "\tInitialize prev pointer as null\n"+
                "\tInitialize curr pointer as head\n" +
                "\tInitialize prev pointer as null\n" +
                "\twhile curr != null\n" +
                "\t\tnext = curr.next\n" +
                "\t\tcurr.next = prev\n" +
                "\t\tcurr = next\n" +
                "\tendloop\n" +
                "endprocess";

        Posts p1 = new Posts (  );
        p1.setTitle ( "Reverse Linked List" );
        p1.setInput ( "Singly Linked List" );
        p1.setOutput ( "Singly Linked List" );
        p1.setDescription ( "Given a singly linked list of elements, reverse its contents and return the new list." );
        p1.setPseudocode ( revll  );
        uploadedPosts.add ( p1 );
        int i = 0;

        for(Posts p :  uploadedPosts)
        {
            Pair<String, Boolean> pair = new Pair <> ( p.getTitle (), false );
            listDataHeader.add ( pair );
            List<String> postChildrenList = new ArrayList<String>();
            postChildrenList.add("Description: " + p.getDescription());
            postChildrenList.add("Input: " + p.getInput());
            postChildrenList.add("Output: " + p.getOutput());
            listDataChild.put(listDataHeader.get(i), postChildrenList);
            i++;
        }
    }
}