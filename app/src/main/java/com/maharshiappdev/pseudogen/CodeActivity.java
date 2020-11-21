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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity {
    ListView codesListView;
    ArrayList<String> codesList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    private final DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

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
            createAlertForAddNew();
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
        codesList.add("Hello Universe!");
        codesListView.setAdapter(arrayAdapter);
        getTitlesFromDatabase();
    }

    public void createAlertForAddNew()
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
                                writeToDatabase(titleEditText.getText().toString());
                                Intent intent = new Intent(CodeActivity.this, CodeEditorActivity.class);
                                intent.putExtra("pseudocodeTitle", titleEditText.getText().toString());
                                startActivity(intent);
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

    //TODO Change function parameters as development changes
    public void writeToDatabase(String title)
    {
        //Write Pseudocode Titles to Database
        firebaseDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts").child("Code List").child(title).setValue(title);
    }

    public void getTitlesFromDatabase()
    {
        firebaseDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren())
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}