package com.redeemindia.in.onboarding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.redeemindia.in.PrefPaths;
import com.redeemindia.in.ProgressBarAnimation;
import com.redeemindia.in.R;
import com.redeemindia.in.databinding.FragmentMainDetailsBinding;
import com.redeemindia.in.models.User;
import com.redeemindia.in.suppliment.Note;
import com.redeemindia.in.viewmodel.OnboardingViewModel;

import java.util.Random;


public class MainDetails extends Fragment {


    FragmentMainDetailsBinding binding;
    EditText fname, mname, lname, acode, dob;
   String country, state, city, username, email, password , fsname ,lsname, msnam , ascode, dsob;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button next;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    OnboardingViewModel viewModel;
    Random random;


    public MainDetails() {
        // Required empty public constructor
    }


    public static MainDetails newInstance() {
        MainDetails fragment = new MainDetails();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);
        viewModel.init();
        random = new Random();
        sharedPreferences = getActivity().getSharedPreferences("RIPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Note(x = "All the data is saving from here ")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRandomDetails();
        mAuth = FirebaseAuth.getInstance();
        initializeViews();



        next.setOnClickListener(view1 -> {

            if (!validation()){
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else {

                setAllData();
                mAuth.createUserWithEmailAndPassword(email,password.toString())
                        .addOnCompleteListener(getActivity(), task -> {
                            showDialog("Loading");
                            if (task.isSuccessful()) {

                                dialog.dismiss();
                                firebaseUser = mAuth.getCurrentUser();
                                assert firebaseUser != null;

                                User user = new User();
                                user.setUID(firebaseUser.getUid());
                                user.setCountry(country);
                                user.setState(state);
                                user.setCity(city);
                                user.setUname(username);
                                user.setEmail(email);
                                user.setPassword(password);
                                user.setFirstname(fsname);
                                user.setMiddlename(msnam);
                                user.setLastname(lsname);
                                user.setAccesscode(ascode);
                                user.setDob(dsob);

                                     viewModel.createUser(user,firebaseUser);
                                    Toast.makeText(getContext(), "Details posted, now verify", Toast.LENGTH_SHORT).show();
                                    syncSharedPrefs();
                                    OtpFragment otpFragment = new OtpFragment();
                                    loadNextFragment(otpFragment);

                                }

                             else {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Firebase : Registration failed." + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });



    }



    void setAllData(){

        fsname= fname.getText().toString();
        msnam = mname.getText().toString();
        lsname = lname.getText().toString();
        ascode = acode.getText().toString();
        dsob = dob.getText().toString();

        country = sharedPreferences.getString(PrefPaths.country,"Error");
        state = sharedPreferences.getString(PrefPaths.state, "Error");
        city = sharedPreferences.getString(PrefPaths.city, "Error");
        username = sharedPreferences.getString(PrefPaths.userName, "Error");
        password = sharedPreferences.getString(PrefPaths.passwd, "Error");
        email = sharedPreferences.getString(PrefPaths.email, "Error");

    }


    void showDialog(String msg){

        dialog.setTitle("Please Wait");
        dialog.setCancelable(false);
        dialog.setMessage(msg);

    }

    void initializeViews(){
        fname = binding.fname;
        lname = binding.lname;
        mname = binding.mname;
        acode = binding.Accesscode;
        dob = binding.editTextDate;
        next =  binding.submit;
        dialog=new ProgressDialog(getContext());
        progressBar = getActivity().findViewById(R.id.progress_bar);


    }

    private boolean validation()
    {
        boolean validate=true;

        if(fname.getText()==null||fname.getText().toString().trim().length()==0)
        {
            validate=false;
        }

        else if(mname.getText()==null||mname.getText().toString().trim().length()==0)
        {
            validate=false;
        }
        else if(lname.getText()==null||lname.getText().toString().trim().length()==0)
        {
            validate=false;
        }
        else if( acode.getText().toString().trim().length()!=4 || acode.getText()==null|| acode.getText().toString().trim().length()==0 )
        {
            validate=false;
            acode.setError("Access code must be of 4 characters only");
            acode.requestFocus();
            InputMethodManager mgr = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(acode, InputMethodManager.SHOW_IMPLICIT);
        }
        else if(dob.getText()==null||dob.getText().toString().trim().length()==0)
        {
            validate=false;
        }


        return validate;

    }



    public void syncSharedPrefs() {

       // editor.putString("com.ri.uname", username.getText().toString());

        editor.putString(PrefPaths.firstName, fname.getText().toString());
        editor.putString(PrefPaths.middleName, mname.getText().toString());
        editor.putString(PrefPaths.lastName, lname.getText().toString());
        editor.putString(PrefPaths.accessCode, acode.getText().toString());
        editor.putString(PrefPaths.DOB, dob.getText().toString());
        editor.putInt(PrefPaths.onBoardStage, 3);
        editor.apply();
    }

    private void loadNextFragment(Fragment fragment){
        increaseProgress(60,85);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contaier,fragment)
                .commit();
    }

    void increaseProgress(int x , int y){

        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, x, y);
        anim.setDuration(800);
        progressBar.startAnimation(anim);
    }

    public void setRandomDetails(){
        binding.fname.setText("Appun"+random.nextInt(1000));
        binding.mname.setText("kumar"+random.nextInt(1000));
        binding.lname.setText("Katya"+random.nextInt(1000));
        binding.Accesscode.setText(random.nextInt(10)+1240+"");
        binding.editTextDate.setText(random.nextInt(1000)+0040+"");

    }

}