package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        LoginButton();
        GoRegisterButton();
    }

    protected void LoginButton(){
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verify the username and password in database
                if(sig_userName.getText().toString().equals("user") & sig_passWord.getText().toString().equals("pass")){
                    Toast.makeText(MainActivity.this,"Username and password is correct", Toast.LENGTH_LONG).show();
                    if(btnGroup_signIn.getCheckedRadioButtonId() == R.id.rbtn_admin){
                        // sign in as admin, go to admin index
                    }
                    if(btnGroup_signIn.getCheckedRadioButtonId() == R.id.rbtn_cus){
                        // sign in as customer, go to customer index
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Invalid user name or password, please try again", Toast.LENGTH_LONG).show();
                }
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
