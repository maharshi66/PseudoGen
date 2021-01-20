package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance ( );
    TextView forgotPasswordTextView;
    private static final int RC_SIGN_IN = 666;
    GoogleSignInClient mGoogleSignInClient;

    public void signInWithEmailClicked ( View view ) {
        Intent intent = new Intent ( this , EmailSignInActivity.class );
        startActivity ( intent );
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        if ( FirebaseAuth.getInstance ( ).getCurrentUser ( ) != null ) {
            FirebaseAuth.getInstance ( ).signOut ( );
        }else
        {
            finishAffinity ();
            finish ();
        }
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        setContentView ( R.layout.activity_login );
        setTitle ( "Sign In" );
        createRequest();
    }

    private void createRequest () {
    // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void signInWithGoogleClicked ( View view ) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult ( signInIntent, RC_SIGN_IN );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                String message = e.getMessage ();
                Toast.makeText ( this , message , Toast.LENGTH_LONG ).show ( );
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI ( user );
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText ( LoginActivity.this , "signInWithCredential:failure" +  task.getException(), Toast.LENGTH_SHORT ).show ( );
                            updateUI ( null );
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent ( LoginActivity.this, CentralActivity.class );
            startActivity ( intent );
        } else {}
    }

    public void privacyPolicyClicked ( View view ) {
        AlertDialog.Builder alert = new AlertDialog.Builder ( this, R.style.CutomAlertDialog  );
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.privacy_policy_dialog, null);
        alert.setView ( dialogView );
        final TextView policyDescription = dialogView.findViewById ( R.id.privacyPolicyDescriptionTextView );
        String htmlText = "<!DOCTYPE html>\n" +
                "    <html>\n" +
                "    <head>\n" +
                "      <meta charset='utf-8'>\n" +
                "      <meta name='viewport' content='width=device-width'>\n" +
                "      <title></title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "    <strong>Privacy Policy</strong> <p>\n" +
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

    public void termsOfUseClicked ( View view ) {
        AlertDialog.Builder alert = new AlertDialog.Builder ( this, R.style.CutomAlertDialog  );
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.terms_of_use_dialog, null);
        alert.setView ( dialogView );
        final TextView termsOfUseDescription = dialogView.findViewById ( R.id.termsOfUseTextView );
        String htmlText = "<!DOCTYPE html>\n" +
                "    <html>\n" +
                "    <head>\n" +
                "      <meta charset='utf-8'>\n" +
                "      <meta name='viewport' content='width=device-width'>\n" +
                "      <title></title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "    <strong>Terms &amp; Conditions</strong> <p>\n" +
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

    public void signInAsGuestClicked ( View view ) {
        Intent intent = new Intent(LoginActivity.this, CentralActivity.class);
        startActivity ( intent );
    }
}