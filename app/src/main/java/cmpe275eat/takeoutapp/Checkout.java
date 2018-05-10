package cmpe275eat.takeoutapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.*;
import com.firebase.client.Firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;


public class Checkout extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ListView itemlist;
    private ListView pricelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String[] list1 = intent.getStringArrayExtra("itemlist");
        String[] list2 = intent.getStringArrayExtra("pricelist");

        itemlist = (ListView)findViewById(R.id.list1);
        pricelist = (ListView)findViewById(R.id.list2);

        List<String> your_array_list1 = new ArrayList<String>();
        for(String s: list1) {
            your_array_list1.add(s);
        }

        List<String> your_array_list2 = new ArrayList<String>();
        for(String s: list2) {
            your_array_list2.add(s);
        }

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list1 );

        itemlist.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list2 );

        pricelist.setAdapter(arrayAdapter2);

        final Button placeOrderButton =findViewById(R.id.placeOrder);
        placeOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v == placeOrderButton){
                    placeOrder();
                    }
                }
            });

    }
    public void placeOrder(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = "userId";
        String pickTime = "15:50";
        String orderStartTime = "15:30";

        JSONArray order = new JSONArray();
        JSONObject item = new JSONObject();
        try {
            item.put("name","curry");
            item.put("category","main course");
            item.put("calories","300");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        order.put(item);
        System.out.print(order);
        Order inputData = new Order(uid,pickTime,orderStartTime,order);
        databaseReference.push().setValue(inputData);

    }

    public void checkOrder(){



    }

}
