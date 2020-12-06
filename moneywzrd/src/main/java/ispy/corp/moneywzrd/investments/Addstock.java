package ispy.corp.moneywzrd.investments;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import ispy.corp.moneywzrd.R;

public class Addstock extends AppCompatActivity {
    private Settings settings_;
    private IQuoteService quote_service_;
    private SymbolSearchAdapter search_adapter_;
    private SymbolSearchThread thread_;
    private Handler message_handler_;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstock);


        settings_ = StockApplication.getSettings();

//        if (!initializeQuoteService())
//            return;

        SymbolSearchThread.OnSearchResults callback = new SymbolSearchThread.OnSearchResults() {
            @Override
            public void onSearchResults(SymbolSuggestion[] suggestions) {
                postSearchResultMessage(suggestions);
            }
        };

        message_handler_ = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleSearchMessage(msg);
            }
        };

        thread_ = new SymbolSearchThread(quote_service_, callback);
    }






    @Override
    protected void onDestroy() {
        if (quote_service_ != null) {
            quote_service_.shutdown();
            quote_service_ = null;
        }
        if (thread_ != null) {
            thread_.shutdown();
            thread_ = null;
        }
        super.onDestroy();
    }




    @Override
    protected void onStart() {
        super.onStart();

        if (quote_service_ == null)
            return;



        final AutoCompleteTextView view = (AutoCompleteTextView)findViewById(R.id.stock_search);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });


        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                SymbolSuggestion suggestion = search_adapter_.getItem(position);
                view.setText(suggestion.symbol);
                addSymbol(suggestion.symbol, suggestion.companyName);
            }
        });

        view.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    addSymbol(v.getText().toString(), null);
                    return true;
                }
                return false;
            }
        });

        search_adapter_ = new SymbolSearchAdapter(this, R.layout.stock_suggestion);
        view.setAdapter(search_adapter_);
        view.setThreshold(0);
    }



    private void addSymbol(String symbol, String companyName) {
        settings_.addSymbol(symbol, companyName);
        finish();
    }


    private void performSearch(String text) {
        if (text.length() == 0)
            return;

        thread_.beginSearch(text);
    }




    private void postSearchResultMessage(SymbolSuggestion[] suggestions) {
        Bundle b = new Bundle();
        b.putInt("count", suggestions.length);
        for (int i = 0; i < suggestions.length; i++) {
            Bundle item = suggestions[i].serialize();
            b.putBundle(Integer.toString(i), item);
        }

        Message msg = new Message();
        msg.setAsynchronous(true);
        msg.setData(b);
        message_handler_.sendMessage(msg);
    }




    private void handleSearchMessage(Message msg) {
        Bundle b = msg.getData();
        int count = b.getInt("count");

        SymbolSuggestion[] suggestions = new SymbolSuggestion[count];
        for (int i = 0; i < count; i++) {
            Bundle item = b.getBundle(Integer.toString(i));
            suggestions[i] = SymbolSuggestion.deserialize(item);
        }
        search_adapter_.update(suggestions);
    }




    private boolean initializeQuoteService() {
        quote_service_ = settings_.createQuoteService();
        if (quote_service_ == null) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }





}






