package xyz.torquato.bookbuy.domain.search;

public class QueryData {

    public String query;

    public int startIndex;

    public int maxResults;

    public QueryData(
            String query,
            int startIndex,
            int maxResults
    ) {
        this.query = query;
        this.startIndex = startIndex;
        this.maxResults = maxResults;
    }
}
