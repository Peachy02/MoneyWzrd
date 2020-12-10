package ispy.corp.moneywzrd.investments;
//ISpy Corp
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.xmlpull.v1.XmlPullParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ispy.corp.moneywzrd.R;


public class Tab3News extends Fragment {
    private RecyclerView newsView;
    private NewsAdapter newsadapter;
    String myxmlResponse;


    //sets the view for news fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context mycontext;
        mycontext = this.getActivity();
        View layout = inflater.inflate(R.layout.tab3news, container, false);
        newsView = (RecyclerView) layout.findViewById(R.id.newstable);
        String symPassed = ((AddStock) getActivity()).message;
        String XmlURL = "http://demoapplication-env.us-east-2.elasticbeanstalk.com/?symbol=" + symPassed + "&indicator=XML"; //cant extract string here
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest req = new StringRequest(Request.Method.GET, XmlURL,
                new Response.Listener<String>() {
            //sets the dividers for the news items
            @Override
                    public void onResponse(String response) {
                        try {

                            myxmlResponse = response;
                            newsView.setHasFixedSize(true);
                            newsView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            newsView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                            newsadapter = new NewsAdapter(getActivity(), getData());
                            newsView.setAdapter(newsadapter);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                   //handles api error response
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error response
                        Toast.makeText(mycontext, R.string.errorfetch, Toast.LENGTH_SHORT).show();

                    }
                }
        );
        queue.add(req);


        return layout;
    }

        //this is grabbing the information on the setting it the current article page all articles come from seeking alpha (all stock news)
    public List<NewsInfo> getData() throws IOException, XmlPullParserException {
        int newscounter = 0;
        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
        List<StackOverflowXmlParser.Entry> entries = null;
        List<NewsInfo> newsdata = new ArrayList<>();
        List<String> ntitles = new ArrayList<>();
        List<String> nauthors = new ArrayList<>();
        List<String> nlinks = new ArrayList<>();
        List<String> ndates = new ArrayList<>();
        InputStream stream = new ByteArrayInputStream(myxmlResponse.getBytes(StandardCharsets.UTF_8.name()));
        entries = stackOverflowXmlParser.parse(stream);
        for (StackOverflowXmlParser.Entry entry : entries) {
            if (entry.link.startsWith("https://seekingalpha.com/article/")) { //cant extract string here
                nauthors.add(entry.author);
                ntitles.add(entry.title);
                String replacedStr = entry.date.replace("-0500", "EDT");//cant extract string here
                nlinks.add(entry.link);
                ndates.add(replacedStr);
            }

        }

        for (int i = 0; i < nauthors.size(); i++) {
            if (newscounter < 5) {
                NewsInfo currentnews = new NewsInfo();
                currentnews.nauthor = "Author: " + nauthors.get(i);//cant extract string here
                currentnews.ntitle = ntitles.get(i);
                currentnews.nlink = nlinks.get(i);

                currentnews.ndate = ndates.get(i);
                newsdata.add(currentnews);
                newscounter++;
            }
        }
        return newsdata;
    }
}


