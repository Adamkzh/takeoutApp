package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        auth = FirebaseAuth.getInstance();
        LoginButton();
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

}