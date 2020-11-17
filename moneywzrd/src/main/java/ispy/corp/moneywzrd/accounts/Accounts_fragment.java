package ispy.corp.moneywzrd.accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

        AccountN.setText(" " + pref.getString("AccountN", null));
        Account.setText("$" + pref.getString("Account", null));
        if ((AccountN.getText().toString().equals(" null") && Account.getText().toString().equals("$null"))) {
            CV.setVisibility(View.INVISIBLE);
        }
        else {
            CV.setVisibility(View.VISIBLE);
        }

        //do above for the rest of the expenses
        AccountN1.setText(" " + pref.getString("AccountN1", null));
        Account1.setText("$" + pref.getString("Account1", null));
        if ((AccountN1.getText().toString().equals(" null") && Account1.getText().toString().equals("$null"))) {
            CV1.setVisibility(View.INVISIBLE);
        }
        else {
            CV1.setVisibility(View.VISIBLE);
        }

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Account name and value");

                final EditText name = new EditText(getContext());
                final EditText value = new EditText(getContext());
                value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL);
                lila1.addView(name);
                lila1.addView(value);
                builder.setView(lila1);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (AccountN.getText().toString().equals("") && Account.getText().toString().equals("")) {
                            String i1 = name.getText().toString();
                            String Stext = value.getText().toString();
                            editor.putString("AccountN", i1);
                            editor.putString("Account", Stext);
                            editor.commit();
                            AccountN.setText(" " + pref.getString("AccountN", null));
                            Account.setText("$" + pref.getString("Account", null));
                            CV.setVisibility(View.VISIBLE);
                        }
                        else if (AccountN1.getText().toString().equals("") && Account1.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i2 = name.getText().toString();
                            String d2 = value.getText().toString();
                            editor.putString("AccountN1", i2);
                            editor.putString("Account1", d2);
                            editor.commit();
                            AccountN1.setText(" " + pref.getString("AccountN1", null));
                            Account1.setText("$" + pref.getString("Account1", null));
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
                AccountN.setText("");
                Account.setText("");
                editor.remove("AccountN");
                editor.remove("Account");
                editor.commit();
                CV.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_LONG).show();

            }
        });
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccountN1.setText("");
                Account1.setText("");
                editor.remove("AccountN1");
                editor.remove("Account1");
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