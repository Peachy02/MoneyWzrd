package ispy.corp.moneywzrd.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;

import static ispy.corp.moneywzrd.R.string.exclamation;
import static ispy.corp.moneywzrd.R.string.wentwrong;
import static ispy.corp.moneywzrd.R.string.successlog;

public class Splash_Activity extends AppCompatActivity { //ISPY CORP

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) { //This is used to display the splash screen for 4 seconds upon login before it redirects to the main app. It uses a image and progress bar, while displaying the users name
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(getString(R.string.userPath));
        userID = user.getUid();
        View loggy = findViewById(android.R.id.content);
        Snackbar.make(loggy,successlog, Snackbar.LENGTH_LONG).show();

        TextView welcome = (TextView)findViewById(R.id.welcomeMSG);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String wel = "Welcome ";
                String exc = getString(exclamation);
                User userprofile = snapshot.getValue(User.class);
                if (userprofile != null) {
                    String fullName = userprofile.fullName;
                    String email = userprofile.email;

                    welcome.setText(String.format("%s%s%s", wel, fullName, exc));
                }
                else {
                    welcome.setText(String.format("%s%s%s", wel, "User", exc));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Splash_Activity.this, wentwrong, Toast.LENGTH_LONG).show();
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 4000); // 4 seconds
    }
}