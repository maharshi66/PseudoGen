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

public class EmailSignInActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

    public void createNewUser(String username, String password)
    {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         Toast.makeText(getApplicationContext(), "Signed Up!", Toast.LENGTH_SHORT).show();
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

    public void signInUser(String username, String password)
    {
        mAuth.signInWithEmailAndPassword(username, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.i("INFO:", "User created " + username);
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

    public void startSignUpDialog()
    {

        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(EmailSignInActivity.this);
        LayoutInflater layoutInflater = EmailSignInActivity.this.getLayoutInflater();

        final View dialogView = layoutInflater.inflate(R.layout.signup_dialog, null);

        final EditText signUpEmailText = dialogView.findViewById(R.id.signUpEmailText);
        final EditText signUpPasswordEditText = dialogView.findViewById(R.id.signUpPasswordEditText);
        final EditText retypePasswordEditText = dialogView.findViewById(R.id.retypePasswordEditText);

        alertDialogBuilder.setView(dialogView)
                .setTitle("Sign Up")
                .setCancelable(true)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Create new user from details entered in alert dialog
                        String userEmail = signUpEmailText.getText().toString();
                        String userPassword = signUpPasswordEditText.getText().toString();
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
}