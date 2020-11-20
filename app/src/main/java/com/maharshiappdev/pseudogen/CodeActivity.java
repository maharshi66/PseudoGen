package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity {
    ListView codesListView;
    ArrayList<String> codesList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.code_editor_add_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_addNew)
        {
            createAlertForNewCode();
            arrayAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_list);
        setTitle("Your List");
        codesListView = findViewById(R.id.codesListview);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, codesList);
        codesList.add("Hello World");

        codesListView.setAdapter(arrayAdapter);
    }

    public void createAlertForNewCode()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CodeActivity.this);
        LayoutInflater layoutInflater = CodeActivity.this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.add_new_code_dialog, null);
        final EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        alertDialog.setTitle("Add New")
                    .setView(dialogView)
                    .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO Take the title, input and output for cloud storage and updating codesList
                            if(!titleEditText.getText().toString().isEmpty())
                            {
                                codesList.add(titleEditText.getText().toString());
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
    }
}