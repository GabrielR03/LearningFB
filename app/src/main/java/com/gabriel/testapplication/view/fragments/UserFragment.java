package com.gabriel.testapplication.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.testapplication.R;
import com.gabriel.testapplication.databinding.ActivityMainPageBinding;
import com.gabriel.testapplication.databinding.FragmentUserBinding;
import com.gabriel.testapplication.view.formlogin.FormLogin;
import com.gabriel.testapplication.view.mainpage.MainPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(getLayoutInflater());
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent backToLogin = new Intent(new MainPage(), FormLogin.class);
                startActivity(backToLogin);
            }
        });

        binding.buttonWriteTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editName = binding.editName.getText().toString();
                String editSurname = binding.editSurname.getText().toString();
                int editAge = Integer.parseInt(binding.editAge.getText().toString());
                if(editName.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Filling the Name EditText is mandatory!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else{
                    Map<String, Object> usersEditMap = new HashMap<String, Object>() {{
                        put("Name", editName);
                        put("Surname", editSurname);
                        put("Age", editAge);
                    }};
                    db.collection("Users").document(editName).set(usersEditMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("db_edittext", "Success in writing the data");
                            binding.editName.setText("");
                            binding.editSurname.setText("");
                            binding.editAge.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db_edittext", "Failure in writing the data");
                        }
                    });
                }
            }
        });

        binding.buttonUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editName = binding.editName.getText().toString();
                String editSurname = binding.editSurname.getText().toString();
                String editAge = binding.editAge.getText().toString();
                if(editName.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Filling the Name EditText is mandatory!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else {
                    db.collection("Users").document(editName).update("Surname", editSurname, "Age", editAge).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("db_update", "Success in updating the data");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db_update", "Failure in updating the data");
                        }
                    });
                }
            }
        });

        binding.buttonReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editUser = binding.editPowwow.getText().toString();
                if(editUser.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Fill in the user EditText!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else{
                    db.collection("Users").document(editUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                            if(document != null){
                                if(document.getString("Name") != null){
                                    binding.resultName.setText(document.getString("Name"));
                                }else{
                                    binding.resultName.setText("Error while trying to access name!");
                                }
                                if(document.getString("Surname") != null){
                                    binding.resultSurname.setText(document.getString("Surname"));
                                }else{
                                    binding.resultSurname.setText("Error while trying to access surname!");
                                }
                                if(document.get("Age") != null){
                                    binding.resultAge.setText(document.get("Age").toString());
                                }else{
                                    binding.resultAge.setText("Error while trying to access age!");
                                }
                            }
                        }
                    });
                }
            }
        });

        binding.buttonDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editUser = binding.editPowwow.getText().toString();
                if(editUser.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Fill in the user EditText!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else {
                    db.collection("Users").document(editUser).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("db_delete", "Success in deleting the data");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db_delete", "Failure in deleting the data");
                        }
                    });
                }
            }
        });
        return binding.getRoot();
    }
}