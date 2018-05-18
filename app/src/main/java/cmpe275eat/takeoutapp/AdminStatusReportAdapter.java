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

public class AdminStatusReportAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ViewHolder holder;
    private ArrayList<Order> list = new ArrayList<Order>();
    private Context context;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    public static class ViewHolder {
        TextView order_id, order_time, start_time, ready_time, pickup_time;
        Button detail;
    }

    public AdminStatusReportAdapter(ArrayList<Order> list, Context context) {
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
            view = inflater.inflate(R.layout.admin_status_report_single_order, null);
            holder = new ViewHolder();
            holder.order_id = (TextView) view.findViewById(R.id.status_report_order_id);
            holder.order_time = (TextView) view.findViewById(R.id.status_report_order_time);
            holder.start_time = (TextView) view.findViewById(R.id.status_report_start_time);
            holder.ready_time = (TextView) view.findViewById(R.id.status_report_ready_time);
            holder.pickup_time = (TextView) view.findViewById(R.id.status_report_pickup_time);
            holder.detail = (Button) view.findViewById(R.id.status_report_detail);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.order_id.setText(list.get(position).getOrderId());
        holder.order_time.setText(list.get(position).getOrderTime());
        holder.start_time.setText(list.get(position).getStartTime());
        holder.ready_time.setText(list.get(position).getReadyTime());
        holder.pickup_time.setText(list.get(position).getPickupTime());
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
                dialog_builder.setMessage("Customer Email: " + list.get(position).getCustomerEmail() + "\n\n" + message
                        + "\nTotal Price: $" + list.get(position).getTotalPrice() + "\n\nStatus: "
                        + list.get(position).getStatus());
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

        return view;
    }
}
