package ispy.corp.moneywzrd.investments;
//ISpy Corp
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ispy.corp.moneywzrd.R;



public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private LayoutInflater inflater;
    List<TableRows> data = Collections.emptyList();

    //sets the table adapter
    public TableAdapter(Context context, List<TableRows> data){
        inflater= LayoutInflater.from(context);
        this.data = data;
    }
    //inflates the view
    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_row, parent,false);
        TableViewHolder holder = new TableViewHolder(view);
        return holder;

    }

    //sets the view to bind the changes made to the table
    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {

        TableRows current = data.get(position);
        if (position == 2) {
            holder.title.setText(current.title);
            if(current.title.charAt(0) != '-') {
                holder.title.setTextColor(Color.parseColor("#32CD32"));//cant extract string here
                holder.imagetable.setImageResource(R.drawable.up);
            }
            else{
                holder.title.setTextColor(Color.parseColor("#FF0000"));//cant extract string here
                holder.imagetable.setImageResource(R.drawable.down);
            }
            holder.header.setText(current.header);
        }
        else{
            holder.title.setText(current.title);
            holder.header.setText(current.header);
        }



    }

    @Override
    public int getItemCount() {
        int dsize = data.size();
        return data.size();
    }
// initiates and sets the variables for the articles
    class TableViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView header;
        ImageView imagetable;
        public TableViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listtext);
            header = (TextView) itemView.findViewById(R.id.listhead);
            imagetable = (ImageView) itemView.findViewById(R.id.tableimage);

        }
    }
}
