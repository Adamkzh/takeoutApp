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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by yichinhsiao on 5/10/18.
 */

public class AdminRemoveMenuAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ViewHolder holder;
    private ArrayList<Menu> list = new ArrayList<Menu>();
    private Context context;
    private ImageView picture;
    private TextView name, enabled;
    private Button activate, remove;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    public static class ViewHolder {
        TextView name, enabled;
        ImageView picture;
        Button activate, remove;
    }

    public AdminRemoveMenuAdapter(ArrayList<Menu> list, Context context) {
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
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.admin_remove_menu_single_menu, null);
            holder = new ViewHolder();
            holder.picture = (ImageView) view.findViewById(R.id.single_menu_pic);
            holder.name = (TextView) view.findViewById(R.id.single_menu_name);
            holder.enabled = (TextView) view.findViewById(R.id.single_menu_enabled);
            holder.activate = (Button) view.findViewById(R.id.single_menu_activate);
            holder.remove = (Button) view.findViewById(R.id.single_menu_remove);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        if(list.size() == 0) {
            holder.name.setText("No Menu");
        }
        else {
            holder.picture.setImageBitmap(list.get(position).getPicture());
            holder.name.setText(list.get(position).getName());
            Boolean status = list.get(position).getEnabled();
            if(status){
                holder.enabled.setText("Enabled");
                holder.enabled.setTextColor(Color.BLUE);
            }
            else{
                holder.enabled.setText("Disabled");
                holder.enabled.setTextColor(Color.RED);
            }

            holder.activate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Firebase.setAndroidContext(context);
                    FirebaseApp.initializeApp(context);
                    auth = FirebaseAuth.getInstance();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mDatabaseRference = mFirebaseDatabase.getReference();
                    mDatabaseRference.child("menu").orderByChild("name")
                            .equalTo(list.get(position).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                String id = data.getKey();
                                mDatabaseRference.child("menu").child(id).child("enabled").setValue(true);
                                list.get(position).setEnabled(true);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Firebase.setAndroidContext(context);
                    FirebaseApp.initializeApp(context);
                    auth = FirebaseAuth.getInstance();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mDatabaseRference = mFirebaseDatabase.getReference();
                    mDatabaseRference.child("menu").orderByChild("name")
                            .equalTo(list.get(position).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                    String id = data.getKey();
                                    mDatabaseRference.child("menu").child(id).child("enabled").setValue(false);
                                    list.get(position).setEnabled(false);
                                    notifyDataSetChanged();
                                }
                            }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }

        return view;
    }
}
