package ispy.corp.moneywzrd.investments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ispy.corp.moneywzrd.R;

public class Stock_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_edit);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Context context = this;

        View button = findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Addstock.class);
                startActivity(intent);
            }
        });

    }




}