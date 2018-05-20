package cmpe275eat.takeoutapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import cmpe275eat.takeoutapp.bean.GoodsBean;
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

    Date createTime;
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


    public String orderid;
    public double allamount;
    public int totalqtyL;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        orderid = UUID.randomUUID().toString();

        set_date = (TextView) findViewById(R.id.set_date);
        set_time = (TextView) findViewById(R.id.set_time);



        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH ) + 1;
        day = now.get(Calendar.DAY_OF_MONTH);

        String hourShow = Integer.toString(now.get(Calendar.HOUR_OF_DAY));
        String minuteShow= Integer.toString(now.get(Calendar.MINUTE));

        String showDefaultDate = Integer.toString(now.get(Calendar.YEAR)) +"." + Integer.toString(now.get(Calendar.MONTH ) + 1) +"." + Integer.toString(now.get(Calendar.DAY_OF_MONTH));
        String showDefaultTime = hourShow +":" + minuteShow;


        hour = now.get(Calendar.HOUR_OF_DAY);
        minute = now.get(Calendar.MINUTE);

        set_date.setText(showDefaultDate);
        set_time.setText(showDefaultTime);


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

        placeOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    placeOrder();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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


    public void placeOrder() throws ParseException {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat currentFormate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String currentTimetoStore = currentFormate.format(currentTime);

        pickTime = hour * 100 + minute;
        readyTime = pickTime;

        String pickTimeCal = hour +":" + minute +":00";
        SimpleDateFormat format = new SimpleDateFormat( "HH:mm:ss");
        DateFormat df = new SimpleDateFormat("HH:mm:ss");

        Date pickTimeDate = format.parse( pickTimeCal);

        Date startCookingTimeDate =  minusMinutesToDate(foodCookingTime,pickTimeDate);
        String startCookingTimeString = df.format(startCookingTimeDate);
        startCookingTime = Integer.valueOf(startCookingTimeString.substring(0,2))*100 + Integer.valueOf(startCookingTimeString.substring(3,5));



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

        ArrayList<OrderItem> orderlist = new ArrayList<>();
        //add orderItem attributes
        for (int i = 0; i < itemL.length; i++){
            OrderItem orderItem = new OrderItem();
            orderItem.setId(i);
            orderItem.setName(itemL[i]);
            orderItem.setQuantity(qtyL[i]);
            orderItem.setUnitPrice(Double.parseDouble(priceL[i]));
            orderlist.add(orderItem);
            final int[] currentpop = {0};
            final String[] pname = new String[1];
            pname[0] = String.valueOf(itemL[i]);

            mDatabaseRference.child("menu").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                        //Loop 1 to go through all the child nodes of users
                        String itemskey = uniqueKeySnapshot.getKey();
                        GetMenu m = uniqueKeySnapshot.getValue(GetMenu.class);
                        if (m.getName().equals(pname[0])) {
                            currentpop[0] = m.getPopularity();
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            currentpop[0] += qtyL[i];
            mDatabaseRference.child("menu").child(String.valueOf(idL[i])).child("popularity").setValue(qtyL[i]);
        }

        String inputMonth = "";
        String inputDay ="";
        if(month < 10){
            inputMonth = "0" + month;
        }
        if(day <10){
            inputDay = "0" + day;
        }

        //save Order entity
        Order order = new Order();
        order.setUserId(uid);
        order.setCustomerEmail(user.getEmail());
        order.setOrderId(orderid);
        order.setTotalPrice(allamount);
        order.setStatus("Queued");
        order.setOrderTime(currentTimetoStore);
        order.setPickupTime(year+"-"+inputMonth+"-"+inputDay +" "+pickTimeCal);
        order.setStartTime( year+"-"+inputMonth+"-"+inputDay +" "+startCookingTimeString);
        order.setReadyTime(year+"-"+inputMonth+"-"+inputDay +" "+pickTimeCal);
        order.setItems(orderlist);



        //save this order to DB
        DatabaseReference newPostRef =  mDatabaseRference.child("order").push();
        newPostRef.setValue(order);

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

    private static Date minusMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs - (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }


    public void alertMessage(String title,String message ){
        new AlertDialog.Builder(Checkout.this).setTitle(title).setMessage(message).show();
    }

    public boolean checkOrder(){

        boolean timeAv = cooker.CheckCooker(startCookingTime,readyTime,orderid, year, month, day); // now hard code
        return timeAv;
    }
    public int checkEarlyTime(){
        while(checkOrder() && startCookingTime >= 500 ){
            readyTime = readyTime - 1;
            startCookingTime = readyTime - foodCookingTime;
        }
        return readyTime;
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();
        Calendar ca = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        switch (id) {
            case Date_id:
                // Open the datepicker dialog
                long now = System.currentTimeMillis() - 1000;
                DatePickerDialog dateDialog = new DatePickerDialog(this, date_listener, year, month, day);
                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dateDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*7)); //After 7 Days from Now
                return dateDialog;
            case Time_id:
                // Open the timepicker dialog
                TimePickerDialog timeDialog = new TimePickerDialog(this, time_listener, hour, minute, false);
                return timeDialog;
        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int years, int months, int days) {
            // store the data in one string and set it to text
            year = years;
            month = months + 1;
            day = days;

            String date1 = String.valueOf(months + 1) + "/" + String.valueOf(days)
                    + "/" + String.valueOf(years);
            set_date.setText(date1);
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hours, int minutes) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hours) + ":" + String.valueOf(minutes);
            hour = hours;
            minute = minutes;
            set_time.setText(time1);
        }
    };

}
