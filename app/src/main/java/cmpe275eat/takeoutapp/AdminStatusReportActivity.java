package cmpe275eat.takeoutapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yichinhsiao on 5/14/18.
 */

public class AdminStatusReportActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;
    private ArrayList<Order> order_list;
    private AdminStatusReportAdapter status_report_adapter;
    private ListView listView;
    private EditText start_date, end_date;
    private Spinner sort_by;
    private Button search;
    private Calendar start_calendar, end_calendar;
    private DatePickerDialog.OnDateSetListener start_date_listener, end_date_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_status_report);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference();

        start_date = (EditText) findViewById(R.id.admin_status_report_start_date);
        end_date = (EditText) findViewById(R.id.admin_status_report_end_date);
        sort_by = (Spinner) findViewById(R.id.admin_status_report_sort_by);
        search = (Button) findViewById(R.id.admin_status_report_search);
        order_list = new ArrayList<Order>();

        start_calendar = Calendar.getInstance();
        end_calendar = Calendar.getInstance();
        start_date_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                start_calendar.set(Calendar.YEAR, year);
                start_calendar.set(Calendar.MONTH, month);
                start_calendar.set(Calendar.DAY_OF_MONTH, day);
                setUpDate("start");
            }
        };
        end_date_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                end_calendar.set(Calendar.YEAR, year);
                end_calendar.set(Calendar.MONTH, month);
                end_calendar.set(Calendar.DAY_OF_MONTH, day);
                setUpDate("end");
            }
        };

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AdminStatusReportActivity.this, start_date_listener,
                        start_calendar.get(Calendar.YEAR), start_calendar.get(Calendar.MONTH),
                        start_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AdminStatusReportActivity.this, end_date_listener,
                        end_calendar.get(Calendar.YEAR), end_calendar.get(Calendar.MONTH),
                        end_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order_list = new ArrayList<Order>();
                if(start_date.getText().toString().isEmpty() || end_date.getText().toString().isEmpty()) {
                    Toast.makeText(AdminStatusReportActivity.this, "Please select start date and end date"
                        , Toast.LENGTH_LONG).show();
                }
                else {
                    SimpleDateFormat date_format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                    Date from = null, to = null;
                    try {
                        from = date_format.parse(start_date.getText().toString());
                        to = date_format.parse(end_date.getText().toString());
                    } catch (Exception e) {
                    }
                    if(from != null && to != null) {
                        long check = (to.getTime() - from.getTime()) / (24*60*60*1000);
                        check += 1;
                        if(check < 1 || check > 7) {
                            Calendar calendar = Calendar.getInstance();
                            String[] date = start_date.getText().toString().split("-");
                            calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
                            calendar.set(Calendar.MONTH, Integer.parseInt(date[0])-1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
                            calendar.add(Calendar.DATE, 6);
                            Toast.makeText(AdminStatusReportActivity.this,
                                    "End date must be from " + start_date.getText().toString() +
                                            " to " + date_format.format(calendar.getTime())
                                    , Toast.LENGTH_LONG).show();
                        }
                        else{
                            mDatabaseRference.child("my_order").orderByChild("orderTime").startAt(start_date.getText().toString())
                                    .endAt(end_date.getText().toString() + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            Order order = new Order();
                                            order.setOrderId((String) data.child("orderId").getValue());
                                            order.setUserId((String) data.child("userId").getValue());
                                            order.setOrderTime((String) data.child("orderTime").getValue());
                                            order.setStartTime((String) data.child("startTime").getValue());
                                            order.setReadyTime((String) data.child("readyTime").getValue());
                                            order.setPickupTime((String) data.child("pickupTime").getValue());
                                            order.setStatus((String) data.child("status").getValue());
                                            order.setCustomerEmail((String) data.child("customerEmail").getValue());
                                            Number total_price_long = (Number) data.child("totalPrice").getValue();
                                            Double total_price = total_price_long.doubleValue();
                                            order.setTotalPrice(total_price);
                                            ArrayList<OrderItem> item_list = new ArrayList<OrderItem>();
                                            int number = (int) data.child("items").getChildrenCount();
                                            for (int j = 1; j <= number; j++) {
                                                OrderItem item = new OrderItem();
                                                Number id_long = (Number) data.child("items")
                                                        .child(String.valueOf(j)).child("id").getValue();
                                                int id = id_long.intValue();
                                                item.setId(id);
                                                item.setName((String) data.child("items")
                                                        .child(String.valueOf(j)).child("name").getValue());
                                                Long quantity_long = (Long) data.child("items")
                                                        .child(String.valueOf(j)).child("quantity").getValue();
                                                int quantity = quantity_long.intValue();
                                                item.setQuantity(quantity);
                                                Number unit_price_long = (Number) data.child("items")
                                                        .child(String.valueOf(j)).child("unitPrice").getValue();
                                                Double unit_price = unit_price_long.doubleValue();
                                                item.setUnitPrice(unit_price);
                                                item_list.add(item);
                                            }
                                            order.setItems(item_list);
                                            order_list.add(order);
                                            if(sort_by.getSelectedItem().toString().equals("Start Time")) {
                                                Collections.sort(order_list, new OrderStartTimeComparator());
                                            }
                                            status_report_adapter = new AdminStatusReportAdapter(order_list,
                                                    AdminStatusReportActivity.this);
                                            listView = (ListView) findViewById(R.id.admin_status_report_list);
                                            listView.setAdapter(status_report_adapter);
                                        }
                                    }
                                    else{
                                        status_report_adapter = new AdminStatusReportAdapter(order_list,
                                                AdminStatusReportActivity.this);
                                        listView = (ListView) findViewById(R.id.admin_status_report_list);
                                        listView.setAdapter(status_report_adapter);
                                        Toast.makeText(AdminStatusReportActivity.this,
                                                "No Order", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void setUpDate(String option) {
        String format = "MM-dd-yyyy";
        SimpleDateFormat date_form = new SimpleDateFormat(format, Locale.US);
        if(option.equals("start")){
            start_date.setText(date_form.format(start_calendar.getTime()));
        }
        else {
            end_date.setText(date_form.format(end_calendar.getTime()));
        }
    }

    public class OrderStartTimeComparator implements Comparator<Order> {
        public int compare(Order o1, Order o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }
}