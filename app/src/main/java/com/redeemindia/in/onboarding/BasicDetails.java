package com.redeemindia.in.onboarding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.redeemindia.in.R;
import com.redeemindia.in.databinding.FragmentBasicDetailsBinding;
import com.redeemindia.in.models.User;
import com.redeemindia.in.viewmodel.BasicDetailsViewModel;

public class BasicDetails extends Fragment {



    EditText country, state, city, username, email, password;
    Button next;
    User user;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "BasicDetails";
    View view;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    ProgressDialog dialog;
    FragmentBasicDetailsBinding binding;
    BasicDetailsViewModel viewModel;

    private String mParam1;
    private String mParam2;


    public BasicDetails() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.setTitle("Please Wait");
        dialog.setCancelable(false);
        dialog.setMessage("Loading");

    }

    public static BasicDetails newInstance() {
        return new BasicDetails();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BasicDetailsViewModel.class);
        viewModel.init();
        initializeViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentBasicDetailsBinding.inflate(inflater, container, false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        next.setOnClickListener(view1 -> {
            if (validation()){
                Toast.makeText(getActivity(), "Please fill in all the field", Toast.LENGTH_SHORT).show();
            }else {

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), task -> {

                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                firebaseUser = mAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String con = country.getText().toString();
                                String st = state.getText().toString();
                                String ci = city.getText().toString();
                                String uname = username.getText().toString();
                                String pass = password.getText().toString();
                                String em = email.getText().toString();

                               if  (viewModel.createUser(firebaseUser,st,ci,con,uname,em,pass)){
                                   Toast.makeText(getContext(), "Details posted, now verify", Toast.LENGTH_SHORT).show();
                                   syncSharedPrefs();
                                   loadNextFragment();
                               }else {
                                   Toast.makeText(getContext(), "Error in details posting", Toast.LENGTH_SHORT).show();
                               }

                            } else {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Firebase : Registration failed." + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });


    }


    private void initializeViews() {

        country = binding.countrytxt;
        email = binding.emailtxt;
        state = binding.statetxt;
        city = binding.citytxt;
        username = binding.username;
        next = binding.submit;
        dialog=new ProgressDialog(getContext());
    }

    private boolean validation()
    {
        boolean validate=true;

         if(email.getText()==null||email.getText().toString().trim().length()==0)
        {
            validate=false;
        }

        else if(country.getText()==null||country.getText().toString().trim().length()==0)
        {
            validate=false;
        }
        else if(city.getText()==null||city.getText().toString().trim().length()==0)
        {
            validate=false;
        }
        else if(username.getText()==null||username.getText().toString().trim().length()==0 && username.length()<6)
        {
            validate=false;
            username.setError("Username must be of at-least 6 characters");
            username.requestFocus();
            InputMethodManager mgr = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);
        }
         else if(state.getText()==null||state.getText().toString().trim().length()==0)
         {
             validate=false;
         }

        return validate;

    }


    public void syncSharedPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LootPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("com.ri.uname", username.getText().toString());
        editor.putString("com.ri.state", state.getText().toString());
        editor.putString("com.ri.country", country.getText().toString());
        editor.putString("com.ri.city", city.getText().toString());
        editor.putString("com.ri.email", email.getText().toString());
        editor.putInt("com.ri.onboardstage", 2);

        editor.apply();
    }

    private void loadNextFragment(){
        OtpFragment fragment = OtpFragment.newInstance();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame,fragment)
                .commit();
    }




}