package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

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
    DatabaseHandler db;

    @Override
    public void onBackPressed () {
        Intent intent = new Intent ( ChallengesActivity.this, CentralActivity.class );
        intent.putExtra ( "enableInterstitial", true );
        startActivity ( intent );
    }

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
        groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);

        switch (item.getItemId ())
        {
            case R.id.action_solveChallenge:
                try{
                    Pair <String, Boolean> groupPair = listDataHeader.get ( groupPos);
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
                    Pair <String, Boolean> currPair = listDataHeader.get (groupPos) ;
                    Pair <String, Boolean> pair = new Pair <> ( currPair.first, true );
                    List<String> currChildren = listDataChild.get ( listDataHeader.get (groupPos ));
                    listDataHeader.set (groupPos, pair );
                    listDataChild.put ( pair, currChildren);
                    db.markChallengeComplete ( pair.first, getUsernameForTable ());
                    listAdapter.notifyDataSetChanged ();
                }catch ( Exception e )
                {
                    e.printStackTrace ();
                }
                break;
            case R.id.action_uncheckChallenge:
                try{
                    Pair <String, Boolean> currPair = listDataHeader.get ( groupPos);
                    Pair <String, Boolean> pair = new Pair <> ( currPair.first, false );
                    List<String> currChildren = listDataChild.get ( listDataHeader.get (groupPos ));
                    listDataHeader.set (groupPos, pair );
                    listDataChild.put ( pair, currChildren);
                    db.markChallengeIncomplete ( pair.first, getUsernameForTable ());
                    listAdapter.notifyDataSetChanged ();
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

        Posts reverseString = new Posts (  );
        reverseString.setTitle ( "Reverse String" );
        reverseString.setInput ( "String" );
        reverseString.setOutput ( "String" );
        reverseString.setDescription ( "Given a string, reverse its contents and return it" );
        newListOfPosts.add ( reverseString );
        db.addChallenge ( reverseString, getUsernameForTable ());

        Posts checkPalindrome = new Posts (  );
        checkPalindrome.setTitle ( "Check Valid Palindrome" );
        checkPalindrome.setInput ( "String" );
        checkPalindrome.setOutput ( "String" );
        checkPalindrome.setDescription ( "Given a string, check if it is a valid palindrome. A palindrome is a word, phrase, or sequence that reads the same backwards as forwards" );
        newListOfPosts.add ( checkPalindrome );
        db.addChallenge ( checkPalindrome, getUsernameForTable ());

        Posts checkAnagram = new Posts (  );
        checkAnagram.setTitle ( "Check Valid Anagrams" );
        checkAnagram.setInput ( "Array of Strings" );
        checkAnagram.setOutput ( "Array of String" );
        checkAnagram.setDescription ( "Given an array of two strings, check if it is a valid anagram.An anagram is a word, phrase, or name formed by rearranging the letters of another" );
        newListOfPosts.add ( checkPalindrome );
        db.addChallenge ( checkAnagram, getUsernameForTable ());

        Posts arrayIntersection = new Posts (  );
        arrayIntersection.setTitle ( "Intersection Of Two Arrays" );
        arrayIntersection.setInput ( "Array of Integers" );
        arrayIntersection.setOutput ( "Array of Integers" );
        arrayIntersection.setDescription ( "Given two arrays of integers, compute and return their intersection" );
        newListOfPosts.add ( arrayIntersection );
        db.addChallenge ( arrayIntersection, getUsernameForTable ());

        Posts mergeSortedArray = new Posts (  );
        mergeSortedArray.setTitle ( "Merge Sorted Arrays" );
        mergeSortedArray.setInput ( "Array of Integers" );
        mergeSortedArray.setOutput ( "Array of Integers" );
        mergeSortedArray.setDescription ( "Given two arrays of integers, compute and return their intersection" );
        newListOfPosts.add ( mergeSortedArray );
        db.addChallenge ( mergeSortedArray, getUsernameForTable ());

        Posts containsDuplicates = new Posts (  );
        containsDuplicates.setTitle ( "Contains Duplicates" );
        containsDuplicates.setInput ( "Array of Integers" );
        containsDuplicates.setOutput ( "Boolean" );
        containsDuplicates.setDescription ( "Given an array of integers, check if it contains duplicates values" );
        newListOfPosts.add ( containsDuplicates );
        db.addChallenge ( containsDuplicates, getUsernameForTable ());

        Posts twoSum = new Posts (  );
        twoSum.setTitle ( "Two Sum" );
        twoSum.setInput ( "Array of Integers" );
        twoSum.setOutput ( "Array of Integers" );
        twoSum.setDescription ( "Given an array of integers, return the positions of two numbers such that they add up to target value T" );
        newListOfPosts.add ( twoSum);
        db.addChallenge ( twoSum, getUsernameForTable ());

        Posts buySellStock = new Posts (  );
        buySellStock.setTitle ( "Best Time to Buy and Sell Stock" );
        buySellStock.setInput ( "Array of Integers" );
        buySellStock.setOutput ( "Integer" );
        buySellStock.setDescription ( "Given an array of integers representing prices of stock on a given day, return the maximum profit that can be earned from one transaction of buying and selling stocks. You cannot sell a stock before buying one." );
        newListOfPosts.add ( buySellStock);
        db.addChallenge ( buySellStock, getUsernameForTable ());

        Posts deleteNodeLinkedList = new Posts (  );
        deleteNodeLinkedList.setTitle ( "Delete Linked List Node" );
        deleteNodeLinkedList.setInput ( "Linked List Node" );
        deleteNodeLinkedList.setOutput ( "Singly Linked List" );
        deleteNodeLinkedList.setDescription ( "Delete a node from a singly linked list and return it.The input parameter is the node to be deleted and not the head of the linked list" );
        newListOfPosts.add ( deleteNodeLinkedList );
        db.addChallenge ( deleteNodeLinkedList, getUsernameForTable ());

        Posts reverseLinkedList = new Posts (  );
        reverseLinkedList.setTitle ( "Reverse Linked List" );
        reverseLinkedList.setInput ( "Singly Linked List" );
        reverseLinkedList.setOutput ( "Singly Linked List" );
        reverseLinkedList.setDescription ( "Given a singly linked list of elements, reverse its contents and return the new list." );
        newListOfPosts.add ( reverseLinkedList );
        db.addChallenge ( reverseLinkedList, getUsernameForTable ());

        Posts bubbleSort = new Posts (  );
        bubbleSort.setTitle ( "Bubble Sort" );
        bubbleSort.setInput ( "Array Of Integers" );
        bubbleSort.setOutput ( "Array of Integers" );
        bubbleSort.setDescription ( "Given an integer array, sort the numbers using the Bubble Sort algorithm" );
        newListOfPosts.add ( bubbleSort );
        db.addChallenge ( bubbleSort, getUsernameForTable ());

        Posts insertionSort = new Posts (  );
        insertionSort.setTitle ( "Insertion Sort" );
        insertionSort.setInput ( "Array Of Integers" );
        insertionSort.setOutput ( "Array of Integers" );
        insertionSort.setDescription ( "Given an integer array, sort the numbers using the Insertion Sort algorithm" );
        newListOfPosts.add ( insertionSort );
        db.addChallenge ( insertionSort, getUsernameForTable ());

        Posts selectionSort = new Posts (  );
        selectionSort.setTitle ( "Selection Sort" );
        selectionSort.setInput ( "Array Of Integers" );
        selectionSort.setOutput ( "Array of Integers" );
        selectionSort.setDescription ( "Given an integer array, sort the numbers using the Selection Sort algorithm" );
        newListOfPosts.add ( selectionSort );
        db.addChallenge ( selectionSort, getUsernameForTable ());

        Posts mergeSort = new Posts (  );
        mergeSort.setTitle ( "Merge Sort" );
        mergeSort.setInput ( "Array Of Integers" );
        mergeSort.setOutput ( "Array of Integers" );
        mergeSort.setDescription ( "Given an integer array, sort the numbers using the Merge Sort algorithm" );
        newListOfPosts.add ( mergeSort );
        db.addChallenge ( mergeSort, getUsernameForTable ());

        Posts quickSort = new Posts (  );
        quickSort.setTitle ( "Quick Sort" );
        quickSort.setInput ( "Array Of Integers" );
        quickSort.setOutput ( "Array of Integers" );
        quickSort.setDescription ( "Given an integer array, sort the numbers using the Quick Sort algorithm" );
        newListOfPosts.add ( quickSort );
        db.addChallenge ( quickSort, getUsernameForTable ());

        Posts binarySearch = new Posts (  );
        binarySearch.setTitle ( "Binary Search" );
        binarySearch.setInput ( "Array Of Integers" );
        binarySearch.setOutput ( "Integer" );
        binarySearch.setDescription ( "Given a sorted integer array, find the position of target X using Binary Search" );
        newListOfPosts.add ( binarySearch);
        db.addChallenge ( binarySearch, getUsernameForTable ());

        Posts preOrderTraversal = new Posts (  );
        preOrderTraversal.setTitle ( "Preorder Traversal" );
        preOrderTraversal.setInput ( "Binary Tree" );
        preOrderTraversal.setOutput ( "Array" );
        preOrderTraversal.setDescription ( "Given a Binary Tree, compute and add elements to an Array using Preorder Traversal" );
        newListOfPosts.add ( preOrderTraversal);
        db.addChallenge ( preOrderTraversal, getUsernameForTable ());

        Posts inOrderTraversal = new Posts (  );
        inOrderTraversal.setTitle ( "Inorder Traversal" );
        inOrderTraversal.setInput ( "Binary Tree" );
        inOrderTraversal.setOutput ( "Array" );
        inOrderTraversal.setDescription ( "Given a Binary Tree, compute and add elements to an Array using Inorder Traversal" );
        newListOfPosts.add ( inOrderTraversal);
        db.addChallenge ( inOrderTraversal, getUsernameForTable ());

        Posts postOrderTraversal = new Posts (  );
        postOrderTraversal.setTitle ( "Postorder Traversal" );
        postOrderTraversal.setInput ( "Binary Tree" );
        postOrderTraversal.setOutput ( "Array" );
        postOrderTraversal.setDescription ( "Given a Binary Tree, compute and add elements to an Array using Postorder Traversal" );
        newListOfPosts.add ( postOrderTraversal);
        db.addChallenge ( postOrderTraversal, getUsernameForTable ());

        Posts pathExistsDFS = new Posts (  );
        pathExistsDFS.setTitle ( "Path Exists - Depth First Search" );
        pathExistsDFS.setInput ( "Graph" );
        pathExistsDFS.setOutput ( "Boolean" );
        pathExistsDFS.setDescription ( "Given a graph, check if there is a path from Node A to Node B using Depth First Search" );
        newListOfPosts.add ( pathExistsDFS );
        db.addChallenge ( pathExistsDFS, getUsernameForTable ());

        Posts shortestPathBFS = new Posts (  );
        shortestPathBFS.setTitle ( "Shortest Path - Breadth First Search" );
        shortestPathBFS.setInput ( "Graph" );
        shortestPathBFS.setOutput ( "Integer" );
        shortestPathBFS.setDescription ( "Given a graph, find the length of the shortest path from Node A to Node B using Breadth First Search" );
        newListOfPosts.add ( shortestPathBFS );
        db.addChallenge ( shortestPathBFS, getUsernameForTable ());

        Posts dummyNode = new Posts (  );
        dummyNode.setTitle ( "Dummy" );
        dummyNode.setInput ( "Dummy" );
        dummyNode.setOutput ( "Dummy" );
        dummyNode.setDescription ( "Dummy" );
        newListOfPosts.add ( dummyNode );
        db.addChallenge ( dummyNode, getUsernameForTable ());
        return newListOfPosts ;
    }

    public void prepareList()
    {
        List<Posts> uploadedPosts = loadChallengePosts ();
        int i = 0;

        for(Posts p :  uploadedPosts)
        {
            int intValue = db.getCheckedState ( p.getTitle (), getUsernameForTable ());
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
            i+=1;
        }
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_challenges );
        TextView examplesHeaderTextView = findViewById ( R.id.examplesHeaderTextView );
        Drawable infoIcon = getResources().getDrawable(R.drawable.info_icon);
        examplesHeaderTextView.setCompoundDrawablesWithIntrinsicBounds(infoIcon, null, null, null);

        db = new DatabaseHandler (getApplicationContext());
        db.createChallengeTable ( getUsernameForTable () );

        solvedHashMap = new HashMap <Integer, Boolean> (); //Contains the Position-State pair for solved challenges
        listView = findViewById(R.id.examplesExpandableListView);
        listDataHeader = new ArrayList <> ();
        listDataChild = new HashMap < Pair < String, Boolean >, List < String > > ();
        prepareList ();
        listAdapter = new ChallengesExpandableListAdapter (getApplicationContext(), listDataHeader, listDataChild);
        listView.setAdapter ( listAdapter );

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

    private String getUsernameForTable () {
        String personName = "guestchallenges";
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ChallengesActivity.this);
        if (acct != null) {
            personName = acct.getEmail ();
        }else
        {
            if( FirebaseAuth.getInstance ().getCurrentUser () != null)
            {
                personName =  FirebaseAuth.getInstance ().getCurrentUser ().getEmail ();
            }
        }
        return personName;
    }
}