package ispy.corp.moneywzrd.home;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ispy.corp.moneywzrd.Login_main;
import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.Settings_activity;
import ispy.corp.moneywzrd.User;
import ispy.corp.moneywzrd.accounts.DAO.DAO;
import ispy.corp.moneywzrd.accounts.adapter.RecyclerViewAdapter;
import ispy.corp.moneywzrd.accounts.objects.Account;

import static ispy.corp.moneywzrd.R.string.logged;

public class Home_fragment extends Fragment {

    View rootView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    RecyclerView.Adapter rvAdap;
    RecyclerView.LayoutManager rvLayMan;
    RecyclerView rv;
    Context context;
    public static Home_fragment newInstance() {
        return new Home_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        context = rootView.getContext();
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setHasOptionsMenu(true);

        SharedPreferences pref = rootView.getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        rv = rootView.findViewById(R.id.rvh);
        context = rootView.getContext();
        ExtractDB();



        TextView msg = rootView.findViewById(R.id.friendlymsg);
        TextView eHome1 = (TextView)rootView.findViewById(R.id.eHome1);
        TextView eHome2 = (TextView)rootView.findViewById(R.id.eHome2);
        TextView eHome3 = (TextView)rootView.findViewById(R.id.eHome3);
        TextView eDate1 = (TextView)rootView.findViewById(R.id.eDate1);
        TextView eDate2 = (TextView)rootView.findViewById(R.id.eDate2);
        TextView eDate3 = (TextView)rootView.findViewById(R.id.eDate3);
        eHome1.setText(pref.getString("expense1", null));
        eHome2.setText(pref.getString("expense2", null));
        eHome3.setText(pref.getString("expense3", null));
        eDate1.setText(pref.getString("date1", null));
        eDate2.setText(pref.getString("date2", null));
        eDate3.setText(pref.getString("date3", null));

        if (eHome1.getText().toString().equals("") && eDate1.getText().toString().equals("") && eHome2.getText().toString().equals("") && eDate2.getText().toString().equals("") && eHome3.getText().toString().equals("") && eDate3.getText().toString().equals("")) {
            msg.setText(R.string.noExp);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        TextView welcome = (TextView)rootView.findViewById(R.id.welcomeName);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String wel = rootView.getResources().getString(R.string.welc);
                String exc = rootView.getResources().getString(R.string.exclamation);
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
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out: {
                AlertDialog.Builder uSure = new AlertDialog.Builder(getContext());
                uSure.setTitle("Logout");
                uSure.setMessage("Are you sure you want to logout?");
                uSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(), logged, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), Login_main.class));

                    }
                });
                uSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    private void ExtractDB() {
        DAO dao2 = new DAO(getActivity().getApplicationContext());

        List<Account> accounts = dao2.searchAccount();

        List<String> names = new ArrayList<String>();
        List<String> values = new ArrayList<String>();

        String[] data_names = new String[] {};
        String[] data_values = new String[] {};

        for(Account nameSearched : accounts){
            names.add(nameSearched.getName());
            values.add(String.valueOf(nameSearched.getValue()));
        }

        data_names = names.toArray(new String[0]);
        data_values = values.toArray(new String[0]);

        rvLayMan = new LinearLayoutManager(context);
        rv.setLayoutManager(rvLayMan);
        rvAdap = new RecyclerViewAdapter(context,data_names, data_values);
        rv.setAdapter(rvAdap);
    }

}