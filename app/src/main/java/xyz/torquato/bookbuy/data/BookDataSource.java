package xyz.torquato.bookbuy.data;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.data.model.QueryResult;

@Singleton
public class BookDataSource {

    public final MutableLiveData<QueryResult> data = new MutableLiveData<>();

    @Inject
    public BookDataSource() {}

    public void setResult(JSONObject input, JSONException error) {
        if (error != null) {
            data.setValue(new QueryResult.Error(error));
        } else {
            data.setValue(new QueryResult.Valid(input));
        }
    }

    public native void Search(String query, int startIndex, int length);

    static {
        System.loadLibrary("booklib");
    }
}
