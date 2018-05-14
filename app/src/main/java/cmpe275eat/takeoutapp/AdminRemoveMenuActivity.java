package cmpe275eat.takeoutapp;

/**
 * Created by yichinhsiao on 5/10/18.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminRemoveMenuActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;
    private ArrayList<Menu> menu_list;
    private AdminRemoveMenuAdapter menu_adapter;
    private ListView listView;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_remove_menu);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference();

        menu_list = new ArrayList<Menu>();

        mDatabaseRference.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
                for(int i = 1; i <= size; i++) {
                    mDatabaseRference.child("menu").child(String.valueOf(i))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = (String) dataSnapshot.child("name").getValue();
                                    Boolean enabled = (Boolean) dataSnapshot.child("enabled").getValue();
                                    String picture = (String) dataSnapshot.child("picture").getValue();
                                    Menu menu = new Menu();
                                    menu.setName(name);
                                    menu.setEnabled(enabled);
//                                    menu.setPicture(picture);
                                    menu_list.add(menu);

                                    if(menu_list.size() == size) {
                                        menu_adapter = new AdminRemoveMenuAdapter(menu_list, getApplicationContext());
                                        listView = (ListView) findViewById(R.id.admin_remove_menu_list);
                                        listView.setAdapter(menu_adapter);
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
