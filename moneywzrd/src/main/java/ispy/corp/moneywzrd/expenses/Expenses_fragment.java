package ispy.corp.moneywzrd.expenses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
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

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;

public class Expenses_fragment extends Fragment { //brandon nicoll - n01338740

    private ExpensesFragViewModel mViewModel;
    View rootView;

    //SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit();

    public static Expenses_fragment newInstance() {
        return new Expenses_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.expenses_fragment, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        SharedPreferences pref = rootView.getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        final DatePickerDialog.OnDateSetListener[] mDateSetListener = {null};
        Button addExpenses = (Button)rootView.findViewById(R.id.addExpense);
        TextView expense1 = (TextView) rootView.findViewById(R.id.expense1);
        TextView date1 = (TextView) rootView.findViewById(R.id.date1);
        TextView expense2 = (TextView) rootView.findViewById(R.id.expense2);
        TextView date2 = (TextView) rootView.findViewById(R.id.date2);
        TextView expense3 = (TextView) rootView.findViewById(R.id.expense3);
        TextView date3 = (TextView) rootView.findViewById(R.id.date3);
        TextView msg = (TextView) rootView.findViewById(R.id.friendlymsg);
        CheckBox c1 = (CheckBox) rootView.findViewById(R.id.checkBox1);
        CheckBox c2 = (CheckBox) rootView.findViewById(R.id.checkBox2);
        CheckBox c3 = (CheckBox) rootView.findViewById(R.id.checkBox3);

        msg.setVisibility(View.INVISIBLE);
        if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
            msg.setVisibility(View.VISIBLE);
        }
        //sets values if anything is saved
        expense1.setText(pref.getString("expense1", null));
        date1.setText(pref.getString("date1", null));
        if ((expense1.getText().toString().equals("") && date1.getText().toString().equals(""))) {
            c1.setVisibility(View.INVISIBLE);
            //msg.setVisibility(View.VISIBLE);
        }
        else {
            c1.setVisibility(View.VISIBLE);
        }

        //do above for the rest of the expenses
        expense2.setText(pref.getString("expense2", null));
        date2.setText(pref.getString("date2", null));
        if ((expense2.getText().toString().equals("") && date2.getText().toString().equals(""))) {
            c2.setVisibility(View.INVISIBLE);
        }
        else {
            c2.setVisibility(View.VISIBLE);
        }

        expense3.setText(pref.getString("expense3", null));
        date3.setText(pref.getString("date3", null));
        if ((expense3.getText().toString().equals("") && date3.getText().toString().equals(""))) {
            c3.setVisibility(View.INVISIBLE);
        }
        else {
            c3.setVisibility(View.VISIBLE);
        }



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

                        if (input1.getText().toString().isEmpty() || input2.getText().toString().equals(" Click to select due date")) {
                            Toast.makeText(getContext(), "*MUST ENTER BOTH REQUIRED FIELDS*", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }

                        else if (expense1.getText().toString().equals("") && date1.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i1 = input1.getText().toString();
                            String d1 = input2.getText().toString();
                            //expense1.setText(input1.getText().toString());
                            //date1.setText("Due " + input2.getText().toString());
                            editor.putString("expense1", i1);
                            editor.putString("date1", d1);
                            editor.commit();
                            expense1.setText(pref.getString("expense1", null));
                            date1.setText(pref.getString("date1", null));
                            c1.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.INVISIBLE);
                        }
                        else if (expense2.getText().toString().equals("") && date2.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i2 = input1.getText().toString();
                            String d2 = input2.getText().toString();
                            editor.putString("expense2", i2);
                            editor.putString("date2", d2);
                            editor.commit();
                            expense2.setText(pref.getString("expense2", null));
                            date2.setText(pref.getString("date2", null));
                            c2.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.INVISIBLE);
                        }
                        else if (expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i3 = input1.getText().toString();
                            String d3 = input2.getText().toString();
                            editor.putString("expense3", i3);
                            editor.putString("date3", d3);
                            editor.commit();
                            expense3.setText(pref.getString("expense3", null));
                            date3.setText(pref.getString("date3", null));
                            c3.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.INVISIBLE);
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

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()) {
                    expense1.setText("");
                    date1.setText("");
                    editor.remove("expense1");
                    editor.remove("date1");
                    editor.commit();
                    c1.setChecked(false);
                    c1.setVisibility(View.INVISIBLE);
                    if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                        msg.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getContext(), "Expense paid", Toast.LENGTH_LONG).show();
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c2.isChecked()) {
                    expense2.setText("");
                    date2.setText("");
                    editor.remove("expense2");
                    editor.remove("date2");
                    editor.commit();
                    c2.setChecked(false);
                    c2.setVisibility(View.INVISIBLE);
                    if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                        msg.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getContext(), "Expense paid", Toast.LENGTH_LONG).show();
                }
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c3.isChecked()) {
                    expense3.setText("");
                    date3.setText("");
                    editor.remove("expense3");
                    editor.remove("date3");
                    editor.commit();
                    c3.setChecked(false);
                    c3.setVisibility(View.INVISIBLE);
                    if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                        msg.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getContext(), "Expense paid", Toast.LENGTH_LONG).show();
                }
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