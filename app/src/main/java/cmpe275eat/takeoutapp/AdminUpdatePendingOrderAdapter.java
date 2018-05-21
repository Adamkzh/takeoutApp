package cmpe275eat.takeoutapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Arrays;

/**
 * Created by yichinhsiao on 5/14/18.
 */

public class AdminUpdatePendingOrderAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ViewHolder holder;
    private ArrayList<Order> list = new ArrayList<Order>();
    private Context context;
    private String[] status_array;
    private int index;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    public static class ViewHolder {
        TextView order_id, start_time, ready_time, pickup_time;
        Spinner status;
        Button detail;
    }

    public AdminUpdatePendingOrderAdapter(ArrayList<Order> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.admin_update_pending_order_single_order, null);
            holder = new ViewHolder();
            holder.order_id = (TextView) view.findViewById(R.id.update_pending_order_order_id);
            holder.start_time = (TextView) view.findViewById(R.id.update_pending_order_start_time);
            holder.ready_time = (TextView) view.findViewById(R.id.update_pending_order_ready_time);
            holder.pickup_time = (TextView) view.findViewById(R.id.update_pending_order_pickup_time);
            holder.status = (Spinner) view.findViewById(R.id.update_pending_order_status);
            holder.detail = (Button) view.findViewById(R.id.update_pending_order_detail);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        if(list.size() > 0) {
            holder.order_id.setText(list.get(position).getOrderId());
            holder.start_time.setText(list.get(position).getStartTime());
            holder.ready_time.setText(list.get(position).getReadyTime());
            holder.pickup_time.setText(list.get(position).getPickupTime());
            status_array = context.getResources().getStringArray(R.array.status_arrays);
            holder.status.setSelection(Arrays.asList(status_array).indexOf(list.get(position).getStatus()));
            holder.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if( i != Arrays.asList(status_array).indexOf(list.get(position).getStatus())) {
                        index = i;
                        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(context);
                        dialog_builder.setMessage("Are you sure to change the order status to "
                                + status_array[i] + "?");
                        dialog_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                notifyDataSetChanged();
                            }
                        });
                        dialog_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Firebase.setAndroidContext(context);
                                FirebaseApp.initializeApp(context);
                                auth = FirebaseAuth.getInstance();
                                mFirebaseDatabase = FirebaseDatabase.getInstance();
                                mDatabaseRference = mFirebaseDatabase.getReference();
                                mDatabaseRference.child("order").child(list.get(position).getOrderId())
                                        .child("status").setValue(status_array[index]);
                                if(status_array[index].equals("Picked") || status_array[index].equals("Abandoned")) {
                                    if(status_array[index].equals("Picked")){
                                        final String id = list.get(position).getOrderId();
                                        final GMailSender sender = new GMailSender("garyhsiao1219@gmail.com",
                                                "yichin0091");
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            public Void doInBackground(Void... arg) {
                                                try {
                                                    sender.sendMail("Order is already Picked up",
                                                            "Hi, your order is already picked up\n" +
                                                                    "Order ID is " + id
                                                                    + "\nThank you for choosing our restaurant",
                                                            "garyhsiao1219@gmail.com",
                                                            list.get(position).getCustomerEmail());

                                                } catch (Exception e) {
                                                    Log.e("SendMail", e.getMessage(), e);
                                                }
                                                return null;
                                            }
                                        }.execute();
                                        list.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                                else {
                                    list.get(position).setStatus(status_array[index]);
                                    if(status_array[index].equals("Fulfilled")) {
                                        final GMailSender sender = new GMailSender("garyhsiao1219@gmail.com",
                                                "yichin0091");
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            public Void doInBackground(Void... arg) {
                                                try {
                                                    sender.sendMail("Your Order is Ready",
                                                            "Hi, your order is ready for pickup\n" +
                                                                    "Order ID is " + list.get(position).getOrderId(),
                                                            "garyhsiao1219@gmail.com",
                                                            list.get(position).getCustomerEmail());
                                                } catch (Exception e) {
                                                    Log.e("SendMail", e.getMessage(), e);
                                                }
                                                return null;
                                            }
                                        }.execute();
                                    }
                                    notifyDataSetChanged();
                                }
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog dialog = dialog_builder.create();
                        dialog.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog_builder = new AlertDialog.Builder(context);
                    String message = "";
                    int amount = list.get(position).getItems().size();
                    for(int i = 0; i < amount; i++) {
                        message = message.concat(list.get(position).getItems().get(i).getName()
                                                    + " * " + list.get(position).getItems().get(i).getQuantity()
                                                    + "     $" + list.get(position).getItems().get(i).getUnitPrice()
                                                    + " * " + list.get(position).getItems().get(i).getQuantity() + "\n");
                    }
                    dialog_builder.setMessage("Order Time: " + list.get(position).getOrderTime() + "\n\n" + message
                                                + "\nTotal Price: $" + list.get(position).getTotalPrice());
                    dialog_builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = dialog_builder.create();
                    dialog.show();
                }
            });
        }
        else {
            Toast.makeText(context, "No Pending Order", Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
