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

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.app.ListFragment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO Delete this Fragment as no longer in use!
public class CodeListFragment extends Fragment implements OnItemClickListener{
    View view;
    Menu deleteMenu;
    ExpandableListView codeListExpandableListView;
    CodeListExapandableListAdapter listAdapter;
    List<Posts> postList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DatabaseHandler db;
    SearchView codeListSearchView;

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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.deleteItem:
                ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
                int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
                int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
                //Deletes from Database based on pseudocode title.

                //TODO Careful with this. Add checks to avoid duplicate naming!!!
                db.deletePost(listDataHeader.get(groupPos));
                listDataHeader.remove(groupPos);
                listDataChild.remove(childPos);
                listAdapter.notifyDataSetChanged();
                break;
            case R.id.editItem:
                ExpandableListView.ExpandableListContextMenuInfo postInfo = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
                int editPostPos = ExpandableListView.getPackedPositionGroup(postInfo.packedPosition);
                Posts editPost = db.getPost(listDataHeader.get(editPostPos));
                Intent intent = new Intent(getActivity().getApplicationContext(), CodeEditorTabbedActivity.class);
                intent.putExtra("fromEditPseudocodeTitle", editPost.getTitle());
                intent.putExtra("fromEditPseudocode", editPost.getPseudocode());
                intent.putExtra("fromEditPseudocodeDescription", editPost.getDescription());
                intent.putExtra("fromEditInput", editPost.getInput());
                intent.putExtra("fromEditOutput", editPost.getOutput());
                startActivity(intent);
                break;
        }
        return true;
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

    public void prepareDataList()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<Posts> uploadedPosts = loadPosts();

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

    private List<Posts> loadPosts(){
        postList = db.getAllPosts();
        return postList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DatabaseHandler(getActivity().getApplicationContext());
        codeListExpandableListView = getView().findViewById(R.id.codeListExpandableListView);
        prepareDataList();
        listAdapter = new CodeListExapandableListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);
        codeListExpandableListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        registerForContextMenu(codeListExpandableListView);
        codeListSearchView = getView().findViewById(R.id.codeListSearchView);
        codeListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.filterData(newText);
                return false;
            }
        });
    }
}