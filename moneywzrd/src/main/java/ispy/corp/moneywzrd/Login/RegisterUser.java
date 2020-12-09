package ispy.corp.moneywzrd.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import ispy.corp.moneywzrd.R;

import static ispy.corp.moneywzrd.R.string.fail_regis;
import static ispy.corp.moneywzrd.R.string.usr_regis;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{ //ISPY CORP


    private TextView banner, registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword, pass2;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //defines objects within the activity for reference in the code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText)findViewById(R.id.fullname);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.password);
        pass2 = (EditText)findViewById(R.id.password2);
        progressBar = (ProgressBar)findViewById(R.id.progressBarR);
    }

    @Override
    public void onStart() { //at the start it will set the current user
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) { //switch case handling the clicks
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, Login_main.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() { //this method checks to see if the user has entered valid information and then creates a user account within the firebase database, once its done it will direct to login
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String password2 = pass2.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError(getString(R.string.name_required));
            editTextFullName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editTextEmail.setError(getString(R.string.email_required));
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getString(R.string.valid_email));
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.pass_required));
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.pass_min));
            editTextPassword.requestFocus();
            return;
        }
        if (password2.isEmpty()) {
            pass2.setError(getString(R.string.confirmpass));
            pass2.requestFocus();
            return;
        }
        if (!(password2.equals(password))) {
            pass2.setError(getString(R.string.Passnomatch));
            pass2.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    User user = new User(fullName, email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterUser.this, usr_regis, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterUser.this, Login_main.class));
                            }
                            else {
                                Toast.makeText(RegisterUser.this, fail_regis, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(RegisterUser.this, fail_regis, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }
}