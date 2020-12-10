package ispy.corp.moneywzrd.investments;
//ISpy Corp
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ispy.corp.moneywzrd.R;



public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {
   //setting class variables
    private LayoutInflater inflator;
    List<FavInfo> favdata;
    private Context context;


    public FavAdapter(Context context, List<FavInfo> favdata){
        inflator = LayoutInflater.from(context);
        this.context = context;
        this.favdata = favdata;
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflator.inflate(R.layout.custom_fav,parent,false);
        FavViewHolder holder = new FavViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        final FavInfo current = favdata.get(position);
        holder.fsymbol.setText(current.favsym);
        holder.fprice.setText(current.favprice);
        holder.fchange.setText(current.favchange);
        if(current.favchange.charAt(0)!='-'){
            holder.fchange.setTextColor(Color.parseColor("#32CD32")); //not allowed to extract colour resource string
                                }
                               else{
            holder.fchange.setTextColor(Color.parseColor("#FF0000"));//not allowed to extract colour resource string
                                }
        holder.favRow.setOnLongClickListener(new View.OnLongClickListener() {

            //this gives you the option to remove your favorite stocks from the main screen
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.removefav);
                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ((StockActivity)context).removeFav(current.favsym);
                        dialog.dismiss();
                        Toast.makeText(context, "Selected Item Deleted", Toast.LENGTH_SHORT).show(); //not allowed to extract toast string here
                    }
                });
                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        holder.favRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddStock.class);
                intent.putExtra("my_data", current.favsym); //cant extract resource
                intent.putExtra("favorite", true);//cant extract resource
                context.startActivity(intent);
            }
        });

    }

    //this is the code to remove favorite from the specific stock screen
    @Override
    public int getItemCount() {
        return favdata.size();
    }
    public void removeFav(String str) {
        for(FavInfo fav : favdata) {
            if(fav.favsym.equals(str)) {
                favdata.remove(fav);
                notifyDataSetChanged();
                break;
            }
        }
    }
//this will clear the data from the main screen if you unfavorable a stock
    public void clearData() {
        favdata.clear();
        notifyDataSetChanged();
    }
    //this will add the stock to the main stock screen
    public void addFav(FavInfo favInfo) {
        favdata.add(favInfo);
        notifyDataSetChanged();
    }


    //this method will use the spinners to set the organization of the stock list
    public void sort(String sort, String order) {
        if(sort.equals(R.string.symbol) && order.equals("Ascending")) {//cant extract this string
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return o1.favsym.compareTo(o2.favsym);
                }
            });
        } else if(sort.equals(R.string.symbol) && order.equals("Descending")) {//cant extract this string
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return o2.favsym.compareTo(o1.favsym);
                }
            });
        } else if(sort.equals(R.string.price) && order.equals("Ascending")) {//cant extract this string
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return Math.round(Float.parseFloat(o1.favprice)- (Float.parseFloat(o2.favprice)));
                }
            });
        } else if(sort.equals(R.string.price) && order.equals("Descending")) {//cant extract this string
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return Math.round(Float.parseFloat(o2.favprice)- Float.parseFloat(o1.favprice));
                }
            });
        } else if(sort.equals(R.string.change) && order.equals("Ascending")) {//cant extract this string
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    String a[] = o1.favchange.split("\\(");
                    String b[] = o2.favchange.split("\\(");
                    return Math.round(Float.parseFloat(a[0]) - Float.parseFloat(b[0]));
                }
            });
        } else if(sort.equals(R.string.change) && order.equals("Descending")) {//cant extract this string
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    String a[] = o1.favchange.split("\\(");
                    String b[] = o2.favchange.split("\\(");
                    return Math.round(Float.parseFloat(b[0]) - Float.parseFloat(a[0]));
                }
            });
        }
        notifyDataSetChanged();
    }

    //this will set the id for view of dependant sort method eg price, symbol, change
    class FavViewHolder extends RecyclerView.ViewHolder{
        TextView fsymbol;
        TextView fprice;
        TextView fchange;
        LinearLayout favRow;
        public FavViewHolder(View itemView){
            super(itemView);
            favRow = itemView.findViewById(R.id.favRow);
            fsymbol = itemView.findViewById(R.id.favSym);
            fprice = itemView.findViewById(R.id.favPrice);
            fchange = itemView.findViewById(R.id.favChange);
        }
    }
}
