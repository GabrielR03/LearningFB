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

import com.gabriel.testapplication.databinding.FragmentItemsBinding;
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

public class ItemsFragment extends Fragment {

    private FragmentItemsBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentItemsBinding.inflate(getLayoutInflater());
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
                String editItemName = binding.editItemName.getText().toString();
                String editQuantity = binding.editQuantity.getText().toString();
                int editId = Integer.parseInt(binding.editIdentification.getText().toString());
                if(editItemName.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Filling the Item name EditText is mandatory!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else{
                    Map<String, Object> usersEditMap = new HashMap<String, Object>() {{
                        put("Name", editItemName);
                        put("Quantity", editQuantity);
                        put("ID", editId);
                    }};
                    db.collection("Items").document(editItemName).set(usersEditMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("db_edittext", "Success in writing the data");
                            binding.editItemName.setText("");
                            binding.editQuantity.setText("");
                            binding.editIdentification.setText("");
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
                String editItemName = binding.editItemName.getText().toString();
                String editQuantity = binding.editQuantity.getText().toString();
                int editId = Integer.parseInt(binding.editQuantity.getText().toString());
                if(editItemName.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Filling the Item Name EditText is mandatory!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else {
                    db.collection("Items").document(editItemName).update("ID", editId,"Quantity", editQuantity).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                String editItem = binding.editPowwow.getText().toString();
                if(editItem.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Fill in the user EditText!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else{
                    db.collection("Items").document(editItem).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                            if(document != null){
                                if(document.getString("Name") != null){
                                    binding.resultItemName.setText(document.getString("Name"));
                                }else{
                                    binding.resultItemName.setText("Error while trying to access name!");
                                }
                                if(document.getString("Quantity") != null){
                                    binding.resultID.setText(document.getString("Quantity"));
                                }else{
                                    binding.resultID.setText("Error while trying to access surname!");
                                }
                                if(document.get("ID") != null){
                                    binding.resultQuantity.setText(document.get("ID").toString());
                                }else{
                                    binding.resultQuantity.setText("Error while trying to access surname!");
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
                String editItem = binding.editPowwow.getText().toString();
                if(editItem.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Fill in the user EditText!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }else {
                    db.collection("Items").document(editItem).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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