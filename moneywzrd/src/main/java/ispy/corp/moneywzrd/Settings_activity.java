package ispy.corp.moneywzrd;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Settings_activity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings_activity);
        addPreferencesFromResource(R.xml.prefs);
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