package cmpe275eat.takeoutapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;
import com.firebase.client.Firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import cmpe275eat.takeoutapp.cooker.Cooker;
import cmpe275eat.takeoutapp.cooker.Interval;


public class Checkout extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    private ListView itemlist;
    private ListView pricelist;
    private ListView timelist;
    private ListView qtyList;

    static final int TIME_DIALOG_ID = 1111;
    private TextView view;
    private TextView total_amount;
    public Button btnClick;
    public int hr;
    public int min;

    private int pickTime;
    private int foodCookingTime;
    private int startCookingTime;
    private int endCookingTime;

    String[] list1;
    String[] list2;
    int[] list3;
    int[] list4;
    int[] list5;


    Cooker cooker = new Cooker();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference();

        Intent intent = getIntent();
        list1 = intent.getStringArrayExtra("itemlist");
        list2 = intent.getStringArrayExtra("pricelist");
        list3 = intent.getIntArrayExtra("qtylist");
        list4 = intent.getIntArrayExtra("idlist");
        list5 = intent.getIntArrayExtra("timelist");
        int total = intent.getIntExtra("totalqty", 0);
        double allamount = intent.getDoubleExtra("totalamount", 0);

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
        final Button sendEmail =findViewById(R.id.send);
        placeOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"forrestyschen@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Thank you for ordering!");
                i.putExtra(Intent.EXTRA_TEXT   , "Hello World!");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Checkout.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button checkOrderButton =findViewById(R.id.checkOrder);
        checkOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkOrder();
            }
        });

        view = (TextView) findViewById(R.id.output);
        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        updateTime(hr, min);
        addButtonClickListener();

        for(int i =0 ;i < list4.length; i++){
            foodCookingTime +=( list4[i] * list5[i]);
        }
        foodCookingTime = foodCookingTime /60 *100  + foodCookingTime %60 ;



        mDatabaseRference.child("cooker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child :children){
                    cooker.intervals.add( child.getValue(Interval.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void placeOrder(){

        pickTime = this.hr * 100 + this.min;
        endCookingTime = pickTime;
        startCookingTime = endCookingTime - foodCookingTime;

        if(startCookingTime %100 > 60){
            startCookingTime = startCookingTime - 40;
        }
        endCookingTime = pickTime;

        if(!checkOrder()){
            alertMessage("Time Not Available!","We will provide you earliest time. ");
                int avTime = checkEarlyTime();
            return;
        }
        //save new interval
        ArrayList<Interval> newCookerIntervals = cooker.getIntervals();
        mDatabaseRference.child("cooker").setValue(newCookerIntervals);

        FirebaseUser user  = auth.getInstance().getCurrentUser();
        String uid = user.getUid();
        pickTime =  hr * 100 + min ;

        String orderId = "123";

        mDatabaseRference.child("order").child(orderId).child("pickTime").setValue(pickTime);
        mDatabaseRference.child("order").child(orderId).child("userID").setValue(uid);

        for (int i = 0; i < list4.length; i++){
            mDatabaseRference.child("order").child(orderId).child("item").child(list4[i]+"").child("Qty").setValue(list3[i]);
        }

        AlertDialog.Builder builder= new AlertDialog.Builder(Checkout.this);
        builder.setMessage("Thank you for ordering from us!")
                .setPositiveButton("Cotinue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Checkout.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertMessage(String title,String message ){
        new AlertDialog.Builder(Checkout.this).setTitle(title).setMessage(message).show();
    }

    public boolean checkOrder(){

        boolean timeAv = cooker.CheckCooker(startCookingTime,endCookingTime); // now hard code
        return timeAv;
    }
    public int checkEarlyTime(){
        while(checkOrder() && startCookingTime >= 500 ){
            endCookingTime = endCookingTime - 1;
            startCookingTime = endCookingTime - foodCookingTime;
        }
        return endCookingTime;
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
        if (value < 10) return "0" + String.valueOf(value); else return String.valueOf(value);
    }
    private void updateTime(int hours, int mins) { String timeSet = ""; if (hours > 12) {
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
