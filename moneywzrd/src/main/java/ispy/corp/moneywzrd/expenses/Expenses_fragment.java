package ispy.corp.moneywzrd.expenses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ispy.corp.moneywzrd.Login_main;
import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.Settings_activity;

import static ispy.corp.moneywzrd.R.string.ExpPaid;
import static ispy.corp.moneywzrd.R.string.logged;
import static ispy.corp.moneywzrd.R.string.mustenter;

public class Expenses_fragment extends Fragment { //brandon nicoll - n01338740

    View rootView;

    //SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit();

    public static Expenses_fragment newInstance() {
        return new Expenses_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.expenses_fragment, container, false);
        TextView msg = (TextView) rootView.findViewById(R.id.friendlymsg);
        msg.setText("");
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        SharedPreferences pref = rootView.getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        final DatePickerDialog.OnDateSetListener[] mDateSetListener = {null};
        Button addExpenses = (Button)rootView.findViewById(R.id.addExpense);
        Button clearExp = (Button)rootView.findViewById(R.id.clearExp);
        TextView expense1 = (TextView) rootView.findViewById(R.id.expense1);
        TextView date1 = (TextView) rootView.findViewById(R.id.date1);
        TextView expense2 = (TextView) rootView.findViewById(R.id.expense2);
        TextView date2 = (TextView) rootView.findViewById(R.id.date2);
        TextView expense3 = (TextView) rootView.findViewById(R.id.expense3);
        TextView date3 = (TextView) rootView.findViewById(R.id.date3);
        TextView price1 = (TextView) rootView.findViewById(R.id.price1);
        TextView price2 = (TextView) rootView.findViewById(R.id.price2);
        TextView price3 = (TextView) rootView.findViewById(R.id.price3);
        TextView totalE = (TextView)rootView.findViewById(R.id.totalE);

        CheckBox c1 = (CheckBox) rootView.findViewById(R.id.checkBox1);
        CheckBox c2 = (CheckBox) rootView.findViewById(R.id.checkBox2);
        CheckBox c3 = (CheckBox) rootView.findViewById(R.id.checkBox3);

        TextView do1 = (TextView)rootView.findViewById(R.id.dollar);
        TextView do2 = (TextView)rootView.findViewById(R.id.dollar2);
        TextView do3 = (TextView)rootView.findViewById(R.id.dollar3);

        setHasOptionsMenu(true);
        int total;


        //sets values if anything is saved
        expense1.setText(pref.getString("expense1", ""));
        date1.setText(pref.getString("date1", ""));
        price1.setText(pref.getString("price1", ""));
        if (price1.getText().toString().equals("")){
            price1.setText("0");
            price1.setVisibility(View.INVISIBLE);
        }
        if ((expense1.getText().toString().equals("") && date1.getText().toString().equals(""))) {
            c1.setVisibility(View.INVISIBLE);
            //msg.setVisibility(View.VISIBLE);
        }
        else {
            c1.setVisibility(View.VISIBLE);
        }

        //do above for the rest of the expenses
        expense2.setText(pref.getString("expense2", ""));
        date2.setText(pref.getString("date2", ""));
        price2.setText(pref.getString("price2", ""));
        if (price2.getText().toString().equals("")){
            price2.setText("0");
            price2.setVisibility(View.INVISIBLE);
        }
        if ((expense2.getText().toString().equals("") && date2.getText().toString().equals(""))) {
            c2.setVisibility(View.INVISIBLE);
        }
        else {
            c2.setVisibility(View.VISIBLE);
        }

