package ispy.corp.moneywzrd;



import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings_activity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings_activity);
        addPreferencesFromResource(R.xml.prefs);
    }
}