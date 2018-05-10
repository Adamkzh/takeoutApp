package cmpe275eat.takeoutapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.*;
import com.firebase.client.Firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Checkout extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
