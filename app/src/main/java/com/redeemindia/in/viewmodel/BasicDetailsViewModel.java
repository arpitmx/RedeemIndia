package com.redeemindia.in.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BasicDetailsViewModel extends ViewModel {


    FirebaseFirestore db;
    boolean successReport = false;


    public void init(){
       db  = FirebaseFirestore.getInstance();

    }

    void setReport(boolean issuccess){
        this.successReport = issuccess;
    }
    boolean getReport(){
        return successReport;
    }

    public boolean createUser(FirebaseUser firebaseUser, String state, String city, String country , String username, String email,String password)
    {
        Map<String, Object> user = new HashMap<>();
        user.put("userID", firebaseUser.getUid());
        user.put("state", state);
        user.put("city", city);
        user.put("country", country);
        user.put("uname", username);
        user.put("email", email);
        user.put("password", password);

        db.collection("users").document(firebaseUser.getUid())
                .set(user).addOnCompleteListener(task -> {

            if(task.isSuccessful())
            {
                Log.i("F Added Succesfully","");
                setReport(true);
            }
            else
            {
                setReport(false);
                Log.i("Firebase : Error",task.getException().getMessage());
            }
        });
        return getReport();
    }


}
