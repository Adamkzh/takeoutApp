package cmpe275eat.takeoutapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.text.TextUtils;

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

        //Get Firebase auth instance
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

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(reg_userName.getText().toString().equals("admin")){  // user name exist
                    Toast.makeText(RegisterActivity.this,"User name already exist, please enter another user name Or sign in with this user name.", Toast.LENGTH_LONG).show();
                }
                else{ // save information into database and go the index page depending on admin or customer
                    if(btnGroup_register.getCheckedRadioButtonId() == R.id.rbtn_admin){
                        // sign in as admin, go to admin index
                    }
                    if(btnGroup_register.getCheckedRadioButtonId() == R.id.rbtn_cus){
                        // sign in as customer, go to customer index
                    }
                    // send welcome email
                }
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
