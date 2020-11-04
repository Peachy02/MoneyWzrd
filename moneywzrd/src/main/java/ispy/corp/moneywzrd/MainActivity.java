package ispy.corp.moneywzrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ispy.corp.moneywzrd.accounts.Accounts_fragment;
import ispy.corp.moneywzrd.expenses.Expenses_fragment;
import ispy.corp.moneywzrd.home.Home_fragment;
import ispy.corp.moneywzrd.investments.Investment_fragment;
import ispy.corp.moneywzrd.settings.Settings_fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new Home_fragment()).commit();

        }
    }


        private BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.Homebtn:
                                selectedFragment = new Home_fragment();
                                break;
                            case R.id.AccBtn:
                                selectedFragment = new Accounts_fragment();
                                break;
                            case R.id.SetBtn:
                                selectedFragment = new Settings_fragment();
                                break;
                            case R.id.InvBtn:
                                selectedFragment = new Investment_fragment();
                                break;
                            case R.id.Expbtn:
                                selectedFragment = new Expenses_fragment();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                selectedFragment).commit();
                        return true;
                    }
                };
    }






