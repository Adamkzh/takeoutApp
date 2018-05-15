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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;

import cmpe275eat.takeoutapp.cooker.Cooker;
import cmpe275eat.takeoutapp.cooker.Interval;

import java.util.Calendar;
import java.util.UUID;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


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

    int year ;
    int month ;
    int day ;
    int hour ;
    int minute ;

    int pickTime;
    int foodCookingTime;
    int startCookingTime;
    int readyTime;

    String[] itemL;
    String[] priceL;
    int[] qtyL;
    int[] idL;
    int[] timeL;


    Cooker cooker = new Cooker();

    private static Button date, time;
    private static TextView set_date, set_time;
    private static final int Date_id = 0;
    private static final int Time_id = 1;



    public double allamount;
    public int totalqtyL;

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
        itemL = intent.getStringArrayExtra("itemlist");
        priceL = intent.getStringArrayExtra("pricelist");
        qtyL = intent.getIntArrayExtra("qtylist");
        idL = intent.getIntArrayExtra("idlist");
        timeL = intent.getIntArrayExtra("timelist");
        totalqtyL = intent.getIntExtra("totalqty", 0);
        allamount = intent.getDoubleExtra("totalamount", 0);

        itemlist = (ListView)findViewById(R.id.list1);
        pricelist = (ListView)findViewById(R.id.list2);
        timelist = (ListView)findViewById(R.id.list3);


        List<String> your_array_list1 = new ArrayList<String>();
        for(String s: itemL) {
            your_array_list1.add(s);
        }

        List<String> your_array_list2 = new ArrayList<String>();
        for(String s: priceL) {
            your_array_list2.add(s);
        }

        List<Integer> your_array_list3 = new ArrayList<Integer>();
        for(int s: qtyL) {
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


        for(int i =0 ;i < idL.length; i++){
            foodCookingTime +=( idL[i] * timeL[i]);
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

        date = (Button) findViewById(R.id.selectdate);
        time = (Button) findViewById(R.id.selecttime);
        set_date = (TextView) findViewById(R.id.set_date);
        set_time = (TextView) findViewById(R.id.set_time);
        date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Date dialog
                showDialog(Date_id);
            }
        });
        time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show time dialog
                showDialog(Time_id);
            }
        });
    }


    public void placeOrder(){
        Date currentTime = Calendar.getInstance().getTime();

        pickTime = hour * 100 + minute;
        readyTime = pickTime;
        startCookingTime = readyTime - foodCookingTime;

        if(startCookingTime %100 > 60){
            startCookingTime = startCookingTime - 40;
        }

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
        String orderId = uid;
        ArrayList<OrderItem> orderlist = new ArrayList<>();

        for (int i = 0; i < itemL.length; i++){
            OrderItem orderItem = new OrderItem();
            orderItem.setId(i);
            orderItem.setName("ice");
            orderItem.setQuantity(qtyL[i]);
            orderItem.setUnitPrice(Double.parseDouble(priceL[i]));

        }

        Order order = new Order();

        order.setUserId(uid);
        order.setCustomerEmail(user.getEmail());
        order.setOrderId(UUID.randomUUID().toString());
        order.setTotalPirce(allamount);
        order.setStatus("queued");
        order.setOrderTime(currentTime.toString());
        order.setPickupTime(pickTime+"");
        order.setStartTime(startCookingTime+"");
        order.setReadyTime(readyTime+"");
        order.setItems(orderlist);

        DatabaseReference newPostRef =  mDatabaseRference.child("order").push();
        newPostRef.setValue(order);


//        mDatabaseRference.child("order").child(orderId).child("pickTime").setValue(pickTime);
//        mDatabaseRference.child("order").child(orderId).child("userID").setValue(uid);
//
//        for (int i = 0; i < idL.length; i++){
//            mDatabaseRference.child("order").child(orderId).child("item").child("" + i).child("id").setValue(idL[i]);
//            mDatabaseRference.child("order").child(orderId).child("item").child("" + i).child("qty").setValue(qtyL[i]);
//        }

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

        boolean timeAv = cooker.CheckCooker(startCookingTime,readyTime); // now hard code
        return timeAv;
    }
    public int checkEarlyTime(){
        while(checkOrder() && startCookingTime >= 500 ){
            readyTime = readyTime - 1;
            startCookingTime = readyTime - foodCookingTime;
        }
        return readyTime;
    }


    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        switch (id) {
            case Date_id:

                // Open the datepicker dialog
                return new DatePickerDialog(this, date_listener, year,
                        month, day);
            case Time_id:

                // Open the timepicker dialog
                return new TimePickerDialog(this, time_listener, hour,
                        minute, false);

        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            String date1 = String.valueOf(month) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            set_date.setText(date1);
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            set_time.setText(time1);
        }
    };

}
