package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    private Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_pickUp:
                    fragment = new HomeMenu();
                    break;
                case R.id.navigation_order_history:
                    fragment = new OrderHistory();
                    break;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(MainMenuActivity.this,LogoutActivity.class);
                    startActivity(intent);
                    return true;
            }

            final FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        fragment = new HomeMenu();
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void toOrderListView(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, OrderListActivity.class);
        startActivity(intent);
    }

    public void toOrderView(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, OrderActivity.class);
        startActivity(intent);
    }
    public void toRegisterView( View view){
        Intent intent = new Intent(MainMenuActivity.this, SigninActivity.class);
        startActivity(intent);
    }

}
