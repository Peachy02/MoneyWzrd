//package ispy.corp.moneywzrd.investments;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//
//public class SymbolSearchAdapter extends ArrayAdapter<SymbolSuggestion>
//{
//    private LayoutInflater inflater_;
//    private SymbolSuggestion[] results_ =
//            new SymbolSuggestion[0];
//
//    public SymbolSearchAdapter(Context context, int resource) {
//        super(context, resource);
//        inflater_ = LayoutInflater.from(context);
//    }
//
//    public void update(SymbolSuggestion[] results) {
//        results_ = results;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//        return results_.length;
//    }
//
//    @Override
//    public SymbolSuggestion getItem(int position) {
//        return results_[position];
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        if (view == null)
//            view = inflater_.inflate(R.layout.stock_suggestion, parent, false);
//
//        SymbolSuggestion suggestion = getItem(position);
//
//        TextView symbol_name = (TextView)view.findViewById(R.id.symbol_name);
//        symbol_name.setText(suggestion.symbol);
//
//        TextView company_name = (TextView)view.findViewById(R.id.company_name);
//        company_name.setText(suggestion.companyName);
//
//        return view;
//    }
//}
