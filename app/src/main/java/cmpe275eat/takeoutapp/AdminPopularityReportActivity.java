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
 * Created by yichinhsiao on 5/18/18.
 */

public class AdminPopularityReportActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;
    private ArrayList<Menu> menu_list;
    private ArrayList<OrderItem> item_list;
    private AdminPopularityReportAdapter popularity_report_adapter;
    private ListView listView;
    private EditText start_date, end_date;
    private Spinner category;
    private Button search;
    private Calendar start_calendar, end_calendar;
    private DatePickerDialog.OnDateSetListener start_date_listener, end_date_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_popularity_report);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference();

        start_date = (EditText) findViewById(R.id.admin_popularity_report_start_date);
        end_date = (EditText) findViewById(R.id.admin_popularity_report_end_date);
        category = (Spinner) findViewById(R.id.admin_popularity_report_category);
        search = (Button) findViewById(R.id.admin_popularity_report_search);
        menu_list = new ArrayList<Menu>();

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
                new DatePickerDialog(AdminPopularityReportActivity.this, start_date_listener,
                        start_calendar.get(Calendar.YEAR), start_calendar.get(Calendar.MONTH),
                        start_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AdminPopularityReportActivity.this, end_date_listener,
                        end_calendar.get(Calendar.YEAR), end_calendar.get(Calendar.MONTH),
                        end_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_list = new ArrayList<Menu>();
                item_list = new ArrayList<OrderItem>();
                if(start_date.getText().toString().isEmpty() || end_date.getText().toString().isEmpty()) {
                    Toast.makeText(AdminPopularityReportActivity.this, "Please select start date and end date"
                            , Toast.LENGTH_LONG).show();
                }
                else {
                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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
                            calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                            calendar.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
                            calendar.add(Calendar.DATE, 6);
                            Toast.makeText(AdminPopularityReportActivity.this,
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
                                            int number = (int) data.child("items").getChildrenCount();
                                            for (int j = 1; j <= number; j++) {
                                                OrderItem item = new OrderItem();
                                                item.setName(data.child("items").child(String.valueOf(j)).child("name").getValue().toString());
                                                Long quantity_long = (Long) data.child("items")
                                                        .child(String.valueOf(j)).child("quantity").getValue();
                                                int quantity = quantity_long.intValue();
                                                item.setQuantity(quantity);
                                                item_list.add(item);
                                            }
                                        }
                                        Collections.sort(item_list, new AdminPopularityReportActivity.ItemNameComparator());
                                        for(int i = 0; i < item_list.size()-1; i++) {
                                            for(int j = i+1; j < item_list.size(); j++) {
                                                if(item_list.get(i).getName().equals(item_list.get(j).getName())) {
                                                    item_list.get(i).setQuantity(item_list.get(i).getQuantity() + item_list.get(j).getQuantity());
                                                    item_list.get(j).setQuantity(0);
                                                }
                                                else {
                                                    i = j-1;
                                                    break;
                                                }
                                            }
                                        }
                                        for(int i = 0; i < item_list.size(); i++) {
                                            if(item_list.get(i).getQuantity() == 0) {
                                                item_list.remove(i);
                                            }
                                        }
                                        String option = category.getSelectedItem().toString();
                                        mDatabaseRference.child("menu").orderByChild("category").equalTo(option)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                                                            for(int i = 0; i < item_list.size(); i++) {
                                                                if(data.child("name").getValue().toString().equals(item_list.get(i).getName())) {
                                                                    Menu menu = new Menu();
                                                                    menu.setPicture(data.child("picture").getValue().toString());
                                                                    menu.setName(data.child("name").getValue().toString());
                                                                    Number price_long = (Number) data.child("price").getValue();
                                                                    Double price = price_long.doubleValue();
                                                                    menu.setPrice(price);
                                                                    Long calories_long = (Long) data.child("calories").getValue();
                                                                    int calories = calories_long.intValue();
                                                                    menu.setCalories(calories);
                                                                    Long preparation_time_long = (Long) data.child("preparation_time").getValue();
                                                                    int preparation_time = preparation_time_long.intValue();
                                                                    menu.setPreparationTime(preparation_time);
                                                                    menu.setEnabled((Boolean) data.child("enabled").getValue());
                                                                    menu.setPopularity(item_list.get(i).getQuantity());
                                                                    menu_list.add(menu);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        Collections.sort(menu_list, new AdminPopularityReportActivity.MenuPopularityComparator());
                                                        popularity_report_adapter = new AdminPopularityReportAdapter(menu_list,
                                                                AdminPopularityReportActivity.this);
                                                        listView = (ListView) findViewById(R.id.admin_popularity_report_list);
                                                        listView.setAdapter(popularity_report_adapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                    else{
                                        popularity_report_adapter = new AdminPopularityReportAdapter(menu_list, AdminPopularityReportActivity.this);
                                        listView = (ListView) findViewById(R.id.admin_popularity_report_list);
                                        listView.setAdapter(popularity_report_adapter);
                                        Toast.makeText(AdminPopularityReportActivity.this,
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
        String format = "yyyy-MM-dd";
        SimpleDateFormat date_form = new SimpleDateFormat(format, Locale.US);
        if(option.equals("start")){
            start_date.setText(date_form.format(start_calendar.getTime()));
        }
        else {
            end_date.setText(date_form.format(end_calendar.getTime()));
        }
    }

    public class ItemNameComparator implements Comparator<OrderItem> {
        public int compare(OrderItem o1, OrderItem o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public class MenuPopularityComparator implements Comparator<Menu> {
        public int compare(Menu m1, Menu m2) {
            return m2.getPopularity() - m1.getPopularity();
        }
    }
}