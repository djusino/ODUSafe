package com.example.barna.odusafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by barna on 9/9/2017.
 */



public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    /*private DBHandler dbh; //Define a DBHandler object
    private Profile profile;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Configure the Sign In to request basic profile information (more can be requested here if needed).
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build the Google API Client with the options specified above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // User's information will be cached for future logins until said information expires.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // Once expired, the user will have to login again.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

         /*  // dbh = DBHandler.getInstance(this);
           // dbh.removeAll();

            //profile = dbh.findProfileAccount(acct.getDisplayName());
            //Log.e("Display Profile message", profile.getFirstName());

            boolean isEmpty = dbh.checkForTables();
            if (!isEmpty ) {

                String Fullname = acct.getDisplayName();
                String firstName = acct.getGivenName();
                String lastName = acct.getFamilyName();
                String email = acct.getEmail();

                Log.e("Display Full Name", Fullname);
                Log.e("Display First Name", firstName);
                Log.e("Display Last Name", lastName);
                Log.e("Display Email", email);

                // breaking
               // dbh.addProfile(new Profile(Fullname, firstName, lastName, "", "", email));


            }*/

            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {
        // Start the sign in intent with Google's API (Google Play Services will handle the UI from here).
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Connection has failed and the Google API could not be contacted.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    // The following are dialogues for Google's API to use.
    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    // Redirect the user to correct place depending on if they signed in or failed to sign in.
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Intent enterDashboard = new Intent(this, Dashboard.class);
            startActivity(enterDashboard);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    // Handle any button presses.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}
