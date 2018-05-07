package cmpe275eat.takeoutapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends Activity{
    private static EditText reg_userName;
    private static EditText reg_password;
    private static EditText reg_email;
    private static Button reg_register;
    private static Button reg_goSignIn;
    private static RadioButton btn_reg_admin;
    private static RadioButton btn_reg_customer;
    private static RadioGroup btnGroup_register;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_userName = (EditText)findViewById(R.id.register_userName);
        reg_password = (EditText)findViewById(R.id.register_password);
        reg_email = (EditText)findViewById(R.id.register_email);

        reg_register = (Button) findViewById(R.id.btn_register);
        reg_goSignIn = (Button)findViewById(R.id.btn_goToSignIn);

        btn_reg_admin = (RadioButton) findViewById(R.id.rbtn_admin);
        btn_reg_customer = (RadioButton)findViewById(R.id.rbtn_cus);
        btnGroup_register = (RadioGroup)findViewById(R.id.radioGroup_register);

        auth = FirebaseAuth.getInstance();

        RegisterButton();
        GoSignInButton();

    }

    protected void RegisterButton(){
        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = reg_userName.getText().toString().trim();
                String password = reg_password.getText().toString().trim();
                String email = reg_email.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Please enter user name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 4) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 4 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // send welcome email

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
            }
        });
    }

    protected void GoSignInButton(){
        reg_goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to sign in page
                Intent signInIntent = new Intent(RegisterActivity.this,
                        MainActivity.class);
                startActivity(signInIntent);
            }
        });
    }

}
