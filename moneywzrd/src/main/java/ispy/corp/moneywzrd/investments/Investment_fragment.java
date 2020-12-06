package ispy.corp.moneywzrd.investments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ispy.corp.moneywzrd.Login_main;
import ispy.corp.moneywzrd.MainActivity;
import ispy.corp.moneywzrd.R;
import ispy.corp.moneywzrd.Settings_activity;

import static ispy.corp.moneywzrd.R.string.logged;

public class Investment_fragment extends Fragment {
    static final private String TAG = "Investment_fragment";


    private InvestmentFragmentViewModel mViewModel;
    View rootView;
    private Settings settings_;
    private IQuoteService quote_service_;
    private QuoteFetcher quote_fetcher_;
    private Map<String, View> stock_rows_ = new HashMap<String, View>();
    private TaskToken refresh_task_;
    private SwipeRefreshLayout refresh_view_;

    enum ChangeBoxMode {
        Value,
        Percent
    };
    ChangeBoxMode changebox_mode_ = ChangeBoxMode.Value;



    public static Investment_fragment newInstance() {
        return new Investment_fragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings_ = StockApplication.getSettings();
    }







    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.investment_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).getDelegate().setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getDelegate().getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setHasOptionsMenu(true);

        return rootView;
    }







    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InvestmentFragmentViewModel.class);

    }





    @Override
    public void onStart() {
        super.onStart();

        populate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL d");
        TextView text = getActivity().findViewById(R.id.subtitle);
        text.setText(LocalDate.now().format(formatter));




        View view = getView().findViewById(R.id.list_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickReorderButton(v);
            }
        });



        view = getView().findViewById(R.id.settings_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickSettingsButton(v);
            }
        });

        refresh_view_ = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh);
        refresh_view_.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


    }

    private void populate() {
        Map<String, CachedQuote> cachedQuotes = settings_.getCachedQuotes();

        LinearLayout container = (LinearLayout)getActivity().findViewById(R.id.stock_list);
        container.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();
        String[] symbols = settings_.getStockSymbols();
        for (String symbol : symbols) {
            CachedQuote quote = cachedQuotes.get(symbol);
            View view = addStockRow(inflater, container, symbol);
            stock_rows_.put(symbol, view);
            updateStockRow(symbol, quote);
            container.addView(inflater.inflate(R.layout.divider_line, null));
        }

        refresh();
    }


    private View addStockRow(LayoutInflater inflater, LinearLayout container, String symbol) {
        View root = inflater.inflate(R.layout.stock_row, null);

        TextView symbol_name = (TextView)root.findViewById(R.id.symbol_name);
        symbol_name.setText(symbol);

        View changebox = root.findViewById(R.id.change_box);
        changebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipChangeBoxMode();
            }
        });

        container.addView(root);
        return root;
    }

    private void refresh() {
        cancelRefresh();

        List<QuoteRequest> requests = new ArrayList<QuoteRequest>();
        for (Map.Entry<String, View> entry : stock_rows_.entrySet()) {
            CachedQuote cache = settings_.getCachedQuotes().get(entry.getKey());
            String lastTradeDate = cache != null ? cache.lastTradeDate : null;
            boolean fetchName = (cache == null || cache.companyName == null);
            requests.add(new QuoteRequest(entry.getKey(), lastTradeDate, fetchName));
        }

        final Investment_fragment activity = this;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                activity.handleQuoteMessage(msg.getData());
            }
        };

        QuoteFetcher.OnQuery callback = new QuoteFetcher.OnQuery() {
            @Override
            public void onQuotes(List<QuoteResult> results, Exception e, int task_id) {
                Message msg = new Message();
                msg.setAsynchronous(true);

                Bundle b = new Bundle();
                b.putInt("count", results.size());
                for (int i = 0; i < results.size(); i++)
                    b.putBundle(Integer.toString(i), results.get(i).serialize());
                if (e != null)
                    b.putString("exception", e.getMessage());
                b.putInt("task_id", task_id);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };

       // refresh_task_ = quote_fetcher_.fetch(requests, callback);
    }

    private void cancelRefresh() {
        if (refresh_task_ == null)
            return;
        refresh_task_.cancel();
        refresh_task_ = null;

        refresh_view_.setRefreshing(false);
    }

    private void handleQuoteMessage(Bundle b) {
        int task_id = b.getInt("task_id");
        if (refresh_task_ == null || refresh_task_.getTaskId() != task_id)
            return;

        refresh_task_.receivedTaskCompletion();
        if (refresh_task_.done()) {
            refresh_task_ = null;
            refresh_view_.setRefreshing(false);
        }

        if (b.containsKey("exception")) {
            Log.e(TAG, "Failed to query quotes: " + b.getString("exception"));
            return;
        }

        int count = b.getInt("count");
        for (int i = 0; i < count; i++) {
            Bundle child = b.getBundle(Integer.toString(i));
            QuoteResult result = QuoteResult.deserialize(child);
            CachedQuote quote = null;
            if (result.success) {
                quote = updateCache(result);
            } else {
                Log.e(TAG, "Could not query symbol " + result.symbol + ": " +
                        result.error);
                quote = maybeFetchCache(result.symbol);
            }
            updateStockRow(result.symbol, quote);
        }
    }

    private void updateStockRow(String symbol, CachedQuote quote) {
        View view = stock_rows_.get(symbol);
        if (view == null) {
            Log.e(TAG, "Could not find View for symbol: " + symbol);
            return;
        }

        BigDecimal price = CachedQuote.getPrice(quote);
        TextView quote_box = (TextView) view.findViewById(R.id.quote);
        if (price != null)
            quote_box.setText(Utilities.formatPrice(price));
        else
            quote_box.setText("");

        TextView name_box = (TextView)view.findViewById(R.id.company_name);
        if (quote != null && quote.companyName != null)
            name_box.setText(quote.companyName);

        TextView changebox = (TextView)view.findViewById(R.id.change_box);
        updateChangeBox(changebox, quote);
    }

    // CachedQuote here can be null.
    private void updateChangeBox(TextView changebox, CachedQuote quote) {
        String value;
        BigDecimal change = null;
        String change_text = null;
        if (changebox_mode_ == ChangeBoxMode.Value) {
            change = CachedQuote.getPriceChange(quote);
            if (change != null)
                change_text = Utilities.formatPrice(change);
        } else if (changebox_mode_ == ChangeBoxMode.Percent) {
            change = CachedQuote.getPercentChange(quote);
            if (change != null)
                change_text = Utilities.formatPercent(change);
        }

        int bgcolor_id;
        int textcolor_id;
        if (change == null) {
            bgcolor_id = R.color.white;
            textcolor_id = R.color.black;
        } else {
            if (change.signum() >= 0) {
                change_text = "+" + change_text;
                bgcolor_id = R.color.green;
            } else {
                bgcolor_id = R.color.red;
            }
            textcolor_id = R.color.almost_white;
        }

        int bgcolor = ResourcesCompat.getColor(getResources(), bgcolor_id, null);
        Drawable pill_box = Utilities.getPillBox(getActivity(), bgcolor);
        if (change_text != null) {
            changebox.setText(change_text);
            changebox.setGravity(Gravity.RIGHT);
        } else {
            changebox.setText("    -    ");
            changebox.setGravity(Gravity.CENTER);
        }
        changebox.setBackground(pill_box);
        changebox.setTextColor(ResourcesCompat.getColor(getResources(), textcolor_id, null));
    }

    private void flipChangeBoxMode() {
        if (changebox_mode_ == ChangeBoxMode.Percent)
            changebox_mode_ = ChangeBoxMode.Value;
        else
            changebox_mode_ = ChangeBoxMode.Percent;

        Map<String, CachedQuote> quotes = settings_.getCachedQuotes();
        for (Map.Entry<String, View> entry : stock_rows_.entrySet()) {
            String symbol = entry.getKey();
            View view = entry.getValue();
            TextView changebox = (TextView)view.findViewById(R.id.change_box);
            updateChangeBox(changebox, quotes.get(symbol));
        }
    }

    private CachedQuote updateCache(QuoteResult result) {
        CachedQuote quote = new CachedQuote(result.symbol);
        quote.lastTradeDate = result.lastTradeDate;
        quote.recentQuote = result.recentQuote;

        // If the service didn't return a quote for the previous day, use the cached result. This
        // can happen if we're trying to avoid an extra query on APIs that have separate endpoints
        // for realtime queries vs historical.
        CachedQuote prev_quote = settings_.getCachedQuotes().get(result.symbol);
        if (result.prevDayQuote != null)
            quote.prevDayQuote = result.prevDayQuote;
        else if (prev_quote != null && prev_quote.lastTradeDate.equals(result.lastTradeDate))
            quote.prevDayQuote = prev_quote.prevDayQuote;

        // Fill the company name in, if present.
        if (result.companyName != null)
            quote.companyName = result.companyName;
        else
            quote.companyName = prev_quote.companyName;

        quote.cacheDate = LocalDate.now(Utilities.UTC).toString();
        settings_.saveCachedQuote(quote);
        return quote;
    }

    private CachedQuote maybeFetchCache(String symbol) {
        CachedQuote quote = settings_.getCachedQuotes().get(symbol);
        if (quote == null || quote.lastTradeDate == null)
            return null;

        LocalDate now = LocalDate.now(Utilities.UTC);
        LocalDate then = Utilities.parseYearMonthDay(quote.lastTradeDate);
        long days = Math.abs(java.time.temporal.ChronoUnit.DAYS.between(now, then));
        if (days > 1)
            return null;
        return quote;
    }


    private void OnClickSettingsButton(View v) {
        LaunchSettingsActivity();
    }

    private void LaunchSettingsActivity() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }


    private void OnClickReorderButton(View v) {
        Intent intent = new Intent(getActivity(), Stock_edit.class);
        startActivity(intent);
    }


}