package ispy.corp.moneywzrd.investments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ispy.corp.moneywzrd.R;

public class Investment_fragment extends Fragment {

    private InvestmentFragmentViewModel mViewModel;

    public static Investment_fragment newInstance() {
        return new Investment_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.investment_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InvestmentFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}