package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.Manifest.permission;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by yichinhsiao on 5/7/18.
 */

public class AdminAddMenuActivity extends AppCompatActivity {

    private static final int LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int CAMERA_PERMISSION = 3;

    private Spinner menu_category;
    private EditText menu_name, menu_price, menu_calo, menu_prep;
    private ImageView menu_photo;
    private Button load_photo, take_photo, add_item;
    private Bitmap photo;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_menu);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference();
        //mDatabaseRference.keepSynced(true);


        /*test get "steak" category
        mDatabaseRference.child("menu").orderByChild("name").equalTo("steak").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String s = data.getKey();
                    mDatabaseRference.child("menu").child(s).child("category").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String s = (String) dataSnapshot.getValue();
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


        menu_category = (Spinner) findViewById(R.id.add_menu_spinner_category);
        menu_name = (EditText) findViewById(R.id.add_menu_name);
        menu_price = (EditText) findViewById(R.id.add_menu_price);
        menu_calo = (EditText) findViewById(R.id.add_menu_calo);
        menu_prep = (EditText) findViewById(R.id.add_menu_prep);
        menu_photo = (ImageView) findViewById(R.id.add_menu_photo);
        load_photo = (Button) findViewById(R.id.add_menu_btn_load_photo);
        take_photo = (Button) findViewById(R.id.add_menu_btn_take_photo);
        add_item = (Button) findViewById(R.id.add_menu_btn_add_menu);

        load_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE);
            }
        });

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{permission.CAMERA}, CAMERA_PERMISSION);
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        });

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(menu_name.getText().toString().isEmpty() ||
                        menu_price.getText().toString().isEmpty() ||
                        menu_calo.getText().toString().isEmpty() ||
                        menu_prep.getText().toString().isEmpty() ||
                        photo == null) {
                    Toast.makeText(AdminAddMenuActivity.this, "Please fill in all information", Toast.LENGTH_LONG).show();
                    /*test get picture
                    mDatabaseRference.child("menu").child("5").child("picture").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String pic_string = (String) dataSnapshot.getValue();
                            byte [] decode = Base64.decode(pic_string, Base64.DEFAULT);
                            Bitmap pic = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                            menu_photo.setImageBitmap(pic);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    */
                }
                else{
                    mDatabaseRference.child("menu").orderByChild("name").equalTo(menu_name.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        Toast.makeText(AdminAddMenuActivity.this,
                                                "Menu name already used", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        checkAndAddData();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }});
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri photo_uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo_uri);
            } catch (IOException e) {

            }
            menu_photo.setImageBitmap(photo);
        }
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            photo = (Bitmap) data.getExtras().get("data");
            menu_photo.setImageBitmap(photo);
        }
    }

    private void checkAndAddData() {
        if (!checkPrice()) {
            Toast.makeText(AdminAddMenuActivity.this, "Please fill in all information", Toast.LENGTH_LONG).show();
        }
        else if (!checkCalo()) {
            Toast.makeText(AdminAddMenuActivity.this, "Please fill in all information", Toast.LENGTH_LONG).show();
        }
        else if (!checkPrep()){
            Toast.makeText(AdminAddMenuActivity.this, "Please fill in all information", Toast.LENGTH_LONG).show();
        }
        else {
            mDatabaseRference.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int size = (int) dataSnapshot.getChildrenCount();
                    Menu menu = new Menu(size+1, menu_category.getSelectedItem().toString(),
                            menu_name.getText().toString(),
                            Double.parseDouble(menu_price.getText().toString()),
                            Integer.parseInt(menu_calo.getText().toString()),
                            Integer.parseInt(menu_prep.getText().toString()),
                            photo, true, 0);
                    addMenu(menu);
                    reset();
                    Toast.makeText(AdminAddMenuActivity.this, "Menu added successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private Boolean checkPrice() {
        String price = menu_price.getText().toString();
        price = price.replaceFirst("^0*", "");
        if(!price.isEmpty()) {
            if(price.charAt(0) == '.'){
                price = "0" + price;
            }
            menu_price.setText(price);
            return true;
        }
        return false;
    }

    private Boolean checkCalo() {
        String price = menu_calo.getText().toString();
        price = price.replaceFirst("^0*", "");
        if(!price.isEmpty()) {
            menu_calo.setText(price);
            return true;
        }
        return false;
    }

    private Boolean checkPrep() {
        String price = menu_prep.getText().toString();
        price = price.replaceFirst("^0*", "");
        if(!price.isEmpty()) {
            if(Integer.parseInt(price) > 10) {
                price = "10";
                menu_prep.setText(price);
            }
            menu_prep.setText(price);
            return true;
        }
        return false;
    }

    private void addMenu(Menu menu) {
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("category").setValue(menu.getCategory());
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("name").setValue(menu.getName());
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("price").setValue(menu.getPrice());
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("calories").setValue(menu.getCalories());
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("preparation_time").setValue(menu.getPreparationTime());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        menu.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
        String picture = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("picture").setValue(picture);
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("enabled").setValue(menu.getEnabled());
        mDatabaseRference.child("menu").child(String.valueOf(menu.getId())).
                child("popularity").setValue(menu.getPopularity());
    }

    private void reset() {
        menu_category.setSelection(0);
        menu_name.setText("");
        menu_price.setText("");
        menu_calo.setText("");
        menu_prep.setText("");
        menu_photo.setImageBitmap(null);
        photo = null;
    }
}