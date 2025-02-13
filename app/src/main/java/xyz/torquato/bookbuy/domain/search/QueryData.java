package xyz.torquato.bookbuy.domain.search;

public class QueryData {

    public String query;

    public int startIndex;

    public int lastIndex;

    public QueryData(
            String query,
            int startIndex,
            int lastIndex
    ) {
        this.query = query;
        this.startIndex = startIndex;
        this.lastIndex = lastIndex;
    }
}
