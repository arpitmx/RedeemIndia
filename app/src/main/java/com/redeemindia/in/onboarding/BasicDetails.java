package com.redeemindia.in.onboarding;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.redeemindia.in.ProgressBarAnimation;
import com.redeemindia.in.R;
import com.redeemindia.in.databinding.FragmentBasicDetailsBinding;
import com.redeemindia.in.models.User;
import com.redeemindia.in.viewmodel.OnboardingViewModel;

import java.util.Random;

public class BasicDetails extends Fragment {



    EditText country, state, city, username, email, password;
    Button next;
    User user;
    private static final String TAG = "BasicDetails";
    View view;
    Random random;

    ProgressDialog dialog;
    FragmentBasicDetailsBinding binding;
    OnboardingViewModel viewModel;

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

        random = new Random();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentBasicDetailsBinding.inflate(inflater, container, false);
       Toast.makeText(getContext(), "Enter basic details", Toast.LENGTH_SHORT).show();
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews();
        setRandomDetails();




        next.setOnClickListener(view1 -> {
            if (!validation()){
                Toast.makeText(getActivity(), "Please fill in all the field", Toast.LENGTH_SHORT).show();
            }else {
                MainDetails fragment = new MainDetails();
                syncSharedPrefs();
                increaseProgress(70);
                loadNextFragment(fragment) ;

            }
        });


    }

    void increaseProgress(int p){
     ProgressBar progressBar= getActivity().findViewById(R.id.progress_bar);

        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 30, 60);
        anim.setDuration(800);
        progressBar.startAnimation(anim);
    }





    private void initializeViews() {

        country =  binding.countrytxt;
        email = binding.emailtxt;
        state = binding.statetxt;
        city = binding.citytxt;
        username = binding.username;
        password = binding.passtxt;
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
        else if( username.getText().toString().trim().length()<6 || username.getText()==null||username.getText().toString().trim().length()==0 )
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
         else if( password.getText().toString().trim().length()<6 || password.getText()==null||password.getText().toString().trim().length()==0 )
         {
             validate=false;
             password.setError("Password must be of atleast 6 characters");
             password.requestFocus();
             InputMethodManager mgr = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
             mgr.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);
         }

        return validate;

    }


    public void syncSharedPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RIPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("com.ri.uname", username.getText().toString());
        editor.putString("com.ri.state", state.getText().toString());
        editor.putString("com.ri.country", country.getText().toString());
        editor.putString("com.ri.city", city.getText().toString());
        editor.putString("com.ri.email", email.getText().toString());
        editor.putString("com.ri.passwd", password.getText().toString());
        editor.putInt("com.ri.onboardstage", 2);
        editor.apply();

    }

    private void loadNextFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contaier,fragment)
                .commit();
    }


    public void setRandomDetails(){
        binding.countrytxt.setText("India");
        binding.statetxt.setText("UP");
        binding.citytxt.setText("Lucknow");
        binding.username.setText("RandomUser"+ random.nextInt(10000));
        binding.emailtxt.setText("arpitmaurya"+random.nextInt(10000)+"@gmail.com");
        binding.passtxt.setText("123456");

    }
}