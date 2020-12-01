package ispy.corp.moneywzrd.accounts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.accounts.DAO.DAO;
import ispy.corp.moneywzrd.accounts.objects.Account;

public class AccountData extends AppCompatActivity {
    TextView nameet,valueet;
    String whichAccount;
    Button editbtn, deletebtn, backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getDelegate().setSupportActionBar(toolbar);
        getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //Values
        nameet = findViewById(R.id.received_name);
        valueet = findViewById(R.id.received_value);
        //Buttons
        editbtn = findViewById(R.id.editbtn);
        deletebtn = findViewById(R.id.deletebtn);
        backbtn = findViewById(R.id.backbtn);

        Intent intent = getIntent();
        //get which account was clicked
        whichAccount = intent.getStringExtra("name");

        nameet.setText(intent.getStringExtra("name"));
        valueet.setText(intent.getStringExtra("value"));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAccount();
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder confirmdel = new AlertDialog.Builder(AccountData.this);
                confirmdel.setTitle("Warning!");
                confirmdel.setMessage("Are you sure you want to delete " + whichAccount + "?");
                confirmdel.setCancelable(false);
                confirmdel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                });
                confirmdel.setNegativeButton("No", null);
                confirmdel.create().show();

            }
        });
    }
    private void editAccount() {
        DAO dao = new DAO(getApplicationContext());
        Account accountedit = new Account();
        accountedit.setName(nameet.getText().toString());
        accountedit.setValue(Integer.valueOf(valueet.getText().toString()));
        dao.insertAccount(accountedit, whichAccount);
        dao.close();
        finish();
    }
    private void deleteAccount() {
        DAO dao = new DAO(getApplicationContext());
        dao.deleteAccount(whichAccount);
        finish();
    }
}