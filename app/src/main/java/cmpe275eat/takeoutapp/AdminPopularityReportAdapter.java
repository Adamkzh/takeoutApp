package cmpe275eat.takeoutapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
/**
 * Created by yichinhsiao on 5/18/18.
 */

public class AdminPopularityReportAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private AdminPopularityReportAdapter.ViewHolder holder;
    private ArrayList<Menu> list = new ArrayList<Menu>();
    private Context context;

    public static class ViewHolder {
        TextView name, price, calories, preparation_time, status, ordered_times;
        ImageView picture;
    }

    public AdminPopularityReportAdapter(ArrayList<Menu> list, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.admin_popularity_report_single_menu, null);
            holder = new AdminPopularityReportAdapter.ViewHolder();
            holder.picture = (ImageView) view.findViewById(R.id.popularity_report_pic);
            holder.name = (TextView) view.findViewById(R.id.popularity_report_name);
            holder.price = (TextView) view.findViewById(R.id.popularity_report_price);
            holder.calories = (TextView) view.findViewById(R.id.popularity_report_calories);
            holder.preparation_time = (TextView) view.findViewById(R.id.popularity_report_preparation_time);
            holder.status = (TextView) view.findViewById(R.id.popularity_report_status);
            holder.ordered_times = (TextView) view.findViewById(R.id.popularity_report_order_times);
            view.setTag(holder);
        }
        else {
            holder = (AdminPopularityReportAdapter.ViewHolder) view.getTag();
        }

        if(list.size() > 0) {
            holder.picture.setImageBitmap(list.get(position).getPicture());
            holder.name.setText(list.get(position).getName());
            holder.price.setText(String.valueOf(list.get(position).getPrice()));
            holder.calories.setText(String.valueOf(list.get(position).getCalories()));
            holder.preparation_time.setText(String.valueOf(list.get(position).getPreparationTime()));
            Boolean enabled = list.get(position).getEnabled();
            if(enabled){
                holder.status.setText("Enabled");
                holder.status.setTextColor(Color.BLUE);
            }
            else{
                holder.status.setText("Disabled");
                holder.status.setTextColor(Color.RED);
            }
            holder.ordered_times.setText(String.valueOf(list.get(position).getPopularity()));
        }
        else {
            Toast.makeText(context, "No Menu", Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
