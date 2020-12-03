package ispy.corp.moneywzrd;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.prefs.PreferenceChangeEvent;

public class Settings_activity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings_activity);
        addPreferencesFromResource(R.xml.prefs);
        Load_Setting();
    }


    private void Load_Setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT", false);
        if (chk_night){
            getListView().setBackgroundColor(Color.parseColor("#404040"));

        }
        else {
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }

        CheckBoxPreference chk_night_instant = (CheckBoxPreference) findPreference("NIGHT");
        chk_night_instant.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference prefs, Object obj) {
                boolean yes = (boolean)obj;

                if (yes){
                    getListView().setBackgroundColor(Color.parseColor("#404040"));
                }
                else {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }


                return true;
            }
        });

        ListPreference LP = (ListPreference) findPreference("ORIENTATION");
        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
            LP.setSummary(LP.getEntry());
        }
        else if ("2".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LP.setSummary(LP.getEntry());
        }
        else if ("3".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LP.setSummary(LP.getEntry());
        }

        LP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference prefs, Object obj) {

                String items = (String) obj;
                if (prefs.getKey().equals("ORIENTATION")) {
                    switch (items) {
                        case "1":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
                            break;
                        case "2":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "3":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }

                    ListPreference LPP = (ListPreference)prefs;
                    LPP.setSummary(LPP.getEntries()[LPP.findIndexOfValue(items)]);
                }

                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        Load_Setting();
        super.onResume();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Exit settings?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings_activity.this, MainActivity.class));
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

}