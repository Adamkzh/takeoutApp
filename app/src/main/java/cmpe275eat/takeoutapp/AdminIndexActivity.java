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
                    }
                });
                dialog_builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Firebase.setAndroidContext(AdminIndexActivity.this);
                        FirebaseApp.initializeApp(AdminIndexActivity.this);
                        auth = FirebaseAuth.getInstance();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabaseRference = mFirebaseDatabase.getReference();
                        mDatabaseRference.child("order").removeValue();
                        mDatabaseRference.child("cooker").removeValue();
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
                AlertDialog.Builder dialog_builder = new AlertDialog.Builder(AdminIndexActivity.this);
                dialog_builder.setMessage("Are you sure to log out?");
                dialog_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent intent = new Intent(AdminIndexActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog dialog = dialog_builder.create();
                dialog.show();
            }
        });
    }
}