        expense3.setText(pref.getString("expense3", ""));
        date3.setText(pref.getString("date3", ""));
        price3.setText(pref.getString("price3", ""));
        if (price3.getText().toString().equals("")){
            price3.setText("0");
            price3.setVisibility(View.INVISIBLE);
        }
        if ((expense3.getText().toString().equals("") && date3.getText().toString().equals(""))) {
            c3.setVisibility(View.INVISIBLE);
        }
        else {
            c3.setVisibility(View.VISIBLE);
        }
        if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
            msg.setText(R.string.noExp);
        }


        addExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.ExpEnt);

                final EditText input1 = new EditText(getContext());
                final TextView input2 = new TextView(getContext());
                final EditText price = new EditText(getContext());
                price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input2.setText(R.string.Duedate);
                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL);
                lila1.addView(input1);
                lila1.addView(input2);
                lila1.addView(price);
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
                //Strings for the notification buttons
                String ok = rootView.getResources().getString(R.string.ok);
                String cancel = rootView.getResources().getString(R.string.cancel);
                builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input1.getText().toString().isEmpty() || input2.getText().toString().equals("Click to select due date") || price.getText().toString().isEmpty() || price.getText().toString().equals("$")) {
                            Toast.makeText(getContext(), "MUST ENTER ALL REQUIRED FIELDS!", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                           // input1.setError("Cannot be empty!");
                           // input1.requestFocus();
                        }
                        /*if (input2.getText().toString().equals("Click to select due date")) {
                            input2.setError("Date required!");
                            input2.requestFocus();
                        }

                         */

                        else if (expense1.getText().toString().equals("") && date1.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i1 = input1.getText().toString();
                            String d1 = input2.getText().toString();
                            String p1 = price.getText().toString();
                            //expense1.setText(input1.getText().toString());
                            //date1.setText("Due " + input2.getText().toString());
                            editor.putString("expense1", i1);
                            editor.putString("date1", d1);
                            editor.putString("price1", p1);
                            editor.commit();
                            expense1.setText(pref.getString("expense1", null));
                            date1.setText(pref.getString("date1", null));
                            price1.setText(pref.getString("price1", null));
                            c1.setVisibility(View.VISIBLE);
                            do1.setVisibility(View.VISIBLE);
                            price1.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.INVISIBLE);
                            int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                            String tot = String.valueOf(total);
                            totalE.setText("Total Expenses: $" + tot);

                        }
                        else if (expense2.getText().toString().equals("") && date2.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i2 = input1.getText().toString();
                            String d2 = input2.getText().toString();
                            String p2 = price.getText().toString();
                            editor.putString("expense2", i2);
                            editor.putString("date2", d2);
                            editor.putString("price2", p2);
                            editor.commit();
                            expense2.setText(pref.getString("expense2", null));
                            date2.setText(pref.getString("date2", null));
                            price2.setText(pref.getString("price2", null));
                            c2.setVisibility(View.VISIBLE);
                            do2.setVisibility(View.VISIBLE);
                            price2.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.INVISIBLE);
                            int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                            String tot = String.valueOf(total);
                            totalE.setText("Total Expenses: $" + tot);
                        }
                        else if (expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                            //String input = input.getText().toString();
                            String i3 = input1.getText().toString();
                            String d3 = input2.getText().toString();
                            String p3 = price.getText().toString();
                            editor.putString("expense3", i3);
                            editor.putString("date3", d3);
                            editor.putString("price3", p3);
                            editor.commit();
                            expense3.setText(pref.getString("expense3", null));
                            date3.setText(pref.getString("date3", null));
                            price3.setText(pref.getString("price3", null));
                            c3.setVisibility(View.VISIBLE);
                            do3.setVisibility(View.VISIBLE);
                            price3.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.INVISIBLE);
                            int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                            String tot = String.valueOf(total);
                            totalE.setText("Total Expenses: $" + tot);
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

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder uSure = new AlertDialog.Builder(getContext());
                uSure.setTitle("Remove Expense");
                uSure.setMessage("Has this expense been paid?");
                uSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (c1.isChecked()) {
                            price1.setText("0");
                            price1.setVisibility(View.INVISIBLE);
                            editor.remove("expense1");
                            editor.remove("date1");
                            editor.remove("price1");
                            editor.commit();
                            expense1.setText("");
                            date1.setText("");
                            c1.setChecked(false);
                            c1.setVisibility(View.INVISIBLE);
                            do1.setVisibility(View.INVISIBLE);
                            int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                            String tot = String.valueOf(total);
                            totalE.setText("Total Expenses: $" + tot);
                            if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                                msg.setText(R.string.noExp);
                            }
                            Toast.makeText(getContext(), "Expense paid", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                uSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c1.setChecked(false);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = uSure.create();
                alert.show();

            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder uSure = new AlertDialog.Builder(getContext());
                uSure.setTitle("Remove Expense");
                uSure.setMessage("Has this expense been paid?");
                uSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (c2.isChecked()) {
                            price2.setText("0");
                            price2.setVisibility(View.INVISIBLE);
                            editor.remove("expense2");
                            editor.remove("date2");
                            editor.remove("price2");
                            editor.commit();
                            expense2.setText("");
                            date2.setText("");
                            c2.setChecked(false);
                            c2.setVisibility(View.INVISIBLE);
                            do2.setVisibility(View.INVISIBLE);
                            int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                            String tot = String.valueOf(total);
                            totalE.setText("Total Expenses: $" + tot);
                            if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                                msg.setText(R.string.noExp);
                            }
                            Toast.makeText(getContext(), ExpPaid, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                uSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c2.setChecked(false);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = uSure.create();
                alert.show();

            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder uSure = new AlertDialog.Builder(getContext());
                uSure.setTitle("Remove Expense");
                uSure.setMessage("Has this expense been paid?");
                uSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (c3.isChecked()) {
                            price3.setText("0");
                            price3.setVisibility(View.INVISIBLE);
                            editor.remove("expense3");
                            editor.remove("date3");
                            editor.remove("price3");
                            editor.commit();
                            expense3.setText("");
                            date3.setText("");
                            c3.setChecked(false);
                            c3.setVisibility(View.INVISIBLE);
                            do3.setVisibility(View.INVISIBLE);
                            int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                            String tot = String.valueOf(total);
                            totalE.setText("Total Expenses: $" + tot);
                            if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                                msg.setText(R.string.noExp);
                            }
                            Toast.makeText(getContext(), ExpPaid, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                uSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c3.setChecked(false);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = uSure.create();
                alert.show();

            }
        });
        clearExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder uSure = new AlertDialog.Builder(getContext());
                uSure.setTitle("Remove All Expenses");
                uSure.setMessage("Do you want to delete all your expenses?");
                uSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        price1.setText("0");
                        price1.setVisibility(View.INVISIBLE);
                        editor.remove("expense1");
                        editor.remove("date1");
                        editor.remove("price1");
                        c1.setChecked(false);
                        c1.setVisibility(View.INVISIBLE);
                        do1.setVisibility(View.INVISIBLE);
                        price2.setText("0");
                        price2.setVisibility(View.INVISIBLE);
                        editor.remove("expense2");
                        editor.remove("date2");
                        editor.remove("price2");
                        c2.setChecked(false);
                        c2.setVisibility(View.INVISIBLE);
                        do2.setVisibility(View.INVISIBLE);
                        price3.setText("0");
                        price3.setVisibility(View.INVISIBLE);
                        editor.remove("expense3");
                        editor.remove("date3");
                        editor.remove("price3");
                        editor.commit();
                        expense3.setText("");
                        date3.setText("");
                        expense2.setText("");
                        date2.setText("");
                        expense1.setText("");
                        date1.setText("");

                        c3.setChecked(false);
                        c3.setVisibility(View.INVISIBLE);
                        do3.setVisibility(View.INVISIBLE);

                        if (expense1.getText().toString().equals("") && date1.getText().toString().equals("") && expense2.getText().toString().equals("") && date2.getText().toString().equals("") && expense3.getText().toString().equals("") && date3.getText().toString().equals("")) {
                            msg.setText(R.string.noExp);
                        }

                        int total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
                        String tot = String.valueOf(total);
                        totalE.setText("Total Expenses: $" + tot);

                        Toast.makeText(getContext(), "All expenses cleared", Toast.LENGTH_LONG).show();
                    }
                });
                uSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = uSure.create();
                alert.show();


            }
        });

        total = Integer.parseInt(price1.getText().toString()) + Integer.parseInt(price2.getText().toString()) + Integer.parseInt(price3.getText().toString());
        String tot = String.valueOf(total);
        totalE.setText("Total Expenses: $" + tot);



        return rootView;
    }


    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button addExpenses = (Button)getActivity().findViewById(R.id.addExpense);
    }

     */

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
                uSure.setTitle("Logout");
                uSure.setMessage("Are you sure you want to logout?");
                uSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(), logged, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), Login_main.class));

                    }
                });
                uSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = uSure.create();
                alert.show();

            }
            case R.id.Settingsbtn: {
                startActivity(new Intent(getContext(), Settings_activity.class));
            }

        }
        return true;
    }


}