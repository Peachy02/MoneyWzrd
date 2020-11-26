package ispy.corp.moneywzrd.accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.accounts.DAO.DAO;
import ispy.corp.moneywzrd.accounts.adapter.RecyclerViewAdapter;
import ispy.corp.moneywzrd.accounts.objects.Account;


public class Accounts_fragment extends Fragment {

    Context context;
    LinearLayout linlay;
    RecyclerView.Adapter rvAdap;
    RecyclerView.LayoutManager rvLayMan;
    RecyclerView rv;

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
        ImageButton addAccount = (ImageButton)V.findViewById(R.id.addAccount);
        TextView AccountN = (TextView) V.findViewById(R.id.AccountN);
        TextView Account0 = (TextView) V.findViewById(R.id.Account);
        TextView AccountN1 = (TextView) V.findViewById(R.id.AccountN1);
        TextView Account1 = (TextView) V.findViewById(R.id.Account1);
        rv = V.findViewById(R.id.rv);
        context = V.getContext();
        ExtractDB();



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
                            DAO dao = new DAO(getActivity().getApplicationContext());
                            Account account = new Account();
                            account.setName(name.getText().toString());
                            account.setValue(Integer.valueOf(value.getText().toString()));
                            dao.insertAccount(account);
                            dao.close();

                            ExtractDB();
                        }
                        else{
                            Toast.makeText(V.getContext(),"Please fill all the fields.", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountsFragViewModel.class);
        // TODO: Use the ViewModel
    }

}