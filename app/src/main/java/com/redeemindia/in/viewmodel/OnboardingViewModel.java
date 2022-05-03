package com.redeemindia.in.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.redeemindia.in.models.User;

import java.util.HashMap;
import java.util.Map;

public class OnboardingViewModel extends ViewModel {


    FirebaseFirestore db;

    public void init(){
       db  = FirebaseFirestore.getInstance();
    }



    public void createUser(User user,FirebaseUser firebaseUser)
    {

        db.collection("users").document(user.getEmail())
                .set(user).addOnCompleteListener(task -> {

            if(task.isSuccessful())
            {
                Log.i("Firebase","Added ");
            }
            else
            {
                firebaseUser.delete();
                Log.i("Firebase : Error",task.getException().getMessage());
            }
        });
    }


}
