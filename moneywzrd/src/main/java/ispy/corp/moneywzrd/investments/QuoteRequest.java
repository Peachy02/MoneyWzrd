package ispy.corp.moneywzrd.investments;

public class QuoteRequest {
    public final String symbol;
    public final String lastTradeDate;
    public final boolean fetchName;

    public QuoteRequest(String symbol, String lastTradeDate, boolean fetchName) {
        this.symbol = symbol;
        this.lastTradeDate = lastTradeDate;
        this.fetchName = fetchName;
    }

    public boolean isSameDate(String ymd) {
        return lastTradeDate != null && lastTradeDate.equals(ymd);
    }
}
