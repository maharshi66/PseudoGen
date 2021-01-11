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
import androidx.core.text.HtmlCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout navDrawer;
    SharedPreferences mPrefs;
    final String welcomeDialogShownPref = "welcomeDialogShown";
    private Boolean welcomeDialogShown;
    private int groupPos;
    private int childPos;
    private int lastExpandedPostion = -1;
    private static final int PERMISSION_REQ_CODE = 100;
    private AdView centralAdViewBanner;
    private InterstitialAd centralInterstitialAd;
    private Boolean enableInterstitialAd = false;
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

    public void goToLoginActivity()
    {
        finishAffinity ();
        Intent intent = new Intent ( CentralActivity.this, LoginActivity.class );
        startActivity ( intent );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_signIn:
                if(FirebaseAuth.getInstance ().getCurrentUser () == null)
                {
                    goToLoginActivity();
                }else
                {
                    Toast.makeText ( this, "Already Signed In!", Toast.LENGTH_SHORT ).show ();
                }
                break;
            case R.id.nav_privacyPolicy:
                privacyPolicyClicked();
                break;
            case R.id.nav_termsConditions:
                termsOfUseClicked ( );
                break;
            case R.id.nav_usingThisApp:
                Intent newIntent = new Intent(CentralActivity.this, UsingPseudoGenActivity.class);
                startActivity ( newIntent );
                break;
            case R.id.nav_editorSettings:
                Intent editorSettingsIntent = new Intent ( CentralActivity.this, EditorSettingsActivity.class );
                startActivity ( editorSettingsIntent );
                break;
            case R.id.nav_generalStructure:
                createAlertForGeneralStructure();
                
                break;
            case R.id.nav_challenges:
                Intent challengesIntent = new Intent ( CentralActivity.this, ChallengesActivity.class );
                challengesIntent.putExtra ( "tableName", getDisplayNameInNavbar ().toLowerCase () );
                startActivity ( challengesIntent );
                break;
            case R.id.nav_developer:
                //TODO Add alert giving background on the developer and why this app was made (succinctly)
                createAlertForDeveloper();
                break;
            case R.id.nav_rateApp:
                //Opens  Playstore for rating the app
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                startActivity(intent);
/*                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
                break;
            case R.id.nav_signOut:
                alertUserForSignOut();
                break;
            default:
                break;
        }
        return true;
    }

    private void createAlertForGeneralStructure () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.general_structure_dialog, null);
        final TextView exampleOneEditText = dialogView.findViewById ( R.id.exampleOneEditText );
        final TextView exampleTwoEditText = dialogView.findViewById ( R.id.exampleTwoEditText );

        String[] keys = {"process", "endprocess", "if","else", "endif", "for", "endloop", "then", "function", "endfunction", "integer", "call"};
        String checkEven = "process\n"
                + "\tfor i in 0 to N"
                + "\n\t\tif i % 2 == 0 then"
                + "\n\t\t\treturn true"
                + "\n\t\tendif"
                + "\n\tendloop" +
                "\nendprocess";
        exampleOneEditText.setText(checkEven);
        for(String str : keys){
            setHighlightTextColor ( exampleOneEditText, str );
        }

        String findFactorial = "function computeFactorial(integer)" +
                "\n\tif N == 1 then" +
                "\n\t\treturn" +
                "\n\telse" +
                "\n\t\treturn N * computeFactorial(N - 1)" +
                "\n\tendif" +
                "\nendfunction" +
                "\n\nprocess" +
                "\n\tcall computeFactorial(100)" +
                "\nendprocess";
        exampleTwoEditText.setText ( findFactorial );
        for(String str : keys){
            setHighlightTextColor ( exampleTwoEditText, str );
        }

        alertDialog.setView ( dialogView );
        alertDialog.setIcon ( R.drawable.pseudogenlogo_layer_list );
        alertDialog.setTitle ( "General Structure" );
        alertDialog.show ();
    }

    private void createAlertForDeveloper () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CutomAlertDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.developer_dialog, null);
        final TextView developerInFoTextView = dialogView.findViewById ( R.id.developerInfoTextView );
        final TextView developerEmailTextView = dialogView.findViewById ( R.id.developerEmailTextView );
        developerInFoTextView.setText ("This app has been developed by Maharshi Shah. " +
                "This app is intended to be used by programming enthusiasts and developers as " +
                "a tool for planning the flow and structure of code. " +
                "\nPlease feel free to provide feedback and suggestions!" );

        String emailString = "maharshishah06@gmail.com";
        developerEmailTextView.setText ("Contact via email:" + emailString);
        setUnderlinedTextColor (developerEmailTextView, emailString);

        developerEmailTextView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                openIntentForEmail();
            }
        } );

        alertDialog.setIcon ( R.drawable.pseudogenlogo_layer_list );
        alertDialog.setTitle ( "Developer" );
        alertDialog.setView ( dialogView );
        alertDialog.show ( );

    }

    private void openIntentForEmail () {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"maharshishah06@gmail.com"});
        try {
            startActivity(Intent.createChooser(i, "Email Developer"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CentralActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void createAlertForDeleteItem()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(CentralActivity.this, R.style.CutomAlertDialog);
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
        alertDialog.setIcon ( R.drawable.add_new_layer_list );
        alertDialog.setView(dialogView)
                .setTitle ( "Add Details" )
                .setMessage ( "*All fields are required" )
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

        codeInputSpinner.setOnTouchListener ( new View.OnTouchListener ( ) {
            @Override
            public boolean onTouch ( View v , MotionEvent event ) {
                hideKeyboard (v);
                return false;
            }
        } );

        codeOutputSpinner.setOnTouchListener ( new View.OnTouchListener ( ) {
            @Override
            public boolean onTouch ( View v , MotionEvent event ) {
                hideKeyboard (v);
                return false;
            }
        } );
    }

    public void alertUserForSignOut()
    {
        if(FirebaseAuth.getInstance ().getCurrentUser () != null) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder ( CentralActivity.this , R.style.CutomAlertDialog );
            alertDialog.setTitle ( "Sign Out" )
                    .setMessage ( "Are you sure you want to sign out?" )
                    .setPositiveButton ( "Yes" , new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick ( DialogInterface dialog , int which ) {
                            signOut ( );
                        }
                    } )
                    .setNegativeButton ( "No" , new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick ( DialogInterface dialog , int which ) {
                            dialog.cancel ( );
                        }
                    } )
                    .show ( );
        }else
        {
            Toast.makeText ( this, "Not Signed In!", Toast.LENGTH_LONG ).show ();
        }

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
            finish ();
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

    private String getDisplayNameInNavbar() {
        String personName = "Guest";
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CentralActivity.this);
        if (acct != null) {
            personName = acct.getGivenName();
        }else
        {
            if(FirebaseAuth.getInstance ().getCurrentUser () != null)
            {
                String temp = FirebaseAuth.getInstance ().getCurrentUser ().getEmail ();
                String[] splitStr = temp.split ( "@" );
                personName = splitStr[0];
            }
        }
        return personName;
    }

    private String getUsernameForTable() {
        String tableName = "guestposts";
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CentralActivity.this);
        if (acct != null) {
            tableName = acct.getEmail ();
        }else
        {
            if(FirebaseAuth.getInstance ().getCurrentUser () != null)
            {
                tableName = FirebaseAuth.getInstance ().getCurrentUser ().getEmail ();
            }
        }
        return tableName;
    }

    public void hideKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void createDialogForWelcome () {

        AlertDialog.Builder welcomeAlert = new AlertDialog.Builder ( this, R.style.CutomAlertDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.welcome_to_app_dialog, null);
        welcomeAlert.setTitle ( "Welcome to PseudoGen!" );
        welcomeAlert.setView ( dialogView );
        final TextView welcomeMessageTextView = dialogView.findViewById ( R.id.welcomeMessageTextView );
        welcomeMessageTextView.setText ( "Thank you for downloading PseudoGen! We hope you find this app useful and use it to edit, store, and share pseudocode. Please rate us on Google Playstore so that we can improve this app.\nHappy pseudocoding!" );
        welcomeAlert.show ();
    }

    public void privacyPolicyClicked (  ) {
        AlertDialog.Builder alert = new AlertDialog.Builder ( this, R.style.CutomAlertDialog  );
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.privacy_policy_dialog, null);
        alert.setView ( dialogView );
        alert.setTitle ( "Privacy Policy" );
        alert.setIcon ( R.drawable.pseudogenlogo_layer_list );
        final TextView policyDescription = dialogView.findViewById ( R.id.privacyPolicyDescriptionTextView );
        String htmlText = "<!DOCTYPE html>\n" +
                "    <html>\n" +
                "    <head>\n" +
                "      <meta charset='utf-8'>\n" +
                "      <meta name='viewport' content='width=device-width'>\n" +
                "      <title></title>\n" +
                "    </head>\n" +
                "    <body>\n" + " <p>\n" +
                "                  Maharshi Shah built the PseudoGen app as\n" +
                "                  an Ad Supported app. This SERVICE is provided by\n" +
                "                  Maharshi Shah at no cost and is intended for use as\n" +
                "                  is.\n" +
                "                </p> <p>\n" +
                "                  This page is used to inform visitors regarding my\n" +
                "                  policies with the collection, use, and disclosure of Personal\n" +
                "                  Information if anyone decided to use my Service.\n" +
                "                </p> <p>\n" +
                "                  If you choose to use my Service, then you agree to\n" +
                "                  the collection and use of information in relation to this\n" +
                "                  policy. The Personal Information that I collect is\n" +
                "                  used for providing and improving the Service. I will not use or share your information with\n" +
                "                  anyone except as described in this Privacy Policy.\n" +
                "                </p> <p>\n" +
                "                  The terms used in this Privacy Policy have the same meanings\n" +
                "                  as in our Terms and Conditions, which is accessible at\n" +
                "                  PseudoGen unless otherwise defined in this Privacy Policy.\n" +
                "                </p> <p><strong>Information Collection and Use</strong></p> <p>\n" +
                "                  For a better experience, while using our Service, I\n" +
                "                  may require you to provide us with certain personally\n" +
                "                  identifiable information, including but not limited to Name, Last Name, Email ID. The information that\n" +
                "                  I request will be retained on your device and is not collected by me in any way.\n" +
                "                </p> <div><p>\n" +
                "                    The app does use third party services that may collect\n" +
                "                    information used to identify you.\n" +
                "                  </p> <p>\n" +
                "                    Third party service providers used" +
                "                    by the app \n" +
                "                  <li>Google Play Services</li><li>AdMob</li><li>Google Analytics for Firebase</li><li>Firebase Crashlytics</li></ul></div> <p><strong>Log Data</strong></p> <p>\n" +
                "                  I want to inform you that whenever you\n" +
                "                  use my Service, in a case of an error in the app\n" +
                "                  I collect data and information (through third party\n" +
                "                  products) on your phone called Log Data. This Log Data may\n" +
                "                  include information such as your device Internet Protocol\n" +
                "                  (“IP”) address, device name, operating system version, the\n" +
                "                  configuration of the app when utilizing my Service,\n" +
                "                  the time and date of your use of the Service, and other\n" +
                "                  statistics.\n" +
                "                </p> <p><strong>Cookies</strong></p> <p>\n" +
                "                  Cookies are files with a small amount of data that are\n" +
                "                  commonly used as anonymous unique identifiers. These are sent\n" +
                "                  to your browser from the websites that you visit and are\n" +
                "                  stored on your device's internal memory.\n" +
                "                </p> <p>\n" +
                "                  This Service does not use these “cookies” explicitly. However,\n" +
                "                  the app may use third party code and libraries that use\n" +
                "                  “cookies” to collect information and improve their services.\n" +
                "                  You have the option to either accept or refuse these cookies\n" +
                "                  and know when a cookie is being sent to your device. If you\n" +
                "                  choose to refuse our cookies, you may not be able to use some\n" +
                "                  portions of this Service.\n" +
                "                </p> <p><strong>Service Providers</strong></p> <p>\n" +
                "                  I may employ third-party companies and\n" +
                "                  individuals due to the following reasons:\n" +
                "                </p> <ul><li>To facilitate our Service;</li> <li>To provide the Service on our behalf;</li> <li>To perform Service-related services; or</li> <li>To assist us in analyzing how our Service is used.</li></ul> <p>\n" +
                "                  I want to inform users of this Service\n" +
                "                  that these third parties have access to your Personal\n" +
                "                  Information. The reason is to perform the tasks assigned to\n" +
                "                  them on our behalf. However, they are obligated not to\n" +
                "                  disclose or use the information for any other purpose.\n" +
                "                </p> <p><strong>Security</strong></p> <p>\n" +
                "                  I value your trust in providing us your\n" +
                "                  Personal Information, thus we are striving to use commercially\n" +
                "                  acceptable means of protecting it. But remember that no method\n" +
                "                  of transmission over the internet, or method of electronic\n" +
                "                  storage is 100% secure and reliable, and I cannot\n" +
                "                  guarantee its absolute security.\n" +
                "                </p> <p><strong>Links to Other Sites</strong></p> <p>\n" +
                "                  This Service may contain links to other sites. If you click on\n" +
                "                  a third-party link, you will be directed to that site. Note\n" +
                "                  that these external sites are not operated by me.\n" +
                "                  Therefore, I strongly advise you to review the\n" +
                "                  Privacy Policy of these websites. I have\n" +
                "                  no control over and assume no responsibility for the content,\n" +
                "                  privacy policies, or practices of any third-party sites or\n" +
                "                  services.\n" +
                "                </p> <p><strong>Children’s Privacy</strong></p> <p>\n" +
                "                  These Services do not address anyone under the age of 13.\n" +
                "                  I do not knowingly collect personally\n" +
                "                  identifiable information from children under 13. In the case\n" +
                "                  I discover that a child under 13 has provided\n" +
                "                  me with personal information, I immediately\n" +
                "                  delete this from our servers. If you are a parent or guardian\n" +
                "                  and you are aware that your child has provided us with\n" +
                "                  personal information, please contact me so that\n" +
                "                  I will be able to do necessary actions.\n" +
                "                </p> <p><strong>Changes to This Privacy Policy</strong></p> <p>\n" +
                "                  I may update our Privacy Policy from\n" +
                "                  time to time. Thus, you are advised to review this page\n" +
                "                  periodically for any changes. I will\n" +
                "                  notify you of any changes by posting the new Privacy Policy on\n" +
                "                  this page.\n" +
                "                </p> <p>This policy is effective as of 2021-01-03</p> <p><strong>Contact Us</strong></p> <p>\n" +
                "                  If you have any questions or suggestions about my\n" +
                "                  Privacy Policy, do not hesitate to contact me at pseudogenhelp@gmail.com.\n" +
                "                </p> <p>This privacy policy page was created at App Privacy Policy Generator </p>\n" +
                "    </body>\n" +
                "    </html>\n" +
                "      ";
        policyDescription.setText ( HtmlCompat.fromHtml(htmlText, 0) );
        alert.show ();
    }

    public void termsOfUseClicked ( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder ( this, R.style.CutomAlertDialog  );
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.terms_of_use_dialog, null);
        alert.setView ( dialogView );
        alert.setTitle ( "Terms & Conditions" );
        alert.setIcon ( R.drawable.pseudogenlogo_layer_list );
        final TextView termsOfUseDescription = dialogView.findViewById ( R.id.termsOfUseTextView );
        String htmlText = "<!DOCTYPE html>\n" +
                "    <html>\n" +
                "    <head>\n" +
                "      <meta charset='utf-8'>\n" +
                "      <meta name='viewport' content='width=device-width'>\n" +
                "      <title></title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "    <p>\n" +
                "                  By downloading or using the app, these terms will\n" +
                "                  automatically apply to you – you should make sure therefore\n" +
                "                  that you read them carefully before using the app. You’re not\n" +
                "                  allowed to copy, or modify the app, any part of the app, or\n" +
                "                  our trademarks in any way. You’re not allowed to attempt to\n" +
                "                  extract the source code of the app, and you also shouldn’t try\n" +
                "                  to translate the app into other languages, or make derivative\n" +
                "                  versions. The app itself, and all the trade marks, copyright,\n" +
                "                  database rights and other intellectual property rights related\n" +
                "                  to it, still belong to Maharshi Shah.\n" +
                "                </p> <p>\n" +
                "                  Maharshi Shah is committed to ensuring that the app is\n" +
                "                  as useful and efficient as possible. For that reason, we\n" +
                "                  reserve the right to make changes to the app or to charge for\n" +
                "                  its services, at any time and for any reason. We will never\n" +
                "                  charge you for the app or its services without making it very\n" +
                "                  clear to you exactly what you’re paying for.\n" +
                "                </p> <p>\n" +
                "                  The PseudoGen app stores and processes personal data that\n" +
                "                  you have provided to us, in order to provide my\n" +
                "                  Service. It’s your responsibility to keep your phone and\n" +
                "                  access to the app secure. We therefore recommend that you do\n" +
                "                  not jailbreak or root your phone, which is the process of\n" +
                "                  removing software restrictions and limitations imposed by the\n" +
                "                  official operating system of your device. It could make your\n" +
                "                  phone vulnerable to malware/viruses/malicious programs,\n" +
                "                  compromise your phone’s security features and it could mean\n" +
                "                  that the PseudoGen app won’t work properly or at all.\n" +
                "                </p> <div><p>\n" +
                "                    The app does use third party services that declare their own\n" +
                "                    Terms and Conditions.\n" +
                "                  </p> <p>\n" +
                "                    Third party service\n" +
                "                    providers used by the app\n" +
                "                  </p> <ul><li>Google Play Services</a></li><li>AdMob</a></li><li>Google Analytics for Firebase</li><li>Firebase Crashlytics</li></ul></div> <p>\n" +
                "                  You should be aware that there are certain things that\n" +
                "                  Maharshi Shah will not take responsibility for. Certain\n" +
                "                  functions of the app will require the app to have an active\n" +
                "                  internet connection. The connection can be Wi-Fi, or provided\n" +
                "                  by your mobile network provider, but Maharshi Shah\n" +
                "                  cannot take responsibility for the app not working at full\n" +
                "                  functionality if you don’t have access to Wi-Fi, and you don’t\n" +
                "                  have any of your data allowance left.\n" +
                "                </p> <p></p> <p>\n" +
                "                  If you’re using the app outside of an area with Wi-Fi, you\n" +
                "                  should remember that your terms of the agreement with your\n" +
                "                  mobile network provider will still apply. As a result, you may\n" +
                "                  be charged by your mobile provider for the cost of data for\n" +
                "                  the duration of the connection while accessing the app, or\n" +
                "                  other third party charges. In using the app, you’re accepting\n" +
                "                  responsibility for any such charges, including roaming data\n" +
                "                  charges if you use the app outside of your home territory\n" +
                "                  (i.e. region or country) without turning off data roaming. If\n" +
                "                  you are not the bill payer for the device on which you’re\n" +
                "                  using the app, please be aware that we assume that you have\n" +
                "                  received permission from the bill payer for using the app.\n" +
                "                </p> <p>\n" +
                "                  Along the same lines, Maharshi Shah cannot always take\n" +
                "                  responsibility for the way you use the app i.e. You need to\n" +
                "                  make sure that your device stays charged – if it runs out of\n" +
                "                  battery and you can’t turn it on to avail the Service,\n" +
                "                  Maharshi Shah cannot accept responsibility.\n" +
                "                </p> <p>\n" +
                "                  With respect to Maharshi Shah’s responsibility for your\n" +
                "                  use of the app, when you’re using the app, it’s important to\n" +
                "                  bear in mind that although we endeavour to ensure that it is\n" +
                "                  updated and correct at all times, we do rely on third parties\n" +
                "                  to provide information to us so that we can make it available\n" +
                "                  to you. Maharshi Shah accepts no liability for any\n" +
                "                  loss, direct or indirect, you experience as a result of\n" +
                "                  relying wholly on this functionality of the app.\n" +
                "                </p> <p>\n" +
                "                  At some point, we may wish to update the app. The app is\n" +
                "                  currently available on Android – the requirements for\n" +
                "                  system(and for any additional systems we\n" +
                "                  decide to extend the availability of the app to) may change,\n" +
                "                  and you’ll need to download the updates if you want to keep\n" +
                "                  using the app. Maharshi Shah does not promise that it\n" +
                "                  will always update the app so that it is relevant to you\n" +
                "                  and/or works with the Android version that you have\n" +
                "                  installed on your device. However, you promise to always\n" +
                "                  accept updates to the application when offered to you, We may\n" +
                "                  also wish to stop providing the app, and may terminate use of\n" +
                "                  it at any time without giving notice of termination to you.\n" +
                "                  Unless we tell you otherwise, upon any termination, (a) the\n" +
                "                  rights and licenses granted to you in these terms will end;\n" +
                "                  (b) you must stop using the app, and (if needed) delete it\n" +
                "                  from your device.\n" +
                "                </p> <p><strong>Changes to This Terms and Conditions</strong></p> <p>\n" +
                "                  I may update our Terms and Conditions\n" +
                "                  from time to time. Thus, you are advised to review this page\n" +
                "                  periodically for any changes. I will\n" +
                "                  notify you of any changes by posting the new Terms and\n" +
                "                  Conditions on this page.\n" +
                "                </p> <p>\n" +
                "                  These terms and conditions are effective as of 2021-01-03\n" +
                "                </p> <p><strong>Contact Us</strong></p> <p>\n" +
                "                  If you have any questions or suggestions about my\n" +
                "                  Terms and Conditions, do not hesitate to contact me\n" +
                "                  at support.pseudogen@gmail.com.\n" +
                "                </p> <p>This Terms and Conditions page was generated by App Privacy Policy Generator</p>\n" +
                "    </body>\n" +
                "    </html>\n" +
                "      ";
        termsOfUseDescription.setText ( HtmlCompat.fromHtml(htmlText, 0) );
        alert.show ();
    }

    public void setUnderlinedTextColor ( TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                // set color here
                //wordToSpan.setSpan(new BackgroundColorSpan (0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordToSpan.setSpan(new ForegroundColorSpan (Color.BLUE), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordToSpan.setSpan(new UnderlineSpan (), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    public void setHighlightTextColor ( TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                // set color here
                //wordToSpan.setSpan(new BackgroundColorSpan (0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordToSpan.setSpan(new ForegroundColorSpan (getResources ().getColor ( R.color.bright_orange )), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        welcomeDialogShown = mPrefs.getBoolean ( welcomeDialogShownPref, false );
        if(!welcomeDialogShown)
        {
            createDialogForWelcome ();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeDialogShownPref, true);
            editor.commit();
        }

        MobileAds.initialize(this);
        centralInterstitialAd = new InterstitialAd(this);
        centralInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        Intent intent = getIntent ();
        enableInterstitialAd = intent.getBooleanExtra ( "enableInterstitial", false);
        centralInterstitialAd.loadAd(new AdRequest.Builder().build());

        centralInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded () {
                if(enableInterstitialAd == true){
                    centralInterstitialAd.show ();
                }
            }

            @Override
            public void onAdClosed () {
                enableInterstitialAd = false;
                centralInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

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
                        hideBottomNav();
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
        db.createPostTable ( getUsernameForTable() );
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
                codeListExpandableListView.expandGroup ( groupPos );
                showBottomNav();
                return true;
            }
        });

        //Collapses other list items except for the selected one
        codeListExpandableListView.setOnGroupExpandListener ( new ExpandableListView.OnGroupExpandListener ( ) {
            @Override
            public void onGroupExpand ( int groupPosition ) {
                if(lastExpandedPostion != -1 && groupPosition != lastExpandedPostion)
                {
                    codeListExpandableListView.collapseGroup ( lastExpandedPostion );
                }
                lastExpandedPostion = groupPosition;
            }
        } );

        //hides bottom nav on collapsing group
        codeListExpandableListView.setOnGroupCollapseListener ( new ExpandableListView.OnGroupCollapseListener ( ) {
            @Override
            public void onGroupCollapse ( int groupPosition ) {
                hideBottomNav ();
            }
        } );

        centralAdViewBanner = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        centralAdViewBanner.loadAd(adRequest);
    }
}