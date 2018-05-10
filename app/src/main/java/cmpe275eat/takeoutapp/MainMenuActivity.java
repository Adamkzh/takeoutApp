package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeMenu homeMenu = new HomeMenu();
                    manager.beginTransaction().replace(R.id.navigation_notifications, homeMenu).commit();
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(MainMenuActivity.this,LogoutActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        FragmentManager manager = getSupportFragmentManager();
        HomeMenu homeMenu = new HomeMenu();
        manager.beginTransaction().replace(R.id.navigation_notifications, homeMenu).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
