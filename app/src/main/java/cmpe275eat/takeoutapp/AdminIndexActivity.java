package cmpe275eat.takeoutapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by yichinhsiao on 5/7/18.
 */

public class AdminIndexActivity extends AppCompatActivity {

    private Button pending_order;
    private Button add_menu;
    private Button remove_menu;
    private Button status_report;
    private Button popularity_report;
    private Button reset_order;
    private Button log_out;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_index);

        pending_order = (Button) findViewById(R.id.admin_index_pending_order);
        add_menu = (Button) findViewById(R.id.admin_index_add_menu);
        remove_menu = (Button) findViewById(R.id.admin_index_remove_menu);
        status_report = (Button) findViewById(R.id.admin_index_status_report);
        popularity_report = (Button) findViewById(R.id.admin_index_popularity_report);
        reset_order = (Button) findViewById(R.id.admin_index_reset_order);
        log_out = (Button) findViewById(R.id.admin_index_log_out);

        pending_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* test sending an email
                final GMailSender sender = new GMailSender("garyhsiao1219@gmail.com",
                        "yichin0091");
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    public Void doInBackground(Void... arg) {
                        try {
                            sender.sendMail("Test Mail",
                                    "Hi, this is a test mail from TakeoutApp",
                                    "garyhsiao1219@gmail.com",
                                    "garyhsiao1219@gmail.com");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                        return null;
                    }
                }.execute();
                */
            }
        });

        add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminIndexActivity.this, AdminAddMenuActivity.class);
                startActivity(intent);
            }
        });

        remove_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminIndexActivity.this, AdminRemoveMenuActivity.class);
                startActivity(intent);
            }
        });

        status_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        popularity_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        reset_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog_builder = new AlertDialog.Builder(AdminIndexActivity.this);
                dialog_builder.setMessage("Are you sure to rest all of orders?");
                dialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog_builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Firebase.setAndroidContext(getApplicationContext());
                        FirebaseApp.initializeApp(getApplicationContext());
                        auth = FirebaseAuth.getInstance();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabaseRference = mFirebaseDatabase.getReference();
                        // later change key "my_order" to real "order"
                        mDatabaseRference.child("my_order").removeValue();
                        Toast.makeText(AdminIndexActivity.this, "Order reset", Toast.LENGTH_LONG).show();
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = dialog_builder.create();
                dialog.show();
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminIndexActivity.this, LogoutActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

                /*
                Order:
                orderId
                userId
                orderTime
                start-time
                ready-time
                Pickup-time
                status: queued, being-prepared, fulfilled, abandoned, picked
                customerEmail
                menuId
                    - quantity
                    - unitPrice
                totalPrice


                Firebase.setAndroidContext(getApplicationContext());
                FirebaseApp.initializeApp(getApplicationContext());
                auth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseRference = mFirebaseDatabase.getReference();

                mDatabaseRference.child("my_order").child("1").
                        child("orderId").setValue(1);
                mDatabaseRference.child("my_order").child("1").
                        child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                mDatabaseRference.child("my_order").child("1").
                        child("orderTime").setValue("May 13, 2018 08:30:00 PM");
                mDatabaseRference.child("my_order").child("1").
                        child("StartTime").setValue("May 15, 2018 11:00:00 AM");
                mDatabaseRference.child("my_order").child("1").
                        child("Readytime").setValue("May 15, 2018 11:24:00 AM");
                mDatabaseRference.child("my_order").child("1").
                        child("PickUpTime").setValue("May 15, 2018 00:00:00 PM");
                mDatabaseRference.child("my_order").child("1").
                        child("status").setValue("queued");
                mDatabaseRference.child("my_order").child("1").
                        child("customerEmail").setValue("garyhsiao1219@gmail.com");
                mDatabaseRference.child("my_order").child("1").
                        child("items").child("1").child("name").setValue("steak");
                mDatabaseRference.child("my_order").child("1").
                        child("items").child("1").child("quantity").setValue(2);
                mDatabaseRference.child("my_order").child("1").
                        child("items").child("1").child("unitPrice").setValue(10.5);
                mDatabaseRference.child("my_order").child("1").
                        child("items").child("2").child("name").setValue("milk tea");
                mDatabaseRference.child("my_order").child("1").
                        child("items").child("2").child("quantity").setValue(2);
                mDatabaseRference.child("my_order").child("1").
                        child("items").child("2").child("unitPrice").setValue(4);
                mDatabaseRference.child("my_order").child("1").
                        child("totalPrice").setValue(29);

                mDatabaseRference.child("my_order").child("2").
                        child("orderId").setValue(1);
                mDatabaseRference.child("my_order").child("2").
                        child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                mDatabaseRference.child("my_order").child("2").
                        child("orderTime").setValue("May 13, 2018 08:45:00 PM");
                mDatabaseRference.child("my_order").child("2").
                        child("StartTime").setValue("May 14, 2018 09:00:00 AM");
                mDatabaseRference.child("my_order").child("2").
                        child("Readytime").setValue("May 14, 2018 09:24:00 AM");
                mDatabaseRference.child("my_order").child("2").
                        child("PickUpTime").setValue("May 14, 2018 10:00:00 AM");
                mDatabaseRference.child("my_order").child("2").
                        child("status").setValue("picked");
                mDatabaseRference.child("my_order").child("2").
                        child("customerEmail").setValue("garyhsiao1219@gmail.com");
                mDatabaseRference.child("my_order").child("2").
                        child("items").child("1").child("name").setValue("steak");
                mDatabaseRference.child("my_order").child("2").
                        child("items").child("1").child("quantity").setValue(2);
                mDatabaseRference.child("my_order").child("2").
                        child("items").child("1").child("unitPrice").setValue(10.5);
                mDatabaseRference.child("my_order").child("2").
                        child("items").child("2").child("name").setValue("milk tea");
                mDatabaseRference.child("my_order").child("2").
                        child("items").child("2").child("quantity").setValue(2);
                mDatabaseRference.child("my_order").child("2").
                        child("items").child("2").child("unitPrice").setValue(4);
                mDatabaseRference.child("my_order").child("2").
                        child("totalPrice").setValue(29);
                */

                /* test date
                String stringdate = "May 13, 2018 00:00:01 PM";
                DateFormat format = DateFormat.getDateTimeInstance();
                try {
                    Date date = format.parse(stringdate);
                    Toast.makeText(AdminIndexActivity.this, date.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                }
                */