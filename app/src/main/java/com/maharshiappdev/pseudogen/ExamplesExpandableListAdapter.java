package com.maharshiappdev.pseudogen;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExamplesExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List <String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap <String, List<String>> _listDataChild;

    public ExamplesExpandableListAdapter (Context context, List<String> listDataHeader,HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataChild = listChildData;
        this._listDataHeader = listDataHeader;
    }

    @Override
    public int getGroupCount () {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount ( int groupPosition ) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup ( int groupPosition ) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild ( int groupPosition , int childPosition ) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId ( int groupPosition ) {
        return groupPosition;    }

    @Override
    public long getChildId ( int groupPosition , int childPosition ) {
            return childPosition;
    }

    @Override
    public boolean hasStableIds () {
        return false;
    }

    @Override
    public View getGroupView ( int groupPosition , boolean isExpanded , View convertView , ViewGroup parent ) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.example_code_title, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.exampleTitle);
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
    public View getChildView ( int groupPosition , int childPosition , boolean isLastChild , View convertView , ViewGroup parent ) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.example_code_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.exampleCodeItem);

        txtListChild.setText(childText);
        txtListChild.setBackgroundResource ( R.drawable.item_child_corner );
        return convertView;    }

    @Override
    public boolean isChildSelectable ( int groupPosition , int childPosition ) {
        return false;
    }
}
