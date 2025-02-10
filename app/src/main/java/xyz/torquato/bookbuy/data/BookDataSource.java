package xyz.torquato.bookbuy.data;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.model.BookList;

public class BookDataSource {

    // TODO: declare result and error as LiveData.
    JSONObject result;
    JSONException error;

    @Inject
    public BookDataSource() {}

    public native String example();

    public native void getBooks();

    public void setResult(JSONObject input, JSONException _error) {
        result = input;
        error = _error;
    }

    public JSONObject getResult() {
            return result;
    }

    public JSONException getError() {
        return  error;
    }

    static {
        System.loadLibrary("booklib");
    }
}
