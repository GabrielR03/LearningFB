package com.gabriel.testapplication.view.formregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.testapplication.R;
import com.gabriel.testapplication.databinding.ActivityFormRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class FormRegister extends AppCompatActivity {

    private ActivityFormRegisterBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button button = (Button)findViewById(binding.buttonRegister.getId());
        button.setText(binding.buttonRegister.getText());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String email = binding.editEmail.getText().toString();
                String password = binding.editPassword.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, "Fill in all fields", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else{
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Snackbar snackbar = Snackbar.make(view, "Successfully registered!", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.GREEN);
                                snackbar.show();
                                binding.editEmail.setText("");
                                binding.editPassword.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String exceptionMessage;
                            if(e instanceof FirebaseAuthUserCollisionException){
                                exceptionMessage = "User is already registered!";
                            }else if(e instanceof FirebaseAuthWeakPasswordException){
                                exceptionMessage = "Password must have 6 or more characters";
                            }else if(e instanceof FirebaseAuthInvalidCredentialsException){
                                exceptionMessage = "Use only valid credentials!";
                            }else if(e instanceof FirebaseNetworkException){
                                exceptionMessage = "You must have internet connection to sign in!";
                            }else{
                                exceptionMessage = "Error when trying to register user";
                            }
                            Snackbar snackbar = Snackbar.make(view, exceptionMessage, Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.GREEN);
                            snackbar.show();
                        }
                    });
                }
            }
        });
    }
}