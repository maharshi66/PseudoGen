package com.maharshiappdev.pseudogen;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CodeListExapandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataHeaderOriginal; // header titles
    private List<String> _listDataHeaderTags;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public CodeListExapandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataChild = listChildData;
        this._listDataHeader = listDataHeader;
        this._listDataHeaderOriginal = new ArrayList<>();
        this._listDataHeaderOriginal.addAll(listDataHeader);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.code_list_title, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.codeListTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

       if(isExpanded)
        {
            convertView.setBackgroundResource (R.drawable.item_rounded_corner_selected );
        }else {
            convertView.setBackgroundResource(R.drawable.item_rounded_corner);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.code_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.codeListItem);

        txtListChild.setText(childText);
        txtListChild.setBackgroundResource ( R.drawable.item_child_corner );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    //Required as this updates the list after an item delete!
    public void updateListsAfterDelete(List newList)
    {
        this._listDataHeaderOriginal.clear();
        this._listDataHeaderOriginal.addAll(newList);
    }

    //Need this to search inside the expandableListView
    public void filterData(String query){
        query = query.toLowerCase();
        this._listDataHeader.clear();
        if(query.isEmpty())
        {
            this._listDataHeader.addAll(this._listDataHeaderOriginal);
        }else
        {
            for(String str : this._listDataHeaderOriginal)
            {
                if(str.toLowerCase().contains(query))
                {
                    this._listDataHeader.add(str);
                }
            }
        }
        notifyDataSetChanged();
    }
}