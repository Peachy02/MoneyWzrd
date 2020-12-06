package ispy.corp.moneywzrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import ispy.corp.moneywzrd.accounts.Accounts_fragment;
import ispy.corp.moneywzrd.expenses.Expenses_fragment;
import ispy.corp.moneywzrd.home.Home_fragment;
import ispy.corp.moneywzrd.investments.Investment_fragment;

import static ispy.corp.moneywzrd.R.string.ok;
import static ispy.corp.moneywzrd.R.string.cancel;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout ml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        ml = findViewById(R.id.mLaout);
        Load_settings();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new Home_fragment()).commit();

        }
    }

    private void Load_settings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT", false);
        if (chk_night){
            ml.setBackgroundColor(Color.parseColor("#404040"));

        }
        else {
            ml.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);

        }
        else if ("2".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
        else if ("3".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.Exitapp)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(cancel, null)
                .show();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onResume() {
        Load_settings();
        super.onResume();
    }

}






