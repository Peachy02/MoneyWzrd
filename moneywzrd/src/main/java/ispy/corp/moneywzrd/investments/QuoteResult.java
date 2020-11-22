//package ispy.corp.moneywzrd.investments;
//
//import android.os.Bundle;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Date;
//
//public class QuoteResult {
//    public static final String SYMBOL_NOT_FOUND = "symbol_not_found";
//
//    public boolean success;
//    public String symbol;
//    public String lastTradeDate;
//    public String prevDayQuote;
//    public String recentQuote;
//    public String companyName;
//    public String error;
//
//    public static  deserialize(Bundle b) {
//        result.lastTradeDate = b.getString("last_trade_date");
//        result.prevDayQuote = b.getString("prev_day_quote");
//        result.recentQuote = b.getString("recent_quote");
//        result.success = b.getBoolean("success");
//        result.symbol = b.getString("symbol");
//        result.companyName = b.getString("company_name");
//        result.error = b.getString("error");
//        return result;
//    }
//
//    public Bundle serialize() {
//        Bundle b = new Bundle();
//        b.putString("symbol", this.symbol);
//        b.putBoolean("success", this.success);
//        b.putString("last_trade_date", this.lastTradeDate);
//        b.putString("prev_day_quote", this.prevDayQuote);
//        b.putString("recent_quote", this.recentQuote);
//        b.putString("company_name", this.companyName);
//        b.putString("error", this.error);
//        return b;
//    }
//
//
//    public boolean notFound() {
//        return !success && error != null && error.equals(SYMBOL_NOT_FOUND);
//    }
//}
