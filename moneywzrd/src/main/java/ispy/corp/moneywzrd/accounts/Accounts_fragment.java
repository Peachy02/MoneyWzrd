package ispy.corp.moneywzrd.accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import ispy.corp.moneywzrd.Login.Login_main;
import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.Settings_activity;
import ispy.corp.moneywzrd.accounts.DAO.DAO;
import ispy.corp.moneywzrd.accounts.adapter.RecyclerViewAdapter;
import ispy.corp.moneywzrd.accounts.objects.Account;

import static ispy.corp.moneywzrd.R.string.Surelogout;
import static ispy.corp.moneywzrd.R.string.cancel;
import static ispy.corp.moneywzrd.R.string.logged;
import static ispy.corp.moneywzrd.R.string.logout;
import static ispy.corp.moneywzrd.R.string.ok;
import static ispy.corp.moneywzrd.R.string.Mustenterfields;

//ISpy Corp
//Screen on the main app for accounts
public class Accounts_fragment extends Fragment {

    Context context;
    RecyclerView.Adapter rvAdap;
    RecyclerView.LayoutManager rvLayMan;
    RecyclerView rv;
    View V;
    public static Accounts_fragment newInstance() {
        return new Accounts_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.accounts_fragment, container, false);

        Toolbar toolbar = (Toolbar) V.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageButton addAccount = (ImageButton)V.findViewById(R.id.addAccount);
        setHasOptionsMenu(true);
        TextView noacc = V.findViewById(R.id.textView8);

        rv = V.findViewById(R.id.rv);
        context = V.getContext();
        ExtractDB();
        noacc(noacc);
        //Plus button handling to add a new account
        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.accnandvalue);

                final EditText name = new EditText(getContext());
                final EditText value = new EditText(getContext());
                value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL);
                lila1.addView(name);
                lila1.addView(value);
                builder.setView(lila1);
                String ok = V.getResources().getString(R.string.ok);
                String cancel = V.getResources().getString(R.string.cancel);

                builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!(name.getText().toString().equals("") || value.getText().toString().equals(""))){
                            insertAccount(name, value);

                            ExtractDB();
                            noacc(noacc);
                        }
                        else{
                            Toast.makeText(V.getContext(),Mustenterfields, Toast.LENGTH_SHORT).show();
                        }




                    }
                });
                builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return V;
    }
    //Set the textview with the message that you have no accounts to invisible if there are no items in the recyclerview
    private void noacc(TextView noacc) {
        if (rvAdap.getItemCount() == 0)
        {
            noacc.setVisibility(View.VISIBLE);
        }
        else
        {
            noacc.setVisibility(View.GONE);
        }
    }
    //Inflates the overflow menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    //Inserts the account name and value gathered from the user into the database
    private void insertAccount(EditText name, EditText value) {
        DAO dao = new DAO(getActivity().getApplicationContext());
        Account account = new Account();
        account.setName(name.getText().toString());
        account.setValue(Integer.valueOf(value.getText().toString()));
        dao.insertAccount(account, null);
        dao.close();
    }
    //Handles the button clicks in the overflow menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out: {
                AlertDialog.Builder uSure = new AlertDialog.Builder(getContext());
                uSure.setTitle(logout);
                uSure.setMessage(Surelogout);
                uSure.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(), logged, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), Login_main.class));

                    }
                });
                uSure.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
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
                startActivity(new Intent(getContext(), Settings_activity.class));
                break;
            }

        }
        return true;
    }
    //Extracts the database to fill up the recyclerview
    public void ExtractDB() {
        DAO dao2 = new DAO(getActivity().getApplicationContext());

        List<Account> accounts = dao2.searchAccount();

        List<String> names = new ArrayList<String>();
        List<String> values = new ArrayList<String>();

        String[] data_names = new String[] {};
        String[] data_values = new String[] {};

        for(Account nameSearched : accounts){
            names.add(nameSearched.getName());
            values.add("$" + String.valueOf(nameSearched.getValue()));
        }

        data_names = names.toArray(new String[0]);
        data_values = values.toArray(new String[0]);

        rvLayMan = new LinearLayoutManager(context);
        rv.setLayoutManager(rvLayMan);
        rvAdap = new RecyclerViewAdapter(context,data_names, data_values);
        rv.setAdapter(rvAdap);
    }
    //Extracts the database even when you add a new one
    @Override
    public void onResume(){
        super.onResume();
        ExtractDB();
    }

}