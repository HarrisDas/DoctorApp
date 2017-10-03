package com.example.harri.mydoctorapp.UserAccountCreationFlow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.harri.mydoctorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserAccountCreationActivity extends AppCompatActivity {
    private static Context context;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase DatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_creation2);
        context = getApplicationContext();
        mAuth= FirebaseAuth.getInstance();
     //  sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.prefKey),0);
       String membershipType = "";
        membershipType = sharedPreferences.getString(getResources().getString(R.string.prefType), "");
        if(mAuth.getCurrentUser()!=null&&!membershipType.equals("")){



        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new SignInFragment())
                .commit();

    }
}
