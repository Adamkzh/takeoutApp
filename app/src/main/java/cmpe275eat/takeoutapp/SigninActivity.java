package cmpe275eat.takeoutapp;

//import com.firebase.client.DataSnapshot;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
//import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.android.gms.auth.api.Auth;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.util.Arrays;

import static android.renderscript.RenderScript.ContextType.PROFILE;

public class SigninActivity extends AppCompatActivity {

    private EditText sig_userName;
    private EditText sig_passWord;
//    private Button btn_facebook;
    private RadioButton btn_sig_admin;
    private RadioButton btn_sig_customer;
    private RadioGroup btnGroup_signIn;
    private DatabaseReference mDatabase;

    private Button btn_ru;
    private Button btn_toAdmin;

    LoginButton btn_facebook;
    CallbackManager mCallbackManager;
    private static final String EMAIL = "email";
    private static final String PROFILE = "public_profile";

    SignInButton btn_google;
//    private final static int RC_SIGN_IN = 2;
    private static final int RC_SIGN_IN = 9001;

    FirebaseAuth mAuth;
//    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
//    FirebaseAuth.AuthStateListener mAuthListener;

    private Button btn_logIn;
    private Button btn_register;

//    @Override
//    protected void onStart() {
//        super.onStart();
////        mAuth.addAuthStateListener(mAuthListener);
//
//    }


    // onCreate function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initButton();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseApp.initializeApp(this);

        googleSignIn();
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        facebookSignIn();
        LoginButton();
        GoRegisterButton();
    }

    private void initButton() {
        sig_userName = (EditText)findViewById(R.id.signIn_userName);
        sig_passWord = (EditText)findViewById(R.id.signIn_password);

        btn_logIn = (Button)findViewById(R.id.btn_signIn);
        btn_register = (Button)findViewById(R.id.btn_goToRegister);
        btn_facebook = (LoginButton)findViewById(R.id.btn_fbSignIn);
        mCallbackManager = CallbackManager.Factory.create();
        btn_facebook.setReadPermissions(Arrays.asList(EMAIL));

//        btnGroup_signIn = (RadioGroup)findViewById(R.id.radioGroup_signIn);
        btn_sig_admin = (RadioButton)findViewById(R.id.rbtn_admin);
        btn_sig_customer = (RadioButton)findViewById(R.id.rbtn_cus);

        btn_google = findViewById(R.id.btn_googleSignIn);

//        btn_ru = (Button)findViewById(R.id.btn_ru);
//        btn_toAdmin = (Button)findViewById(R.id.btn_toAdmin);
    }

    private void facebookSignIn() {
        btn_facebook.setReadPermissions(EMAIL, PROFILE);
        btn_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebookLogIn", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebookLogIn", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebookLogIn", "facebook:onError", error);
                // ...
            }
        });
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            // Pass the activity result back to the Facebook SDK
//            mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookLog", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FacebookLog", "signInWithCredential:success");
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String currentEmail = "";
                                for (UserInfo userInfo : user.getProviderData()) {
                                    Log.i(userInfo.getUid(),userInfo.getProviderId()+" "+
                                            userInfo.getEmail()+" "+userInfo.isEmailVerified() );
                                    currentEmail = userInfo.getEmail();
                                }
                                User newUser = new User(user.getUid(), currentEmail, user.getUid(),"Customer");
                                updateUser(newUser);
                                sendWelcomeEmail(currentEmail);
                                Intent goCustomerActivity = new Intent(SigninActivity.this, MainMenuActivity.class);
                                startActivity(goCustomerActivity);
                                Toast.makeText(SigninActivity.this, "Facebook log in Success! " + user.getEmail(), Toast.LENGTH_LONG).show();
                            }
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FacebookLog", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GoogleSignIn", "signInWithCredential:success");
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                User newUser = new User(user.getUid(),user.getEmail(), user.getUid(),"Customer");
                                updateUser(newUser);
                                sendWelcomeEmail(user.getEmail());
                                Intent goCustomerActivity = new Intent(SigninActivity.this, MainMenuActivity.class);
                                startActivity(goCustomerActivity);
                                Toast.makeText(getApplicationContext(), "Success! Welcome New User, " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                updateUI();
                                Toast.makeText(getApplicationContext(), "Success! Welcome back, " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleSignIn", "Google sign in failed", e);
                // ...
            }
        }

    }

    //    go to customer index page
    private void updateUI() {
        Intent goCustomerActivity = new Intent(SigninActivity.this, MainMenuActivity.class);
        startActivity(goCustomerActivity);
        finish();
    }

    protected void LoginButton(){
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = sig_userName.getText().toString();
                final String password = sig_passWord.getText().toString();
                //        email empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //        password empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //       radio button empty
                if (checkAnswer() == null) {
                    Toast.makeText(getApplicationContext(), "Please choose register as Admin or Customer", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("logIn", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
//                                Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();

                                //                 verify user is admin or customer
                                String user_email = mAuth.getCurrentUser().getEmail();
                                mDatabase.child("users").orderByChild("email").equalTo(user_email)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                                    String s = data.getKey();
                                                    mDatabase.child("users").child(s).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String t = (String)dataSnapshot.getValue();
//                                                            Toast.makeText(getApplicationContext(),t,Toast.LENGTH_LONG).show();
                                                            if(t.equals("Admin")) {
                                                                Toast.makeText(getApplicationContext(),"Success! Welcome back, "+ mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(SigninActivity.this, AdminIndexActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(), "Success! Welcome back, "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                                                updateUI();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("logIn", "signInWithEmail:failure", task.getException());
                                Toast.makeText(SigninActivity.this, "Wrong Email or Password or User Type!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }

    private String checkAnswer() {
        if (btn_sig_admin.isChecked()) {
            return btn_sig_admin.getText().toString();
        }
        if (btn_sig_customer.isChecked()) {
            return btn_sig_customer.getText().toString();
        }
        return null;
    }

    private void updateUser(User newUser) {
        mDatabase.child("users").child(newUser.getUid()).child("email").setValue(newUser.getEmail());
        mDatabase.child("users").child(newUser.getUid()).child("password").setValue(newUser.getPassword());
        mDatabase.child("users").child(newUser.getUid()).child("type").setValue(newUser.getType());
//        Toast.makeText(SigninActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
    }

    private void sendWelcomeEmail(final String newEmail) {
        final GMailSender sender = new GMailSender("noraliu1206@gmail.com", "cmpe2772018");
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... arg) {
                try {
                    sender.sendMail("Welcome to CMPE 277 Takeout App - 2018 Spring in SJSU",
                            "Hi, Weilcome to use TakeoutApp - CMPE 277 Group 6",
                            "noraliu1206@gmail.com",
                            newEmail);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }

    protected void GoRegisterButton(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to register page
                Intent registerIntent = new Intent(SigninActivity.this, RegisterActivity.class);
                startActivity(registerIntent);

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mAuth.getCurrentUser() != null) {
        //            check current user type is Admin or Customer
            String user_email = mAuth.getCurrentUser().getEmail();
            mDatabase.child("users").orderByChild("email").equalTo(user_email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                String s = data.getKey();
                                mDatabase.child("users").child(s).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String t = (String)dataSnapshot.getValue();
//                                                            Toast.makeText(getApplicationContext(),t,Toast.LENGTH_LONG).show();
                                        if(t.equals("Admin")) {
                                            Toast.makeText(getApplicationContext(),"Success! Welcome back, "+ mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SigninActivity.this, AdminIndexActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Success! Welcome back, "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                            updateUI();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}

