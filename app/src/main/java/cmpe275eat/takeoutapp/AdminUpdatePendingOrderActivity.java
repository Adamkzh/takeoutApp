package cmpe275eat.takeoutapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by yichinhsiao on 5/14/18.
 */

public class AdminUpdatePendingOrderActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;
    private ArrayList<Order> order_list;
    private AdminUpdatePendingOrderAdapter pending_order_adapter;
    private ListView listView;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_pending_order);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference();

        order_list = new ArrayList<Order>();

        mDatabaseRference.child("my_order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
                for(int i = 1; i <= size; i++) {
                    mDatabaseRference.child("my_order").child(String.valueOf(i))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String status = (String) dataSnapshot.child("status").getValue();
                                    if(!status.equals("Picked") && !status.equals("Abandoned")) {
                                        Order order = new Order();
                                        order.setOrderId((String) dataSnapshot.child("orderId").getValue());
                                        order.setUserId((String) dataSnapshot.child("userId").getValue());
                                        order.setOrderTime((String) dataSnapshot.child("orderTime").getValue());
                                        order.setStartTime((String) dataSnapshot.child("startTime").getValue());
                                        order.setReadyTime((String) dataSnapshot.child("readyTime").getValue());
                                        order.setPickupTime((String) dataSnapshot.child("pickupTime").getValue());
                                        order.setStatus((String) dataSnapshot.child("status").getValue());
                                        order.setCustomerEmail((String) dataSnapshot.child("customerEmail").getValue());
                                        Number total_price_long = (Number) dataSnapshot.child("totalPrice").getValue();
                                        Double total_price = total_price_long.doubleValue();
                                        order.setTotalPrice(total_price);
                                        ArrayList<OrderItem> item_list = new ArrayList<OrderItem>();
                                        int number = (int) dataSnapshot.child("items").getChildrenCount();
                                        for(int j = 1; j <= number; j++) {
                                            OrderItem item = new OrderItem();
                                            Number id_long = (Number) dataSnapshot.child("items").child(String.valueOf(j)).child("id").getValue();
                                            int id = id_long.intValue();
                                            item.setId(id);
                                            item.setName((String) dataSnapshot.child("items").child(String.valueOf(j)).child("name").getValue());
                                            Long quantity_long = (Long) dataSnapshot.child("items").child(String.valueOf(j)).child("quantity").getValue();
                                            int quantity = quantity_long.intValue();
                                            item.setQuantity(quantity);
                                            Number unit_price_long = (Number) dataSnapshot.child("items").child(String.valueOf(j)).child("unitPrice").getValue();
                                            Double unit_price = unit_price_long.doubleValue();
                                            item.setUnitPrice(unit_price);
                                            item_list.add(item);
                                        }
                                        order.setItems(item_list);
                                        order_list.add(order);
                                        pending_order_adapter = new AdminUpdatePendingOrderAdapter(order_list, AdminUpdatePendingOrderActivity.this);
                                        listView = (ListView) findViewById(R.id.admin_update_pending_order_list);
                                        listView.setAdapter(pending_order_adapter);
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