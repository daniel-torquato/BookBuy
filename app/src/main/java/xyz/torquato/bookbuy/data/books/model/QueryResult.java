package xyz.torquato.bookbuy.data.books.model;

import org.json.JSONException;
import org.json.JSONObject;

public interface QueryResult  {

    class Valid implements QueryResult  {
        public JSONObject data;
        public Valid(JSONObject data) {
            this.data = data;
        }
    }

    class Error implements QueryResult {
        public JSONException error;
        public Error(JSONException error) {
            this.error = error;
        }
    }
}
