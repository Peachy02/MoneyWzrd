package ispy.corp.moneywzrd.expenses;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ispy.corp.moneywzrd.R;

public class Expenses_fragment extends Fragment { //brandon nicoll

    private ExpensesFragViewModel mViewModel;

    public static Expenses_fragment newInstance() {
        return new Expenses_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expenses_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExpensesFragViewModel.class);
        // TODO: Use the ViewModel
    }

}