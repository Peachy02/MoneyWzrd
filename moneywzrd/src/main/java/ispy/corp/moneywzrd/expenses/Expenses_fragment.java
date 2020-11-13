package ispy.corp.moneywzrd.expenses;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ispy.corp.moneywzrd.R;

public class Expenses_fragment extends Fragment { //brandon nicoll

    private ExpensesFragViewModel mViewModel;
    View rootView;

    public static Expenses_fragment newInstance() {
        return new Expenses_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.expenses_fragment, container, false);

        Button addExpenses = (Button)rootView.findViewById(R.id.addExpense);
        TextView expense1 = (TextView) rootView.findViewById(R.id.expense1);
        addExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter Expense");

                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (expense1.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            expense1.setText(input.getText().toString());
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

        return rootView;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button addExpenses = (Button)getActivity().findViewById(R.id.addExpense);
    }

     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExpensesFragViewModel.class);
        // TODO: Use the ViewModel
    }

}