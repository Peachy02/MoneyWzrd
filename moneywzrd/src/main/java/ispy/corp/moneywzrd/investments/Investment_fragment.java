package ispy.corp.moneywzrd.investments;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import ispy.corp.moneywzrd.Login_main;
import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.Settings_activity;

import static ispy.corp.moneywzrd.R.string.Surelogout;
import static ispy.corp.moneywzrd.R.string.logged;
import static ispy.corp.moneywzrd.R.string.logout;
import static ispy.corp.moneywzrd.R.string.ok;
import static ispy.corp.moneywzrd.R.string.cancel;

public class Investment_fragment extends Fragment {


    private InvestmentFragmentViewModel mViewModel;
    View rootView;
    public static Investment_fragment newInstance() {
        return new Investment_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.investment_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InvestmentFragmentViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();



        View view = getView().findViewById(R.id.list_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickReorderButton(v);
            }
        });




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
    private void OnClickReorderButton(View v) {
        Intent intent = new Intent(getActivity(), Stock_edit.class);
        startActivity(intent);
    }


}