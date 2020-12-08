package ispy.corp.moneywzrd.investments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ispy.corp.moneywzrd.R;

public class Tab1Charts extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView tableView;
    private TableAdapter adapter;
    public JSONObject obj;
    public JSONObject metaobj;
    private ImageButton favorite;
    private SharedPreferences sharedPreferences;
    private Context context;
    Spinner spinner;
    ProgressBar progressBar;
    boolean isFav;
    Button changeButton;
    WebView indicatorView;
    Spinner indicatorSpinner;
    ImageButton fbButton;
    ShareDialog shareDialog;
    String lastRendered = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context mycontext;
        mycontext = getActivity();


        View layout = inflater.inflate(R.layout.tab1charts, container, false);
        tableView = (RecyclerView) layout.findViewById(R.id.pricetable);
        favorite = (ImageButton) layout.findViewById(R.id.imageButton3);
        progressBar =  (ProgressBar) layout.findViewById(R.id.progressBar);
        indicatorView =(WebView) layout.findViewById(R.id.indicatorView);
        indicatorSpinner =(Spinner) layout.findViewById(R.id.spinner);
        fbButton =(ImageButton) layout.findViewById(R.id.fbButton);
        context = getActivity();
        changeButton = (Button) layout.findViewById(R.id.indicatorButton);
        isFav = ((AddStock)context).isFav;
        if(isFav) {
            favorite.setImageResource(R.drawable.filled);
        } else {
            favorite.setImageResource(R.drawable.star);
        }
        final String symPassed = ((AddStock)getActivity()).message;
        Log.d("Symbol",symPassed);
        String JsonURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symPassed+"&outputsize=full&apikey=7GFR6K8SGU99N55C";
        Log.d("Making request again",symPassed);
        Log.d("pooja","outside");
        spinner = (Spinner) layout.findViewById(R.id.spinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this.context,R.array.indicators,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        if(!(((AddStock)getActivity()).isSet)) {
            Log.d("pooja","called");
            ((AddStock)getActivity()).isSet = true;
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JsonURL, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        obj = response.getJSONObject("Time Series (Daily)");
                        metaobj = response.getJSONObject("Meta Data");
                        Log.d("Reply","Getting respone");
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

                        Log.d("TAG", "hello");

                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mycontext,"Error while fetching API", Toast.LENGTH_SHORT).show();
                    Log.e("Volley", "Error11" + error.toString());


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

        shareDialog = new ShareDialog(getActivity());
        fbButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String renderMe = lastRendered.toLowerCase();
                Log.d("fbButton","FaceBook button");
                Bitmap image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/yahoo.jpg",new BitmapFactory.Options());
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("http://www-scf.usc.edu/~deole/high"+renderMe+".php/?sym="+symPassed))
                            .build();

                    shareDialog.show(content);
                }



            }
        });



        return layout;
    }

    public List<TableRows> getData() throws JSONException {
        int i;
        List<TableRows> data = new ArrayList<>();
        List<String> values = new ArrayList<String>();
        String[] heading={"Stock Symbol","Last Price","Change","Timestamp","Open","Close","Day's Range","Volume"};

        try {
            int count = 0;
            String currentKey="";
            String prevKey = "";
            Iterator<?> keys =  obj.keys();
            while( keys.hasNext() && count!=2){
                String key = (String) keys.next();
                if(count == 0){
                    currentKey = key;
                    Log.d("jsonkey",currentKey);
                }
                else if(count == 1){
                    prevKey = key;
                    Log.d("jsonkey",prevKey);
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
            String lastRefreshed = metaobj.getString("3. Last Refreshed");
            lastRefreshed = lastRefreshed+ " 16:00:00 EDT";
            Float closePrice = Float.valueOf(obj1.getString("4. close"));
            String closetoAdd = String.format("%.2f",closePrice);
            Float openPrice = Float.valueOf(obj1.getString("1. open"));
            String opentoAdd = String.format("%.2f",openPrice);
            Float low = Float.valueOf(obj1.getString("3. low"));
            String lowtoAdd = String.format("%.2f",low);
            Float high = Float.valueOf(obj1.getString("2. high"));
            String hightoAdd = String.format("%.2f",high);
            String lowhigh = lowtoAdd+"-"+hightoAdd;
            String volume = obj1.getString("5. volume");
            values.add(((AddStock)getActivity()).message);
            values.add(currclose.toString());
            values.add(changetoadd);
            values.add(lastRefreshed);
            values.add(opentoAdd);
            values.add(closetoAdd);
            values.add(lowhigh);
            values.add(volume);

        }
        catch (Exception e){
            Log.d("JsonError","Error!!!");
        }

        for(i =0; i < values.size(); i++){
            TableRows current = new TableRows();
            current.title = values.get(i);
            current.header = heading[i];
            data.add(current);
        }

        return data;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        final TextView mytext = (TextView) view;

        if(mytext!=null) {


            if(lastRendered.equals(mytext.getText())){
                changeButton.setEnabled(false);
                changeButton.setTextColor(Color.parseColor("#808080"));
            }
            else if(lastRendered.equals("")){
                changeButton.setEnabled(true);
                changeButton.setTextColor(Color.parseColor("#000000"));
            }
            else {
                changeButton.setEnabled(true);
                changeButton.setTextColor(Color.parseColor("#000000"));
            }


            changeButton.setOnClickListener(new View.OnClickListener(){
                String symPassed = ((AddStock)getActivity()).message;
                public void onClick(View v){
                    Log.d("change","change clicked");
                    String indicatorText = indicatorSpinner.getSelectedItem().toString();
                    Log.d("indicator text",indicatorText);

                    if(indicatorText.equals("SMA")){
                        lastRendered = "SMA";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highsma.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);


                    }
                    else if(indicatorText.equals("EMA")){
                        lastRendered = "EMA";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highema.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }
                    else if(indicatorText.equals("STOCH")){
                        lastRendered = "STOCH";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highstoch.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);

                    }
                    else if(indicatorText.equals("BBANDS")){
                        lastRendered = "BBANDS";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highbbands.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }
                    else if(indicatorText.equals("ADX")){
                        lastRendered = "ADX";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highadx.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }
                    else if(indicatorText.equals("Price")){
                        lastRendered = "Price";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highprice.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }
                    else if(indicatorText.equals("RSI")){
                        lastRendered = "RSI";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient()
                                                       {
                                                           public void onPageFinished(WebView view, String url){

                     Picture picture = view.capturePicture();
                     Bitmap b = Bitmap.createBitmap( picture.getWidth(),
                     picture.getHeight(), Bitmap.Config.ARGB_8888);
                     Canvas c = new Canvas( b );
                     picture.draw( c );
                     FileOutputStream fos = null;
                     File file;
                     try{
                     file = new File(Environment.getExternalStorageDirectory().toString()+"/yahoo.jpg");
                     Log.d("file",file.getAbsolutePath());
                     fos = new FileOutputStream(file);
                     Log.d("PageFinFinished",fos.toString());
                     if (!file.exists()) {
                     file.createNewFile();
                     }

                     if ( fos != null )
                     {
                     b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                     fos.close();
                     }

                     }
                     catch (Exception e){
                     e.printStackTrace();
                     }
                       }});

                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highrsi.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }
                    else if(indicatorText.equals("CCI")){
                        lastRendered = "CCI";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highcci.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }
                    else if(indicatorText.equals("MACD")){
                        lastRendered = "MACD";
                        Log.d("indicator change", (String) mytext.getText());
                        indicatorView.getSettings().setJavaScriptEnabled(true);
                        indicatorView.setWebChromeClient(new WebChromeClient());
                        indicatorView.setWebViewClient(new WebViewClient());
                        indicatorView.loadUrl("http://www-scf.usc.edu/~deole/highmacd.php/?sym="+symPassed);
                        indicatorView.setVisibility(View.VISIBLE);
                        changeButton.setTextColor(Color.parseColor("#808080"));
                        changeButton.setEnabled(false);
                    }



                }
            });

        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
