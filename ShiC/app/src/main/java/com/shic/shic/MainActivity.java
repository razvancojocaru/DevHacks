package com.shic.shic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CallbackManager mCallbackManager;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    /* Define views */
    ImageView mLogo;
    LoginButton loginButton;
    GridLayout layoutCategories;
    ImageButton[] categoryButtons = new ImageButton[6];
    int[] imageCategories = {R.id.clothing, R.id.toys, R.id.food, R.id.furniture, R.id.appliances, R.id.misc};
    String[] categories = {"clothing", "toys", "food", "furniture", "appliances", "misc"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        setContentView(R.layout.activity_main);

        /* Init views */
        mLogo = (ImageView)findViewById(R.id.logo);
        layoutCategories = (GridLayout)findViewById(R.id.layoutCategories);

        for (int i = 0; i < categoryButtons.length; i++) {
            categoryButtons[i] = (ImageButton)findViewById(imageCategories[i]);
            categoryButtons[i].setOnClickListener(new CategoryListener(categories[i]));
        }



        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI(user);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    updateUI(null);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                updateUI(null);
            }
        });

        //assume user not logged in

        //updateUI(null);
    }

    class CategoryListener implements View.OnClickListener {

        String categoryName;

        CategoryListener(String categoryName) {
            this.categoryName = categoryName;
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), NgoList.class);
            i.setAction(categoryName);
            startActivity(i);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        hideProgressDialog();
        //mAuth.signOut();
        //LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    /*
    * Hide Sign in with facebook buttons and logo
    * show everything else and update views
    * */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //TODO set visibility visible on LOGO and FB login button
            //TODO set visibility View.GONE on everything else
            layoutCategories.setVisibility(View.VISIBLE);
            getActionBar().show();
            loginButton.setVisibility(View.GONE);
            mLogo.setVisibility(View.GONE);
        } else {
            //TODO other way around, show everything
            layoutCategories.setVisibility(View.GONE);
            getActionBar().hide();
            loginButton.setVisibility(View.VISIBLE);
            mLogo.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}