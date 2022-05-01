package com.redeemindia.in.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.redeemindia.in.R;
import com.redeemindia.in.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    View view;
    EditText name, email, contact, zeal, username, password;
    ImageView avatars[]=new ImageView[6];
    ImageView tick[]=new ImageView[6];
    int avatarIds[]=new int[6];
    ProgressDialog dialog;
    FirebaseFirestore db;
    int selectedAvatar;
    FirebaseUser firebaseUser;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initializeViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initializeViews() {
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        contact = view.findViewById(R.id.contactNumber);
        zeal = view.findViewById(R.id.zealId);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);

        dialog=new ProgressDialog(this);
    }




}