package com.dhakanewsclub.virtualline.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.login.LoginActivity;

public class RegistrationActivity extends AppCompatActivity {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";
    EditText mNameEditText,mPhoneNumberEditText,mPasswordEditText,mConfirmPasswordEditText;
    Button mCreateAccountButton;
    TextView mLinkLoginTextView;

    RegistrationViewModel mRegistrationViewModel;

    private static final String TAG = "SignupActivity";

    //hold view data
    String name,phoneNumber,password,confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        mNameEditText=findViewById(R.id.input_name);
        mPhoneNumberEditText = findViewById(R.id.input_signup_phoneNumber);
        mPasswordEditText = findViewById(R.id.input_password);
        mConfirmPasswordEditText = findViewById(R.id.input_password_confirm);
        mCreateAccountButton = findViewById(R.id.btn_signup);
        mLinkLoginTextView = findViewById(R.id.link_login);
        mRegistrationViewModel= ViewModelProviders.of(this).get(RegistrationViewModel.class);

        createAccountOnClick();
        linkLoginOnCreate();
    }

    private void createAccountOnClick(){
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void linkLoginOnCreate(){
        mLinkLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);

                startActivity(login);
                finish();
            }
        });
    }

    public void getDataFromView(){
        name = mNameEditText.getText().toString();
        phoneNumber = mPhoneNumberEditText.getText().toString();
        password = mPasswordEditText.getText().toString();
        confirmPassword = mConfirmPasswordEditText.getText().toString();
    }

    public boolean validate() {
        boolean valid = true;

        getDataFromView();


        if (name.isEmpty() || name.length() < 3) {
            mNameEditText.setError("at least 3 characters");
            return false;
        } else {
            mNameEditText.setError(null);
        }

        if (phoneNumber.isEmpty() || phoneNumber.length()<11) {
            mPhoneNumberEditText.setError("enter a valid phone number");
            return false;
        } else {
            mPhoneNumberEditText.setError(null);
        }

        if (password.isEmpty() || password.length()<7) {
            mPasswordEditText.setError("password length must be grater than 6 alphanumeric characters");
            return false;
        } else {
            mPasswordEditText.setError(null);
        }
        if(confirmPassword.length()!=password.length()){
            mConfirmPasswordEditText.setError("password does not matched");
            return false;
        }
        else {
            mConfirmPasswordEditText.setError(null);
        }

        return valid;
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            Toast.makeText(this,"Invalid input",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(DIBAGING_TAG,"regi activity");
        mRegistrationViewModel.registration(RegistrationActivity.this,name,phoneNumber,password);
        //mCreateAccountButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this,
//                R.style.Theme_AppCompat_Light_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();



        // TODO: Implement your own signup logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    public void onSignupSuccess() {
        mCreateAccountButton.setEnabled(true);


//        Intent login = new Intent(getApplicationContext(),LoginActivity.class);
//        startActivity(login);
//        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mCreateAccountButton.setEnabled(true);
    }

}