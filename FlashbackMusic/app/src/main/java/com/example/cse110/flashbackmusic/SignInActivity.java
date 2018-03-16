package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {

    // A magic number that indicates whether the sign-in error resolution activity has completed
    private static final int REQUEST_CODE = 64993;

    // A magic number that we will send back to main activity if the sign-in succeeds
    private final int LOGIN_SUCCESS_RESULT_CODE = 178900;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = MainActivity.getGoogleSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            handleSuccessfulSignIn();
        } catch (ApiException e) {
            handleFailedSignIn(e);
        }
    }

    private void handleSuccessfulSignIn() {
        setResult(LOGIN_SUCCESS_RESULT_CODE);
        finish();
    }

    private void handleFailedSignIn(ApiException e) {
        // The ApiException status code indicates the detailed failure reason.
        // Please refer to the GoogleSignInStatusCodes class reference for more information.
        Log.e("SIGN IN ACTIVITY", "signInResult:failed code = " + e.getStatusCode());
        finish();
    }
}
