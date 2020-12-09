package ispy.corp.moneywzrd.accounts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.accounts.DAO.DAO;
import ispy.corp.moneywzrd.accounts.objects.Account;

import static ispy.corp.moneywzrd.R.string.cancel;
import static ispy.corp.moneywzrd.R.string.ok;

//ISpy Corp
//Activity that opens from the editbutton in accounts fragment
public class AccountData extends AppCompatActivity {
    TextView nameet,valueet;
    String whichAccount;
    Button editbtn, deletebtn, backbtn;

    //Creates the layout for AccountData
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
        //sets the textviews to the values gathered from the strings in the table
        nameet.setText(intent.getStringExtra("name"));
        valueet.setText(intent.getStringExtra("value"));
        String display = valueet.getText().toString();

        if(!TextUtils.isEmpty(display)) {
            display  = display.substring(1);

            valueet.setText(display);
        }
        //Handles back button press
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //handles edit button press
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAccount();
            }
        });
        //handles delete button press
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shows an alert dialog to confirm deletion, if confirmed it deletes that entry in the table in the database
                AlertDialog.Builder confirmdel = new AlertDialog.Builder(AccountData.this);
                confirmdel.setTitle(R.string.warn);
                confirmdel.setMessage(getString(R.string.suredelet) + whichAccount + getString(R.string.questionmark));
                confirmdel.setCancelable(false);
                confirmdel.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                });
                confirmdel.setNegativeButton(cancel, null);
                confirmdel.create().show();

            }
        });
    }
    //Edits the account according to the one selected on the recyclerView, using the name passed from it to define the entry
    private void editAccount() {
        DAO dao = new DAO(getApplicationContext());
        Account accountedit = new Account();
        accountedit.setName(nameet.getText().toString());
        accountedit.setValue(Integer.valueOf(valueet.getText().toString()));
        dao.insertAccount(accountedit, whichAccount);
        dao.close();
        finish();
    }
    //Deletes the account using the selected entry to choose which one
    private void deleteAccount() {
        DAO dao = new DAO(getApplicationContext());
        dao.deleteAccount(whichAccount);
        finish();
    }
}