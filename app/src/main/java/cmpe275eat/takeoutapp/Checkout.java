package cmpe275eat.takeoutapp;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;

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
import java.util.Calendar;


public class Checkout extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ListView itemlist;
    private ListView pricelist;
    private ListView timelist;
    private ListView qtyList;

    static final int TIME_DIALOG_ID = 1111;
    private TextView view;
    public Button btnClick;
    private int hr;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String[] list1 = intent.getStringArrayExtra("itemlist");
        String[] list2 = intent.getStringArrayExtra("pricelist");
        int[] list3 = intent.getIntArrayExtra("timelist");
//        String[] list4 = intent.getStringArrayExtra("pricelist");

        itemlist = (ListView)findViewById(R.id.list1);
        pricelist = (ListView)findViewById(R.id.list2);
        timelist = (ListView)findViewById(R.id.list3);

        List<String> your_array_list1 = new ArrayList<String>();
        for(String s: list1) {
            your_array_list1.add(s);
        }

        List<String> your_array_list2 = new ArrayList<String>();
        for(String s: list2) {
            your_array_list2.add(s);
        }

        List<Integer> your_array_list3 = new ArrayList<Integer>();
        for(int s: list3) {
            your_array_list3.add(s);
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

        ArrayAdapter<Integer> arrayAdapter3 = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list3 );

        timelist.setAdapter(arrayAdapter3);

        final Button placeOrderButton =findViewById(R.id.placeOrder);
        placeOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v == placeOrderButton){
                    placeOrder();
                    }
                }
            });

        view = (TextView) findViewById(R.id.output);
        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        updateTime(hr, min);
        addButtonClickListener();

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

    public void addButtonClickListener() {
        btnClick = (Button) findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createdDialog(0).show();
            }
        });
    }
    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timePickerListener, hr, min, false);
        }
//        return null;
        return new TimePickerDialog(this, timePickerListener, hr, min, false);
    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
// TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min);
        }
    };
    private static String utilTime(int value) {
        if (value < 10) return "0" + String.valueOf(value); else return String.valueOf(value); } private void updateTime(int hours, int mins) { String timeSet = ""; if (hours > 12) {
        hours -= 12;
        timeSet = "PM";
    } else if (hours == 0) {
        hours += 12;
        timeSet = "AM";
    } else if (hours == 12)
        timeSet = "PM";
    else
        timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        view.setText(aTime);
    }

}
