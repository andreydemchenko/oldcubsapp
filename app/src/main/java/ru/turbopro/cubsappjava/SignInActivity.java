package ru.turbopro.cubsappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginPassword";

    private EditText mLoginField;
    private EditText mPasswordField;
    private Button mSignInBtn;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        mLoginField = (EditText) findViewById(R.id.edLoginSignIn);
        mPasswordField = (EditText) findViewById(R.id.edPasswordSignIn);
        mSignInBtn = (Button) findViewById(R.id.btnSignInSignIn);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            updateUI(user);
        };
        mSignInBtn.setOnClickListener(view -> signIn(mLoginField.getText().toString().trim(), mPasswordField.getText().toString().trim()));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void signIn(String code, String password) {
        Log.d(TAG, "signIn:" + code);
        if (!validateForm()) return;

        showProgressDialog();

        String email = code + "@cubs.com";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        /*AestheticDialog.Builder dialog = new AestheticDialog.Builder(this, DialogStyle.FLASH, DialogType.ERROR);
                        dialog.setTitle("Wrong login or password");
                        dialog.setMessage("Please try again!");
                        dialog.setAnimation(DialogAnimation.CARD);
                        dialog.setOnClickListener(view2 -> dialog.dismiss());
                        dialog.show();*/
                    }
                    hideProgressDialog();
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mLoginField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mLoginField.setError("Required.");
            valid = false;
        } else
            mLoginField.setError(null);

        String password = mPasswordField.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else
            mPasswordField.setError(null);

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SignInUserId", mLoginField.getText().toString().trim());
            editor.apply();
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.putExtra("userId", mLoginField.getText().toString().trim());
            System.out.println("uuuuuuuuuuuuuiiiiiiiiiiiiiiiidddddddd ============== "+user.getUid() + "  code = " + mLoginField.getText());
            startActivity(intent);
            finish();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.progressdialog_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}