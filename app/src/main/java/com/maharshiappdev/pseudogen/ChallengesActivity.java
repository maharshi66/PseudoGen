package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ChallengesActivity extends AppCompatActivity {
    List< Pair <String, Boolean> > listDataHeader;
    HashMap < Pair < String, Boolean >, List < String > > listDataChild;
    Map <Integer, Boolean> solvedHashMap;
    ExpandableListView listView;
    ChallengesExpandableListAdapter listAdapter;
    private AdView examplesAdViewBanner;
    private int groupPos;
    private int lastExpandedPostion = -1;
    private String _tableName = "";
    DatabaseHandler db;

    @Override
    public void onCreateContextMenu ( ContextMenu menu , View v , ContextMenu.ContextMenuInfo menuInfo ) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.challenge_context_menu, menu );
    }

    public String skipStringPattern(String inputString, String pattern)
    {
        Scanner skipInput = new Scanner(inputString);
        skipInput.skip( Pattern.compile (pattern));
        String input = skipInput.nextLine ();
        skipInput.close ();
        return input;
    }

    @Override
    public boolean onContextItemSelected ( @NonNull MenuItem item ) {
        super.onContextItemSelected ( item );
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo ();
        groupPos = ExpandableListView.getPackedPositionType(info.packedPosition);
        switch (item.getItemId ())
        {
            case R.id.action_solveChallenge:
                try{
                    Pair <String, Boolean> groupPair = listDataHeader.get ( groupPos );
                    String title = groupPair.first;
                    List<String> childrenList = listDataChild.get(groupPair);
                    String description = skipStringPattern ( childrenList.get ( 0 ), "Description:" );
                    String input = skipStringPattern ( childrenList.get ( 1 ), "Input:" );
                    String output = skipStringPattern ( childrenList.get ( 2 ), "Output:" );
                    Intent editorIntent = new Intent(ChallengesActivity.this, CodeEditorTabbedActivity.class);
                    editorIntent.putExtra ( "challengeTitle", title );
                    editorIntent.putExtra ( "challengeDescription", description );
                    editorIntent.putExtra (  "challengeInput", input);
                    editorIntent.putExtra ( "challengeOutput", output );
                    startActivity ( editorIntent );
                }catch ( Exception e )
                {
                    e.printStackTrace ();
                }
                break;
            case R.id.action_markAsComplete:
                try{
                    Pair <String, Boolean> currPair = listDataHeader.get ( groupPos );
                    Pair <String, Boolean> pair = new Pair <> ( currPair.first, true );
                    listDataHeader.set ( groupPos, pair );
                    listAdapter.updateCheckAtPosition(groupPos, pair.first, pair.second);
                    db.markChallengeComplete ( pair.first );
                }catch ( Exception e )
                {
                    e.printStackTrace ();
                }
                break;
            case R.id.action_uncheckChallenge:
                try{
                    Pair <String, Boolean> currPair = listDataHeader.get ( groupPos );
                    Pair <String, Boolean> pair = new Pair <> ( currPair.first, false );
                    listDataHeader.set ( groupPos, pair );
                    listAdapter.updateCheckAtPosition(groupPos, pair.first, pair.second);
                    db.markChallengeIncomplete ( pair.first);
                }catch ( Exception e )
                {
                    e.printStackTrace ();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public List<Posts> loadChallengePosts ()
    {
        List<Posts> newListOfPosts = new ArrayList <> (  );
        Posts p1 = new Posts (  );
        p1.setTitle ( "Reverse Linked List" );
        p1.setInput ( "Singly Linked List" );
        p1.setOutput ( "Singly Linked List" );
        p1.setDescription ( "Given a singly linked list of elements, reverse its contents and return the new list." );
        newListOfPosts.add ( p1 );
        db.addChallenge ( p1);
        return newListOfPosts ;
    }

    public void prepareList()
    {
        List<Posts> uploadedPosts = loadChallengePosts ();
        int i = 0;

        for(Posts p :  uploadedPosts)
        {
            int intValue = db.getCheckedState ( p.getTitle ());
            Boolean state;
            if (intValue >= 1) {
                state = true;
            }
            else {
                state = false;
            }
            Pair<String, Boolean> pair = new Pair <> ( p.getTitle (), state);
            listDataHeader.add ( pair );
            List<String> postChildrenList = new ArrayList<String>();
            postChildrenList.add("Description: " + p.getDescription());
            postChildrenList.add("Input: " + p.getInput());
            postChildrenList.add("Output: " + p.getOutput());
            listDataChild.put(listDataHeader.get(i), postChildrenList);
            i++;
        }
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_challenges );
        db = new DatabaseHandler (getApplicationContext());

        solvedHashMap = new HashMap <Integer, Boolean> (); //Contains the Position-State pair for solved challenges
        listView = findViewById(R.id.examplesExpandableListView);
        listDataHeader = new ArrayList <> ();
        listDataChild = new HashMap < Pair < String, Boolean >, List < String > > ();
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

        registerForContextMenu ( listView );

        examplesAdViewBanner = findViewById(R.id.examplesAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        examplesAdViewBanner.loadAd(adRequest);
    }
}