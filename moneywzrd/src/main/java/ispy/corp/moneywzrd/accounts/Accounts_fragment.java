package ispy.corp.moneywzrd.accounts;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;


public class Accounts_fragment extends Fragment {

    private AccountsFragViewModel mViewModel;
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

        SharedPreferences pref = V.getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        ImageButton addAccount = (ImageButton)V.findViewById(R.id.addAccount);
        ImageButton edit = (ImageButton)V.findViewById(R.id.edit);
        ImageButton edit1 = (ImageButton)V.findViewById(R.id.edit1);
        TextView AccountN = (TextView) V.findViewById(R.id.AccountN);
        TextView Account = (TextView) V.findViewById(R.id.Account);
        TextView AccountN1 = (TextView) V.findViewById(R.id.AccountN1);
        TextView Account1 = (TextView) V.findViewById(R.id.Account1);
        CardView CV = (CardView) V.findViewById(R.id.CV);
        CardView CV1 = (CardView) V.findViewById(R.id.CV1);

        //sets values if anything is saved
        Account.setText(pref.getString("Account", null));
        AccountN.setText(pref.getString("AccountN", null));
        if ((Account.getText().toString().equals("") && AccountN.getText().toString().equals(""))) {
            CV.setVisibility(View.INVISIBLE);
        }
        else {
            CV.setVisibility(View.VISIBLE);
        }

        //do above for the rest of the expenses
        Account1.setText(pref.getString("Account1", null));
        AccountN1.setText(pref.getString("AccountN1", null));
        if ((Account1.getText().toString().equals("") && AccountN1.getText().toString().equals(""))) {
            CV1.setVisibility(View.INVISIBLE);
        }
        else {
            CV1.setVisibility(View.VISIBLE);
        }

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Account value and name");

                final EditText input1 = new EditText(getContext());
                final EditText input2 = new EditText(getContext());
                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL);
                lila1.addView(input1);
                lila1.addView(input2);
                builder.setView(lila1);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Account.getText().toString().equals("") && AccountN.getText().toString().equals("")) {
                            String i1 = input1.getText().toString();
                            String d1 = input2.getText().toString();
                            editor.putString("Account", i1);
                            editor.putString("AccountN", d1);
                            editor.commit();
                            Account.setText(pref.getString("Account", null));
                            AccountN.setText(pref.getString("AccountN", null));
                            CV.setVisibility(View.VISIBLE);
                        }
                        else if (Account1.getText().toString().equals("") && AccountN1.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i2 = input1.getText().toString();
                            String d2 = input2.getText().toString();
                            editor.putString("Account1", i2);
                            editor.putString("AccountN1", d2);
                            editor.commit();
                            Account1.setText(pref.getString("Account1", null));
                            AccountN1.setText(pref.getString("AccountN1", null));
                            CV1.setVisibility(View.VISIBLE);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account.setText("");
                AccountN.setText("");
                editor.remove("Account");
                editor.remove("AccountN");
                editor.commit();
                CV.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_LONG).show();

            }
        });
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Account1.setText("");
                    AccountN1.setText("");
                    editor.remove("Account1");
                    editor.remove("AccountN1");
                    editor.commit();
                    CV1.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_LONG).show();

            }
        });



        return V;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountsFragViewModel.class);
        // TODO: Use the ViewModel
    }

}