package ispy.corp.moneywzrd.investments;
//ISpy Corp
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ispy.corp.moneywzrd.R;

import static ispy.corp.moneywzrd.R.string.errorfetch;

public class Tab1Charts extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView tableView;
    private TableAdapter adapter;
    public JSONObject obj;
    public JSONObject metaobj;
    private ImageButton favorite;
    private SharedPreferences sharedPreferences;
    private Context context;
    ProgressBar progressBar;
    boolean isFav;
    Button changeButton;
    WebView indicatorView;
    Spinner indicatorSpinner;
    String lastRendered = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //creates the local variables and sets view of activity
        final Context mycontext;
        mycontext = getActivity();
        View layout = inflater.inflate(R.layout.tab1charts, container, false);
        tableView =  layout.findViewById(R.id.pricetable);
        favorite = layout.findViewById(R.id.imageButton3);
        progressBar =  layout.findViewById(R.id.progressBar);
        indicatorView = layout.findViewById(R.id.indicatorView);
        indicatorSpinner = layout.findViewById(R.id.spinner);
        context = getActivity();

        isFav = ((AddStock)context).isFav;
        if(isFav) {
            favorite.setImageResource(R.drawable.filled);
        } else {
            favorite.setImageResource(R.drawable.star);
        }
        //this is very similar to stock activity it is calling the api once again
        final String symPassed = ((AddStock)getActivity()).message;
        String JsonURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symPassed+"&outputsize=full&apikey=7GFR6K8SGU99N55C";//cant extract this string API call would break
        if(!(((AddStock)getActivity()).isSet)) {
            ((AddStock)getActivity()).isSet = true;
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JsonURL, new Response.Listener<JSONObject>() {
                //setting the type of search results coming back to the user d
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        obj = response.getJSONObject(getString(R.string.timeSeries));
                        metaobj = response.getJSONObject(getString(R.string.Metadata));
                        progressBar.setVisibility(View.INVISIBLE);
                        tableView.setHasFixedSize(true);
                        tableView.setLayoutManager(new LinearLayoutManager(context));
                        tableView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
                        try {
                            ((AddStock)context).data.addAll(getData());
                            adapter = new TableAdapter(context, getData());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tableView.setAdapter(adapter);



                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                //any errors when searching with using the api will return this message
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mycontext, errorfetch, Toast.LENGTH_SHORT).show();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);
        } else {
            tableView.setHasFixedSize(true);
            if(progressBar!=null) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            tableView.setLayoutManager(new LinearLayoutManager(context));
            tableView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
            try {
                adapter = new TableAdapter(context, ((AddStock)context).data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tableView.setAdapter(adapter);
        }
        sharedPreferences = getActivity().getSharedPreferences(
                "favorite", Context.MODE_PRIVATE);
        favorite.setOnClickListener(new View.OnClickListener() {
           //shared prefs on favorite
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(!isFav) {
                    editor.putString(symPassed, symPassed);
                    favorite.setImageResource(R.drawable.filled);
                } else {
                    editor.remove(symPassed);
                    favorite.setImageResource(R.drawable.star);
                }
                editor.apply();
            }});
        return layout;
    }

    //this will parse the list with the information on the stock you search
    public List<TableRows> getData() throws JSONException {
        String symPassed = ((AddStock)getActivity()).message;
        int i;
        List<TableRows> data = new ArrayList<>();
        List<String> values = new ArrayList<String>();
        String[] heading={"Stock Symbol","Last Price","Change","Timestamp","Open","Close","Day's Range","Volume"};//cant extract these strings

        try {
            int count = 0;
            String currentKey="";
            String prevKey = "";
            Iterator<?> keys =  obj.keys();
            while( keys.hasNext() && count!=2){
                String key = (String) keys.next();
                if(count == 0){
                    currentKey = key;
                }
                else if(count == 1){
                    prevKey = key;
                }
                count++;
            }

            JSONObject obj1 = obj.getJSONObject(currentKey);
            JSONObject obj2 = obj.getJSONObject(prevKey);
            Float currclose = Float.valueOf(obj1.getString("4. close"));
            Float prevclose = Float.valueOf(obj2.getString("4. close"));
            Float change = ((currclose-prevclose)/prevclose)*100;
            String changeno = String.format("%.2f", change);
            Float changediff = currclose-prevclose;
            String change2diff = String.format("%.2f", changediff);
            String changetoadd = change2diff + "("+ changeno +"%)";
            String lastRefreshed = metaobj.getString("3. Last Refreshed");//cant extract this string
            lastRefreshed = lastRefreshed+ " 16:00:00 EDT";
            Float closePrice = Float.valueOf(obj1.getString("4. close"));//cant extract this string
            String closetoAdd = String.format("%.2f",closePrice);
            Float openPrice = Float.valueOf(obj1.getString("1. open"));//cant extract this string
            String opentoAdd = String.format("%.2f",openPrice);
            Float low = Float.valueOf(obj1.getString("3. low"));//cant extract this string
            String lowtoAdd = String.format("%.2f",low);
            Float high = Float.valueOf(obj1.getString("2. high"));//cant extract this string
            String hightoAdd = String.format("%.2f",high);
            String lowhigh = lowtoAdd+"-"+hightoAdd;
            String volume = obj1.getString("5. volume");//cant extract this string
            values.add(((AddStock)getActivity()).message);
            values.add(currclose.toString());
            values.add(changetoadd);
            values.add(lastRefreshed);
            values.add(opentoAdd);
            values.add(closetoAdd);
            values.add(lowhigh);
            values.add(volume);
            lastRendered = "Price";
            indicatorView.getSettings().setJavaScriptEnabled(true);
            indicatorView.setWebChromeClient(new WebChromeClient());
            indicatorView.setWebViewClient(new WebViewClient());
            indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highprice.php/?sym="+symPassed);//cant extract this string
            indicatorView.setVisibility(View.VISIBLE);
            changeButton.setTextColor(Color.parseColor("#808080"));//cant extract this string
            changeButton.setEnabled(false);

        }
        catch (Exception e){
        }
        for(i =0; i < values.size(); i++){
            TableRows current = new TableRows();
            current.title = values.get(i);
            current.header = heading[i];
            data.add(current);
        }
        return data;
    }

    //parse the text color
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        final TextView mytext = (TextView) view;

        if(mytext!=null) {
            if(lastRendered.equals(mytext.getText())){
                changeButton.setEnabled(false);
                changeButton.setTextColor(Color.parseColor("#808080"));//cant extract this string
            }
            else if(lastRendered.equals("")){
                changeButton.setEnabled(true);
                changeButton.setTextColor(Color.parseColor("#000000"));//cant extract this string
            }
            else {
                changeButton.setEnabled(true);
                changeButton.setTextColor(Color.parseColor("#000000"));//cant extract this string
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}
