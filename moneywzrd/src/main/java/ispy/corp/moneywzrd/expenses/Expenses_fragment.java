package ispy.corp.moneywzrd.expenses;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ispy.corp.moneywzrd.R;

public class Expenses_fragment extends Fragment { //brandon nicoll - n01338740

    private ExpensesFragViewModel mViewModel;
    View rootView;

    public static Expenses_fragment newInstance() {
        return new Expenses_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.expenses_fragment, container, false);
        final DatePickerDialog.OnDateSetListener[] mDateSetListener = {null};
        Button addExpenses = (Button)rootView.findViewById(R.id.addExpense);
        TextView expense1 = (TextView) rootView.findViewById(R.id.expense1);
        TextView date1 = (TextView) rootView.findViewById(R.id.date1);
        addExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter Expense");

                final EditText input1 = new EditText(getContext());
                final TextView input2 = new TextView(getContext());
                input2.setText(" Click to select due date");
                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL);
                lila1.addView(input1);
                lila1.addView(input2);
                builder.setView(lila1);

                input2.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                getContext(),
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener[0],
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
                mDateSetListener[0] = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;


                        String date = month + "/" + day + "/" + year;
                        input2.setText(date);
                    }
                };

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (expense1.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            expense1.setText(input1.getText().toString());
                            date1.setText("Due " + input2.getText().toString());
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