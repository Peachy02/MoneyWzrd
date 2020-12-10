package ispy.corp.moneywzrd.investments;
//ISpy Corp
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import ispy.corp.moneywzrd.Login.Login_main;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.Settings_activity;
import static ispy.corp.moneywzrd.R.string.logged;


public class AddStock extends AppCompatActivity {
    //Declaration of variables
    private AddStock.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public String message;
    public boolean isSet;
    public List<TableRows> data = new ArrayList<>();
    public boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_string);

        Intent intent = getIntent();
        message = intent.getStringExtra(getString(R.string.MyData));
        isFav = intent.getBooleanExtra(getString(R.string.fav), false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    //grabs the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    //Builds the different pages and allows you to switch between them
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    Tab1Charts tab1 = new Tab1Charts();
                    return tab1;
                case 1:
                    Tab2History tab2 = new Tab2History();
                    return tab2;
                case 2:
                    Tab3News tab3 = new Tab3News();
                    return tab3;
                default:
                    return null;

            }
        }

        @Override
        //setting how many fragments there are see SectionsPagerAdapter
        public int getCount() {
            return 3;
        }

        //Sets the string names for the action bar fragments
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.Charts);
                case 1:
                    return getString(R.string.History);
                case 2:
                    return getString(R.string.NewsFeed);
            }
            return null;
        }
    }

    //This sets functionality for overflow sign out and settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out: {
                AlertDialog.Builder uSure = new AlertDialog.Builder(this);
                uSure.setTitle(R.string.logout);
                uSure.setMessage(R.string.Surelogout);
                uSure.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(AddStock.this, logged, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddStock.this, Login_main.class));

                    }
                });
                uSure.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = uSure.create();
                alert.show();
                break;
            }
            case R.id.Settingsbtn: {
                startActivity(new Intent(this, Settings_activity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}






