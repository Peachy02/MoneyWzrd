package ispy.corp.moneywzrd.home;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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

import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.User;

public class Home_fragment extends Fragment {

    private HomeFragmentViewModel mViewModel;
    View rootView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    public static Home_fragment newInstance() {
        return new Home_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        SharedPreferences pref = rootView.getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        CardView CV = (CardView) rootView.findViewById(R.id.CVH);
        CardView CV1 = (CardView) rootView.findViewById(R.id.CVH1);
        TextView AccountN = rootView.findViewById(R.id.AccountN);
        TextView Account = rootView.findViewById(R.id.Account);
        TextView AccountN1 = rootView.findViewById(R.id.AccountN1);
        TextView Account1 = rootView.findViewById(R.id.Account1);
        AccountN.setText(" " + pref.getString("AccountN",null));
        Account.setText("$"+pref.getString("Account",null));
        AccountN1.setText(" "+pref.getString("AccountN1",null));
        Account1.setText("$" + pref.getString("Account1",null));

        if ((AccountN.getText().toString().equals(" null") && Account.getText().toString().equals("$null"))) {
            CV.setVisibility(View.INVISIBLE);
        }
        else {
            CV.setVisibility(View.VISIBLE);
        }

        if ((AccountN1.getText().toString().equals(" null") && Account1.getText().toString().equals("$null"))) {
            CV1.setVisibility(View.INVISIBLE);
        }
        else {
            CV1.setVisibility(View.VISIBLE);
        }

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
            msg.setText("You have no current expenses!");
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        TextView welcome = (TextView)rootView.findViewById(R.id.welcomeName);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if (userprofile != null) {
                    String fullName = userprofile.fullName;
                    String email = userprofile.email;

                    welcome.setText("Welcome back, " + fullName + "!");
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}