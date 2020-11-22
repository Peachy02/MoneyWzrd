//
//package ispy.corp.moneywzrd.investments;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class QuoteFetcher {
//    private IQuoteService service_;
//
//    public QuoteFetcher(IQuoteService service) {
//        service_ = service;
//    }
//
//    public interface OnQuery {
//        public void onQuotes(List<QuoteResult> result, Exception e, int task_id);
//    }
//
//    private class FetchTask implements Runnable {
//        private TaskToken token_;
//        private List<QuoteRequest> requests_;
//        private OnQuery callback_;
//
//        public FetchTask(TaskToken token, List<QuoteRequest> requests, OnQuery callback) {
//            token_ = token;
//            requests_ = requests;
//            callback_ = callback;
//        }
//
//        @Override
//        public void run() {
//            List<QuoteResult> results = null;
//            Exception ex = null;
//            try {
//                results = service_.query(requests_);
//            } catch (Exception e) {
//                ex = e;
//                results = new ArrayList<QuoteResult>();
//            }
//            callback_.onQuotes(results, ex, token_.getTaskId());
//        }
//    }
//
//    public TaskToken fetch(List<QuoteRequest> symbols, OnQuery callback) {
//        int batch_size = service_.getBatchSize();
//        int num_batches = getNumBatches(symbols.size(), batch_size);
//
//        ExecutorService pool = Executors.newFixedThreadPool(Math.min(num_batches, 4));
//        TaskToken token = new TaskToken(pool, num_batches);
//
//        for (int i = 0; i < num_batches; i++) {
//            int start = i * batch_size;
//            int end = Math.min(i * batch_size + batch_size, symbols.size());
//            List<QuoteRequest> batch = symbols.subList(start, end);
//            FetchTask task = new FetchTask(token, batch, callback);
//            pool.submit(task);
//        }
//        return token;
//    }
//
//    private int getNumBatches(int symbols, int batch_size) {
//        if (symbols % batch_size != 0)
//            symbols += batch_size;
//        return symbols / batch_size;
//    }
//
//    public void shutdown() {
//        service_.shutdown();
//    }
//}
