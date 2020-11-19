package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailSignInActivity extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
    EditText usernameEditText;
    EditText passwordEditText;
    String userEmail;
    String userPassword;
    String firstName;
    String lastName;

    public void signInClicked(View view)
    {
        if(mAuth.getCurrentUser() == null)
        {
            signInUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_in);
        setTitle("Sign in With Email");
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void signInUser(String username, String password)
    {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("INFO:", "User signed in " + username);
                            //TODO Switch Activity
                        }else
                        {
                            try
                            {
                                throw task.getException();
                            }catch (Exception e)
                            {
                                Toast.makeText(EmailSignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            startSignUpDialog();
                        }
                    }
                });
    }

    public void createNewUser(String username, String password)
    {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         Toast.makeText(getApplicationContext(), "Signed Up!", Toast.LENGTH_SHORT).show();
                         //Update Database
                         writeToDatabase(firstName, lastName, userEmail, userPassword);

                         //TODO Switch Activity
                     }else
                     {
                        try{
                            throw task.getException();
                        }catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                     }
                    }
                });
    }

    public void startSignUpDialog()
    {

        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(EmailSignInActivity.this);
        LayoutInflater layoutInflater = EmailSignInActivity.this.getLayoutInflater();

        final View dialogView = layoutInflater.inflate(R.layout.signup_dialog, null);

        final EditText signUpEmailText = dialogView.findViewById(R.id.signUpEmailText);
        final EditText signUpPasswordEditText = dialogView.findViewById(R.id.signUpPasswordEditText);
        final EditText firstNameEditText = dialogView.findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = dialogView.findViewById(R.id.lastNameEditText);

        final EditText retypePasswordEditText = dialogView.findViewById(R.id.retypePasswordEditText);

        alertDialogBuilder.setView(dialogView)
                .setTitle("Sign Up")
                .setCancelable(true)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Create new user from details entered in alert dialog
                        userEmail = signUpEmailText.getText().toString();
                        userPassword = signUpPasswordEditText.getText().toString();
                        firstName = firstNameEditText.getText().toString();
                        lastName = lastNameEditText.getText().toString();

                        String retypePassword = retypePasswordEditText.getText().toString();
                        if(!userPassword.isEmpty() && !retypePassword.isEmpty())
                        {
                            createNewUser(userEmail, userPassword);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void writeToDatabase(String firstName, String lastName, String userEmail, String userPassword)
    {
        firebaseDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Credentials").child("First Name").setValue(firstName);
        firebaseDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Credentials").child("Last Name").setValue(lastName);
        firebaseDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Credentials").child("Email").setValue(userEmail);
        firebaseDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Credentials").child("Password").setValue(userPassword);
    }
}