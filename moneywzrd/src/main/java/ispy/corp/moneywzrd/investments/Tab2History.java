package ispy.corp.moneywzrd.investments;
//ISpy Corp
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import ispy.corp.moneywzrd.R;



public class Tab2History extends Fragment {

    //brings in the web view chart of the history of the stock price
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String symPassed = ((AddStock)getActivity()).message;
        String url  = "http://www-scf.usc.edu/~deole/highstocks.php/?sym="+symPassed;//cant extract this string
            super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.tab2history, container, false);
            WebView view = (WebView) rootView.findViewById(R.id.webView);
            view.getSettings().setJavaScriptEnabled(true);
            view.setWebChromeClient(new WebChromeClient());
            view.loadUrl(url);

            return rootView;

    }
}
