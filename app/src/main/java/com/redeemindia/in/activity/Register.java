package com.redeemindia.in.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.redeemindia.in.ProgressBarAnimation;
import com.redeemindia.in.R;
import com.redeemindia.in.databinding.ActivityRegisterBinding;
import com.redeemindia.in.models.User;
import com.redeemindia.in.onboarding.BasicDetails;
import com.redeemindia.in.onboarding.MainDetails;
import com.redeemindia.in.onboarding.OtpFragment;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        switch (getProgressAndStage()){
            case 1 :
                BasicDetails basicDetailsFragment = new BasicDetails();
                increaseProgress(0,30);
                loadFragment(basicDetailsFragment,"splash");
                break;

            case 2 :
                MainDetails mainDetailsFragment = MainDetails.newInstance();
                increaseProgress(0,60);
                loadFragment(mainDetailsFragment,"maindetails");
                break;


            case 3 :
                OtpFragment otpFragment = OtpFragment.newInstance();
                increaseProgress(0,85);
                loadFragment(otpFragment,"maindetails");
                break;
        }
    }

    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contaier, fragment,tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }



    public int getProgressAndStage() {
        SharedPreferences sharedPreferences = getSharedPreferences("RIPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("com.ri.onboardstage",1);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    void increaseProgress(int x , int y){

        ProgressBarAnimation anim = new ProgressBarAnimation(binding.progressBar, x, y);
        anim.setDuration(800);
       binding.progressBar.startAnimation(anim);
    }

}