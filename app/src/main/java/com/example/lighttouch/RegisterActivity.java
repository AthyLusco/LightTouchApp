package com.example.lighttouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.RegisterUser);
        registerUser.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.banner:
                startActivity(new Intent(this,SplashActivity.class));
                break;
            case R.id.RegisterUser:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(name.isEmpty()){
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Password must be 6 characters or more!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Account has been created!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        //Redirect
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Registration failed, please try again!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(RegisterActivity.this, "Registration failed, please try again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.VISIBLE);

                        }
                    }
                });
    }
}