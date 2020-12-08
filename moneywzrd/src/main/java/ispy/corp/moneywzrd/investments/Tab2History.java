package ispy.corp.moneywzrd.investments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import ispy.corp.moneywzrd.R;

/**
 * Created by poojadeole on 11/18/17.
 */

public class Tab2History extends Fragment {

    WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.tab2history, container, false);
//        webView = (WebView) rootView.findViewById(R.id.webview);
//        return rootView;
        String symPassed = ((AddStock)getActivity()).message;
        String url  = "http://www-scf.usc.edu/~deole/highstocks.php/?sym="+symPassed;
            super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.tab2history, container, false);
            //setContentView(R.layout.tab2history);
            WebView view = (WebView) rootView.findViewById(R.id.webView);
            view.getSettings().setJavaScriptEnabled(true);
            view.setWebChromeClient(new WebChromeClient());
            view.loadUrl(url);

            return rootView;

    }
}
