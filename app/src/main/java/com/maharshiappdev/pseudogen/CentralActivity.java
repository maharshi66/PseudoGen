package com.maharshiappdev.pseudogen;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

public class CentralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout navDrawer;
    private int groupPos;
    private int childPos;
    private static final int PERMISSION_REQ_CODE = 100;
    private AdView centralAdViewBanner;
    FloatingActionButton fab_addNew;
    AutoCompleteTextView codeListSearchEditText;
    ArrayAdapter<String> autoCompleteArrayAdapter;
    List<String> listDataHeaders;
    Toolbar centralToolbar;
    BottomNavigationView navigation;
    Boolean bottomNavState = false; //false = hidden, true = showing
    BottomNavigationView codeItemNavigation;
    ExpandableListView codeListExpandableListView;
    CodeListExapandableListAdapter listAdapter;
    List<Posts> postList;
    List<String> listDataHeader;
    List<String> listDataHeaderTags;
    HashMap<String, List<String>> listDataChild;
    DatabaseHandler db;
    SearchView codeListSearchView;

    public void saveToInternalStorage(String title, String description, String input, String output, String pseudocode) throws IOException {
        FileOutputStream fOut = openFileOutput("title", Context.MODE_PRIVATE);
        String fileText = "Algorithm: " + title + "\n"
                + "Description: " + description + "\n"
                + "Input: " + input + "\n"
                + "Output: " + output + "\n"
                + pseudocode;
        fOut.write(fileText.getBytes());
        fOut.close();
        Toast.makeText(this, "saved as txt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_logOut:
                alertUserForSignOut();
                break;
            case R.id.nav_developer:
                //TODO Add alert giving background on the developer and why this app was made (succinctly)
                break;
            case R.id.nav_rateApp:
                //Opens  Playstore for rating the app
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                startActivity(intent);

                //TODO uncomment once app is on Playstore
/*                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
                break;
        }
        return true;
    }
    public void createAlertForDeleteItem()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(CentralActivity.this);
        alert.setMessage("Delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deletePost(listDataHeader.get(groupPos));
                        listDataHeader.remove(groupPos);
                        listDataChild.remove(childPos);
                        listAdapter.notifyDataSetChanged();
                        listAdapter.updateListsAfterDelete(listDataHeader);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public File writeFileOnInternalStorage(String fileName, String fileText){
        File dir = new File(this.getFilesDir(), "text");
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            File fileOut = new File(dir, fileName);
            FileWriter writer = new FileWriter (fileOut);
            writer.append(fileText);
            writer.flush();
            writer.close();
            return fileOut;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_editItem:
                    Posts editPost = db.getPost(listDataHeader.get(groupPos));
                    Intent intent = new Intent(CentralActivity.this, CodeEditorTabbedActivity.class);
                    intent.putExtra("fromEditPseudocodeTitle", editPost.getTitle());
                    intent.putExtra("fromEditPseudocode", editPost.getPseudocode());
                    intent.putExtra("fromEditPseudocodeDescription", editPost.getDescription());
                    intent.putExtra("fromEditInput", editPost.getInput());
                    intent.putExtra("fromEditOutput", editPost.getOutput());
                    startActivity(intent);
                    hideBottomNav();
                    break;
                case R.id.action_shareItem:
                    String title = "",  description = "", input = "", output = "", pseudocode = "";
                    Posts sharePost = db.getPost(listDataHeader.get(groupPos));
                    title = sharePost.getTitle();
                    description = sharePost.getDescription();
                    input = sharePost.getInput();
                    output = sharePost.getOutput();
                    pseudocode = sharePost.getPseudocode();
                    String fileText = "";
                    String fileName = title + ".txt";
                    fileText = "Algorithm: " + title + "\n"
                            + "Description: " + description + "\n"
                            + "Input: " + input + "\n"
                            + "Output: " + output + "\n\n"
                            + pseudocode;
                    File shareFile = writeFileOnInternalStorage ( fileName , fileText );
                    try {
                        Uri fileUri = FileProvider.getUriForFile (
                                CentralActivity.this ,
                                "com.maharshiappdev.pseudogen.fileprovider" ,
                                shareFile );
                        //Open intent with file from external storage
                        Intent sharingIntent = new Intent ( Intent.ACTION_SEND );
//                              sharingIntent.setType("message/rfc822"); //for attachments
                        sharingIntent.setType ( "*/*" );
                        sharingIntent.putExtra ( Intent.EXTRA_SUBJECT , title + " - pseudocode from PseudoGen" );
//                                sharingIntent.putExtra(Intent.EXTRA_TEXT, fileText);
                        sharingIntent.putExtra ( Intent.EXTRA_STREAM , fileUri );
                        startActivity ( Intent.createChooser ( sharingIntent , "Share pseudoode with:" ) );
                    }
                    catch ( IllegalArgumentException e ) {
                        Log.e ( "File Selector" ,
                                "The selected file can't be shared: " + shareFile.toString ( ) );
                    }
                    hideBottomNav();
                    break;
                case R.id.action_deleteItem:
                    createAlertForDeleteItem();
                    hideBottomNav();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private List<Posts> loadPosts(){
        postList = db.getAllPosts();
        return postList;
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
    public void createAlertForAddNew()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CutomAlertDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.add_new_code_dialog, null);
        final EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        final Spinner codeInputSpinner = dialogView.findViewById(R.id.codeInputSpinner);
        final Spinner codeOutputSpinner = dialogView.findViewById(R.id.codeOutputSpinner);
        String[] dataStructuresInput = getResources().getStringArray(R.array.data_structures_input);
        String[] dataStructuresOutput = getResources().getStringArray(R.array.data_structures_output);

        ArrayAdapter<String> codeInputArrayAdapter = new ArrayAdapter<String>(CentralActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructuresInput);
        ArrayAdapter<String> codeOutputArrayAdapter = new ArrayAdapter<String>(CentralActivity.this, android.R.layout.simple_spinner_dropdown_item, dataStructuresOutput);

        codeInputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeOutputArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        codeInputSpinner.setAdapter(codeInputArrayAdapter);
        codeOutputSpinner.setAdapter(codeOutputArrayAdapter);

        alertDialog.setView(dialogView)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String codeTitleText = "";
                        String codeDescriptionText = "";
                        String codeInputText = "";
                        String codeOutputText = "";
                        codeTitleText = titleEditText.getText().toString();
                        codeDescriptionText = descriptionEditText.getText().toString();
                        codeInputText = codeInputSpinner.getSelectedItem().toString();
                        codeOutputText = codeOutputSpinner.getSelectedItem().toString();

                        if(!codeTitleText.isEmpty() && !codeDescriptionText.isEmpty() &&
                            !codeInputText.isEmpty() && !codeOutputText.isEmpty())
                        {
                            Intent intent = new Intent(CentralActivity.this, CodeEditorTabbedActivity.class);
                            intent.putExtra("pseudocodeTitle", codeTitleText);
                            intent.putExtra("pseudocodeDescription", codeDescriptionText);
                            intent.putExtra("pseudocodeInputText", codeInputText);
                            intent.putExtra("pseudocodeOutputText", codeOutputText);
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(CentralActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
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
/*        AlertDialog dialog = alertDialog.create();
        dialog.show();

        //TODO Helps Increase Size of the Dialog but doesnt extend the View with it. FIX!
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth);
        int dialogWindowHeight = (int) (displayHeight * 0.80f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);*/
    }

    public void alertUserForSignOut()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CentralActivity.this);
        alertDialog.setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void signOut()
    {
        //Sign out and go to login
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(
                getApplicationContext(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut();
        finishAffinity();
        Intent intent = new Intent(CentralActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else if(bottomNavState) {
            hideBottomNav();
        } else
        {
            finishAffinity();
        }
    }

    public void hideBottomNav()
    {
        bottomNavState = false;
        navigation.setVisibility(View.INVISIBLE);
    }

    public void showBottomNav()
    {
        bottomNavState = true;
        navigation.setVisibility(View.VISIBLE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(CentralActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(CentralActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(CentralActivity.this, "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(CentralActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }

    public void deleteAllPosts()
    {
        db.deleteAllPosts();
        listDataHeader.clear();
        listDataChild.clear();
        listAdapter.notifyDataSetChanged();
        listAdapter.updateListsAfterDelete(listDataHeader);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        MobileAds.initialize(this);
        listDataHeaders = new ArrayList<>();
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        hideBottomNav();

        fab_addNew = findViewById(R.id.fab_addNew);
        fab_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomNav();
                createAlertForAddNew();
            }
        });

        //Options menu for toolbar
        centralToolbar = findViewById(R.id.centralToolBar);
        centralToolbar.inflateMenu(R.menu.code_list_top_menu);

        centralToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.action_deleteAll:
                        AlertDialog.Builder alert = new AlertDialog.Builder(CentralActivity.this);
                        alert.setMessage("Do you want to delete all items?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllPosts();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                        break;
                }
                return false;
            }
        });

        navDrawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        View headerView = navigationView.getHeaderView ( 0 );
        TextView displayNameTextView = headerView.findViewById ( R.id.displayNameTextView );
        displayNameTextView.setText("Hello, "+ getDisplayNameInNavbar());
        TextView versionCodeTextView = headerView.findViewById ( R.id.versionCodeTextView );
        String version ="";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
             version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCodeTextView.setText("version: " + version);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navDrawer, centralToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        db = new DatabaseHandler(getApplicationContext());
        codeListExpandableListView = findViewById(R.id.codeListExpandableListView);
        prepareDataList();
        listAdapter = new CodeListExapandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
        codeListExpandableListView.setAdapter(listAdapter);
        codeListSearchView = findViewById(R.id.codeListSearchView);
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
        listAdapter.notifyDataSetChanged();

        codeListExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long pos = codeListExpandableListView.getExpandableListPosition(position);
                groupPos = ExpandableListView.getPackedPositionGroup(pos);
                childPos = ExpandableListView.getPackedPositionChild(pos);
                showBottomNav();
                return true;
            }
        });

        centralAdViewBanner = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        centralAdViewBanner.loadAd(adRequest);
    }

    private String getDisplayNameInNavbar() {
        String personName = "";
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CentralActivity.this);
        if (acct != null) {
             personName = acct.getGivenName();
        }
        return personName;
    }
}