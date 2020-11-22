package ispy.corp.moneywzrd.investments;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;

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

    private void OnClickReorderButton(View v) {
        Intent intent = new Intent(getActivity(), Stock_edit.class);
        startActivity(intent);
    }


}