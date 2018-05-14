package cmpe275eat.takeoutapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class OrderListActivity extends Activity {
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    private ListView orderlist;
    private ListView namelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderlist = (ListView)findViewById(R.id.order_list);
        namelist = (ListView)findViewById(R.id.name_list);

        final List<String> your_array_list1 = new ArrayList<String>();
        final List<String> your_array_list2 = new ArrayList<String>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference("order");

        mDatabaseRference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                    String itemskey = uniqueKeySnapshot.getKey();
                    FirebaseUser user  = auth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    if (uid.equals(itemskey)) {
                        your_array_list1.add("ID:");
                        your_array_list2.add(itemskey);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list1 );
        namelist.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list2 );
        orderlist.setAdapter(arrayAdapter2);

        orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String itemid = ((TextView)view).getText().toString();
                oderdetail(itemid);
            }
        });
    }

    private void oderdetail(String itemid) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order Detail");

        ListView modeList = new ListView(this);
        final ArrayList<String> listData = new ArrayList<>();

        mDatabaseRference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                    String itemskey = uniqueKeySnapshot.getKey();
                    Order o = uniqueKeySnapshot.getValue(Order.class);
                    FirebaseUser user  = auth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    if (uid.equals(o.getUserID())) {
                        listData.add("UesrID: " + o.getUserID());
                        listData.add("Pick Time: " + o.getPickTime());
                        for (Map.Entry<String,Integer> entry : o.getItem().entrySet()) {
                            listData.add("ItemId: " + entry.getKey() + "    Qty: " + entry.getValue());
                        }
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listData);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user  = auth.getInstance().getCurrentUser();
                String uid = user.getUid();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseRference = mFirebaseDatabase.getReference("order");
                mDatabaseRference.child(uid).removeValue();
//
                Toast.makeText(getBaseContext(),"Order Removed!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderListActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
            } });
        final Dialog dialog = builder.create();

        dialog.show();
    }
}