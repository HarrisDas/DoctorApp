package com.example.harri.mydoctorapp.UserAccountCreationFlow;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.harri.mydoctorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private EditText emailSignIn;


    private EditText passwordSignIn;
    private Button userSignInButton;
    private LinearLayout goToSignUpFragment;

    private  String membershipType;
    private String mEmailSignIn;
    private String mPasswordSignIn;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;
    private SharedPreferences sharedPreferences;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mProgressDialog=new ProgressDialog(getActivity());
        mAuth= FirebaseAuth.getInstance();
        attachingWidgets(rootView);
        attachingComponents();
    return rootView;
    }

    private void attachingComponents() {
userSignInButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(final View v) {
        mEmailSignIn = emailSignIn.getText().toString();
        mPasswordSignIn = passwordSignIn.getText().toString();
        if (TextUtils.isEmpty(mEmailSignIn)&& TextUtils.isEmpty(mPasswordSignIn) ){
            emailSignIn.setError(getString(R.string.enter_email));
            passwordSignIn.setError(getString(R.string.enter_password));

            return;
        }
        if ( !Patterns.EMAIL_ADDRESS.matcher(mEmailSignIn).matches()){
            emailSignIn.setError(getString(R.string.invalid_email));

            return;

        }
        if(TextUtils.isEmpty(mPasswordSignIn))
        {
            passwordSignIn.setError(getString(R.string.enter_password));

            return;

        }
        mProgressDialog.setMessage("Signing in ...");
        mProgressDialog.show();
    mAuth.signInWithEmailAndPassword(mEmailSignIn,mPasswordSignIn)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
            if(!task.isSuccessful()){
                Toast.makeText(getActivity(),"Error while Signing Up",
            Toast.LENGTH_LONG).show();

            }
            else{
                FirebaseDatabase.getInstance().getReference().child("User")
                        .child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            SignUp myUser=dataSnapshot.getValue(SignUp.class);
                            membershipType=myUser.getMemberType();

                            saveUserPref();
                            if (myUser.getMemberType().equals("Doctor")){

                                openDoctorActivity(v);

                            }else if(myUser.getMemberType().equals("Patient")){
                                openPatientActivity(v);
                            }else{
                                Toast.makeText(getActivity(), "Invalid User",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else {

                            Toast.makeText(getActivity(), getString(R.string.user_Not_found),
                                    Toast.LENGTH_LONG).show();



                        }
                    }

                    

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }

                }
            });

    }

});

        goToSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SignUpFragment())
                        .addToBackStack("SignInFragment")
                        .commit();
            }
        });
        emailSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){

                    String emailText = ((EditText)v).getText().toString().trim();

                    if (TextUtils.isEmpty(emailText)){

                        emailSignIn.setError(getString(R.string.enter_email));
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){

                        emailSignIn.setError(getString(R.string.invalid_email));
                    }
                }

            }
        });
    }

    private void openDoctorActivity(View v) {

    }

    private void openPatientActivity(View v) {
        
    }

    private void saveUserPref() {
        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefKey),0);

        sharedPreferences.edit().putString(getResources().getString(R.string.prefType),membershipType).apply();
    }

    private void attachingWidgets(View view) {

        emailSignIn = (EditText) view.findViewById(R.id.user_email_sign_in);
        passwordSignIn = (EditText) view.findViewById(R.id.user_password_sign_in);
        userSignInButton = (Button) view.findViewById(R.id.user_sign_in);
        goToSignUpFragment = (LinearLayout) view.findViewById(R.id.go_to_user_sign_up);
    }

}
