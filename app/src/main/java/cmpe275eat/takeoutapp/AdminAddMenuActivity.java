package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by yichinhsiao on 5/7/18.
 */

public class AdminAddMenuActivity extends AppCompatActivity {

    private Spinner category;
    private EditText menu_name, menu_price, menu_calo, menu_prep;
    private ImageView menu_photo;
    private Button upload_photo, take_photo, add_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_menu);

        category = (Spinner) findViewById(R.id.add_menu_spinner_category);
        menu_name = (EditText) findViewById(R.id.add_menu_name);
        menu_price = (EditText) findViewById(R.id.add_menu_price);
        menu_calo = (EditText) findViewById(R.id.add_menu_calo);
        menu_prep = (EditText) findViewById(R.id.add_menu_prep);
        menu_photo = (ImageView) findViewById(R.id.add_menu_photo);
        upload_photo = (Button) findViewById(R.id.add_menu_btn_upload);
        take_photo = (Button) findViewById(R.id.add_menu_btn_takePhoto);
        add_item = (Button) findViewById(R.id.add_menu_btn_add_menu);

        /*
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        */

        upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check input and DB
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri photo = data.getData();
            String[] file_path = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(photo,
                    file_path, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(file_path[0]);
            String photo_path = cursor.getString(index);
            cursor.close();
            menu_photo.setImageBitmap(BitmapFactory.decodeFile(photo_path));
        }
    }
}
