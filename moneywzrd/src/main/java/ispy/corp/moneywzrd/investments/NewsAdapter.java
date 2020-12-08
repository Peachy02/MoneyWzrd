package ispy.corp.moneywzrd.investments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ispy.corp.moneywzrd.R;

/**
 * Created by poojadeole on 11/21/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private  final LayoutInflater inflator;
    List<NewsInfo> newsdata = Collections.emptyList();
    Context context;

    public NewsAdapter(Context context, List<NewsInfo> newsdata){
        inflator = LayoutInflater.from(context);
        this.context = context;
        this.newsdata = newsdata;

    }



    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_news,parent,false);
        NewsViewHolder holder = new NewsViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final NewsInfo currentNews = newsdata.get(position);
        holder.ntitle.setText(currentNews.ntitle);
        holder.nauthor.setText(currentNews.nauthor);
        holder.ndate.setText(currentNews.ndate);
        holder.ntitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("link",currentNews.nlink);
                String url = currentNews.nlink;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }});
    }

    @Override
    public int getItemCount() {
        return newsdata.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView ntitle;
        TextView nauthor;
        TextView ndate;
        public NewsViewHolder(View itemView) {
            super(itemView);
            ntitle = (TextView) itemView.findViewById(R.id.newstitle);
            ntitle.setClickable(true);
            ntitle.setMovementMethod(LinkMovementMethod.getInstance());
            nauthor = (TextView) itemView.findViewById(R.id.newsauthor);
            ndate =  (TextView) itemView.findViewById(R.id.newsdate);
        }
    }
}
