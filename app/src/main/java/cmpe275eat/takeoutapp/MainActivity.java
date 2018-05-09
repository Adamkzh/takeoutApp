package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static EditText sig_userName;
    private static EditText sig_passWord;
    private static Button btn_logIn;
    private static Button btn_register;
    private static Button btn_facebook;
    private static Button btn_google;
    private static RadioButton btn_sig_admin;
    private static RadioButton btn_sig_customer;
    private static RadioGroup btnGroup_signIn;
    private FirebaseAuth auth;

    private static Button btn_shortcut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sig_userName = (EditText)findViewById(R.id.signIn_userName);
        sig_passWord = (EditText)findViewById(R.id.signIn_password);

        btn_logIn = (Button)findViewById(R.id.btn_signIn);
        btn_register = (Button)findViewById(R.id.btn_goToRegister);
        btn_facebook = (Button)findViewById(R.id.btn_fbSignIn);
        btn_google = (Button)findViewById(R.id.btn_googleSignIn);
        btnGroup_signIn = (RadioGroup)findViewById(R.id.radioGroup_signIn);
        btn_sig_admin = (RadioButton)findViewById(R.id.rbtn_admin);
        btn_sig_customer = (RadioButton)findViewById(R.id.rbtn_cus);

        ///test
        btn_shortcut = (Button)findViewById(R.id.btn_shortcut);

        auth = FirebaseAuth.getInstance();
        LoginButton();
//        GoogleSignIn();
        GoRegisterButton();
    }

    protected void LoginButton(){
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = sig_userName.getText().toString();
                final String password = sig_passWord.getText().toString();

                auth = FirebaseAuth.getInstance();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 4) {
                                    Toast.makeText(getApplicationContext(), "Password too short!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Wrong User Name or Password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
                            }
                        }
                    });
            }
        });
    }

//    protected void GoogleSignIn(){
//        btn_google.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Configure Google Sign In, reference: https://firebase.google.com/docs/auth/android/google-signin
//                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(getString(R.string.default_web_client_id))
//                        .requestEmail()
//                        .build();
//            }
//        });
//    }
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    protected void GoRegisterButton(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to register page
                Intent registerIntent = new Intent(MainActivity.this,
                        RegisterActivity.class);
                startActivity(registerIntent);

            }
        });
    }

    public void toOrderView(View view)
    {
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        startActivity(intent);
    }

}
