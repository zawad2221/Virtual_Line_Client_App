package com.dhakanewsclub.virtualline.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.registration.RegistrationActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText mPhoneNumberEditText,mPasswordEditText;
    private TextView mSingupTextView;
    private Button mLoginButton;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mPasswordEditText=findViewById(R.id.input_password);
        mPhoneNumberEditText=findViewById(R.id.input_phoneNumber);
        mLoginButton = findViewById(R.id.button_login);
        mSingupTextView = findViewById(R.id.link_signup);
        mLoginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);


        loginOnClick();
        signupOnClick();

    }

    private void signupOnClick(){
        mSingupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(getApplicationContext(), RegistrationActivity.class);

                startActivity(signup);
                finish();
            }
        });
    }

    private void loginOnClick(){
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public boolean validateInput(){
        boolean valid = true;

        String phoneNumber = mPhoneNumberEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (phoneNumber.isEmpty() || phoneNumber.length()<11) {
            mPhoneNumberEditText.setError("enter a valid phone number address");
            valid = false;
        } else {
            mPhoneNumberEditText.setError(null);
        }

        if (password.isEmpty() || password.length()<7) {
            mPasswordEditText.setError("password length must be grater than 6 alphanumeric characters");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        return valid;
    }



    private void login(){
        if(!validateInput()){
            Toast.makeText(this,"Invalid input",Toast.LENGTH_SHORT).show();

            return;
        }
        //mLoginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.Theme_AppCompat_Light_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
        String phoneNumber=mPhoneNumberEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        //login code
        mLoginViewModel.login(LoginActivity.this,phoneNumber,password);


//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
////                        // On complete call either onSignupSuccess or onSignupFailed
////                        // depending on success
////                        onSignupSuccess();
////                        // onSignupFailed();
//                        //progressDialog.dismiss();
//                    }
//                }, 9000);

    }
}