package ispy.corp.moneywzrd.accounts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.accounts.AccountData;
//ISpy Corp
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//Declaration of local variables
    Context context;
    List<String> names = new ArrayList<String>();
    String[] values;
    View viewOnCreate;
    ViewHolder ViewH;
//Adds the names values passed to the adapter as values in the RecyclerView
    public RecyclerViewAdapter(Context contextL, String[] namesL, String[] valuesL){
        context = contextL;
        names.addAll(Arrays.asList(namesL));
        values = valuesL;

    }
//Assign local textviews and imagebutton to the ones on the layout "rv_items"
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nametext;
        public TextView valuetext;
        public ImageButton edit;

        public ViewHolder(View itemView){
            super (itemView);
            nametext = itemView.findViewById(R.id.AccountN);
            valuetext = itemView.findViewById(R.id.Account);
            edit = itemView.findViewById(R.id.edit);
        }
    }

// Inflates a view holder with the cardview for the recyclerview list
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.rv_items, parent, false);
        ViewH = new ViewHolder(viewOnCreate);
        return ViewH;
    }

    //Handles the click event for the edit button, carrying the values to AccountData where they are handled
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.nametext.setText(names.get(position));
        holder.valuetext.setText(values[position]);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AccountData.class);
                intent.putExtra("name", names.get(position));
                intent.putExtra("value", values[position]);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }
//Counts the number of items in the Recyclerview
    @Override
    public int getItemCount() {
        return names.size();
    }
}
