package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailSignInActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void signInClicked(View view)
    {
        if(mAuth.getCurrentUser() == null)
        {
            createNewUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
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
                    Log.i("INFO:", "User created " + username);
                }else
                {
                    Toast.makeText(EmailSignInActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}