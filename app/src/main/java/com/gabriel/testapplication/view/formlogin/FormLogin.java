package com.gabriel.testapplication.view.formlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.gabriel.testapplication.databinding.ActivityFormLoginBinding;
import com.gabriel.testapplication.view.formregister.FormRegister;
import com.gabriel.testapplication.view.mainpage.MainPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlin.reflect.KFunction;

public class FormLogin extends AppCompatActivity {

    private ActivityFormLoginBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.enterEmail.getText().toString();
                String password = binding.enterPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, "Fill in all fields", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else{
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                navigateMainPage();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String exceptionMessage = "Error while sign in";
                            Snackbar snackbar = Snackbar.make(view, exceptionMessage, Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.RED);
                            snackbar.show();
                        }
                    });
                }
            }
        });
        binding.redirectToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormRegister.class);
                startActivity(intent);
            }
        });
    }

    private void navigateMainPage() {
        Intent intent = new Intent(FormLogin.this, MainPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            navigateMainPage();
        }

    }
}