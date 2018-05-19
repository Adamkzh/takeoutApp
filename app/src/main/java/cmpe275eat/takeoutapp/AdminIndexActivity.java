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
                Intent intent = new Intent(AdminIndexActivity.this, AdminUpdatePendingOrderActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(AdminIndexActivity.this, AdminStatusReportActivity.class);
                startActivity(intent);
            }
        });

        popularity_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminIndexActivity.this, AdminPopularityReportActivity.class);
                startActivity(intent);
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

                        Firebase.setAndroidContext(getApplicationContext());
                        FirebaseApp.initializeApp(getApplicationContext());
                        auth = FirebaseAuth.getInstance();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabaseRference = mFirebaseDatabase.getReference();

                        //05-13-2018
                        mDatabaseRference.child("my_order").child("1").
                                child("orderId").setValue("1");
                        mDatabaseRference.child("my_order").child("1").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("1").
                                child("orderTime").setValue("05-13-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("1").
                                child("startTime").setValue("05-15-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("1").
                                child("readyTime").setValue("05-15-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("1").
                                child("pickupTime").setValue("05-15-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("1").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("1").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("1").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("1").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("2").
                                child("orderId").setValue("2");
                        mDatabaseRference.child("my_order").child("2").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("2").
                                child("orderTime").setValue("05-13-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("2").
                                child("startTime").setValue("05-14-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("2").
                                child("readyTime").setValue("05-14-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("2").
                                child("pickupTime").setValue("05-14-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("2").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("2").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("2").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("2").
                                child("totalPrice").setValue(10.0);

                        //05-14-2018
                        mDatabaseRference.child("my_order").child("3").
                                child("orderId").setValue("3");
                        mDatabaseRference.child("my_order").child("3").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("3").
                                child("orderTime").setValue("05-14-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("3").
                                child("startTime").setValue("05-16-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("3").
                                child("readyTime").setValue("05-16-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("3").
                                child("pickupTime").setValue("05-16-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("3").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("3").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("3").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("3").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("4").
                                child("orderId").setValue("4");
                        mDatabaseRference.child("my_order").child("4").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("4").
                                child("orderTime").setValue("05-14-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("4").
                                child("startTime").setValue("05-15-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("4").
                                child("readyTime").setValue("05-15-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("4").
                                child("pickupTime").setValue("05-15-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("4").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("4").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("4").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("4").
                                child("totalPrice").setValue(10.0);

                        //05-15-2018
                        mDatabaseRference.child("my_order").child("5").
                                child("orderId").setValue("5");
                        mDatabaseRference.child("my_order").child("5").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("5").
                                child("orderTime").setValue("05-15-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("5").
                                child("startTime").setValue("05-17-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("5").
                                child("readyTime").setValue("05-17-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("5").
                                child("pickupTime").setValue("05-17-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("5").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("5").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("5").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("5").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("6").
                                child("orderId").setValue("6");
                        mDatabaseRference.child("my_order").child("6").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("6").
                                child("orderTime").setValue("05-15-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("6").
                                child("startTime").setValue("05-16-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("6").
                                child("readyTime").setValue("05-16-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("6").
                                child("pickupTime").setValue("05-16-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("6").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("6").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("6").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("6").
                                child("totalPrice").setValue(10.0);

                        //05-16-2018
                        mDatabaseRference.child("my_order").child("7").
                                child("orderId").setValue("7");
                        mDatabaseRference.child("my_order").child("7").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("7").
                                child("orderTime").setValue("05-16-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("7").
                                child("startTime").setValue("05-18-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("7").
                                child("readyTime").setValue("05-18-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("7").
                                child("pickupTime").setValue("05-18-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("7").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("7").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("7").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("7").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("8").
                                child("orderId").setValue("8");
                        mDatabaseRference.child("my_order").child("8").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("8").
                                child("orderTime").setValue("05-16-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("8").
                                child("startTime").setValue("05-17-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("8").
                                child("readyTime").setValue("05-17-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("8").
                                child("pickupTime").setValue("05-17-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("8").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("8").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("8").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("8").
                                child("totalPrice").setValue(10.0);

                        //05-17-2018
                        mDatabaseRference.child("my_order").child("9").
                                child("orderId").setValue("9");
                        mDatabaseRference.child("my_order").child("9").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("9").
                                child("orderTime").setValue("05-17-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("9").
                                child("startTime").setValue("05-19-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("9").
                                child("readyTime").setValue("05-19-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("9").
                                child("pickupTime").setValue("05-19-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("9").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("9").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("9").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("9").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("10").
                                child("orderId").setValue("10");
                        mDatabaseRference.child("my_order").child("10").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("10").
                                child("orderTime").setValue("05-17-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("10").
                                child("startTime").setValue("05-18-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("10").
                                child("readyTime").setValue("05-18-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("10").
                                child("pickupTime").setValue("05-18-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("10").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("10").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("10").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("10").
                                child("totalPrice").setValue(10.0);

                        //05-18-2018
                        mDatabaseRference.child("my_order").child("11").
                                child("orderId").setValue("11");
                        mDatabaseRference.child("my_order").child("11").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("11").
                                child("orderTime").setValue("05-18-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("11").
                                child("startTime").setValue("05-20-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("11").
                                child("readyTime").setValue("05-20-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("11").
                                child("pickupTime").setValue("05-20-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("11").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("11").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("11").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("11").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("12").
                                child("orderId").setValue("12");
                        mDatabaseRference.child("my_order").child("12").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("12").
                                child("orderTime").setValue("05-18-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("12").
                                child("startTime").setValue("05-19-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("12").
                                child("readyTime").setValue("05-19-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("12").
                                child("pickupTime").setValue("05-19-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("12").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("12").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("12").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("12").
                                child("totalPrice").setValue(10.0);

                        //05-19-2018
                        mDatabaseRference.child("my_order").child("13").
                                child("orderId").setValue("13");
                        mDatabaseRference.child("my_order").child("13").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("13").
                                child("orderTime").setValue("05-19-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("13").
                                child("startTime").setValue("05-21-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("13").
                                child("readyTime").setValue("05-21-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("13").
                                child("pickupTime").setValue("05-21-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("13").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("13").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("13").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("13").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("14").
                                child("orderId").setValue("14");
                        mDatabaseRference.child("my_order").child("14").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("14").
                                child("orderTime").setValue("05-19-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("14").
                                child("startTime").setValue("05-20-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("14").
                                child("readyTime").setValue("05-20-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("14").
                                child("pickupTime").setValue("05-20-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("14").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("14").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("1").child("id").setValue(5);
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("1").child("name").setValue("Pudding");
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("1").child("unitPrice").setValue(3.5);
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("2").child("id").setValue(8);
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("2").child("name").setValue("Caesar Salad");
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("2").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("14").
                                child("items").child("2").child("unitPrice").setValue(6.5);
                        mDatabaseRference.child("my_order").child("14").
                                child("totalPrice").setValue(10.0);

                        //05-20-2018
                        mDatabaseRference.child("my_order").child("15").
                                child("orderId").setValue("15");
                        mDatabaseRference.child("my_order").child("15").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("15").
                                child("orderTime").setValue("05-20-2018 20:30:00");
                        mDatabaseRference.child("my_order").child("15").
                                child("startTime").setValue("05-22-2018 11:00:00");
                        mDatabaseRference.child("my_order").child("15").
                                child("readyTime").setValue("05-22-2018 11:24:00");
                        mDatabaseRference.child("my_order").child("15").
                                child("pickupTime").setValue("05-22-2018 12:00:00");
                        mDatabaseRference.child("my_order").child("15").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("15").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("1").child("id").setValue(1);
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("1").child("name").setValue("Steak");
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("1").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("1").child("unitPrice").setValue(14.5);
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("2").child("id").setValue(3);
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("2").child("name").setValue("Boba Milk Tea");
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("15").
                                child("items").child("2").child("unitPrice").setValue(4.25);
                        mDatabaseRference.child("my_order").child("15").
                                child("totalPrice").setValue(37.5);

                        mDatabaseRference.child("my_order").child("16").
                                child("orderId").setValue("16");
                        mDatabaseRference.child("my_order").child("16").
                                child("userId").setValue("C7a9pfh0vMXBqYdgAYz050p9N9Q2");
                        mDatabaseRference.child("my_order").child("16").
                                child("orderTime").setValue("05-20-2018 20:45:00");
                        mDatabaseRference.child("my_order").child("16").
                                child("startTime").setValue("05-21-2018 09:00:00");
                        mDatabaseRference.child("my_order").child("16").
                                child("readyTime").setValue("05-21-2018 09:24:00");
                        mDatabaseRference.child("my_order").child("16").
                                child("pickupTime").setValue("05-21-2018 10:00:00");
                        mDatabaseRference.child("my_order").child("16").
                                child("status").setValue("Queued");
                        mDatabaseRference.child("my_order").child("16").
                                child("customerEmail").setValue("garyhsiao1219@gmail.com");
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("1").child("id").setValue(2);
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("1").child("name").setValue("Hawaiian Pizza");
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("1").child("quantity").setValue(1);
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("1").child("unitPrice").setValue(13);
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("2").child("id").setValue(4);
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("2").child("name").setValue("Orange Juice");
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("2").child("quantity").setValue(2);
                        mDatabaseRference.child("my_order").child("16").
                                child("items").child("2").child("unitPrice").setValue(5);
                        mDatabaseRference.child("my_order").child("16").
                                child("totalPrice").setValue(23);

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
                        mDatabaseRference.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot menu: dataSnapshot.getChildren()) {
                                    menu.getRef().child("popularity").setValue(0);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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