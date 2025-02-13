package xyz.torquato.bookbuy.data.books;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.concurrency.IOExecutor;
import xyz.torquato.bookbuy.data.books.model.QueryResult;
import xyz.torquato.bookbuy.domain.model.BookItem;
import xyz.torquato.bookbuy.domain.search.QueryData;

@Singleton
public class BookRepository {

    private final BookDataSource dataSource;
    public final MutableLiveData<List<BookItem>> _items = new MutableLiveData<>();
    public final LiveData<List<BookItem>> items = _items;
    private final IOExecutor executor;

    @Inject
    public BookRepository(BookDataSource dataSource, IOExecutor executor) {
        this.dataSource = dataSource;
        Transformations.map(dataSource.data, data -> {
            if (data instanceof QueryResult.Valid) {
                return toDomain(((QueryResult.Valid) data).data);
            } else if (data instanceof QueryResult.Error) {
                return List.of(new BookItem("No Title", "No Author", "No Description"));
            }
            return List.of(new BookItem("No Title", "No Author", "No Description"));
        }).observeForever(newItems -> {
            List<BookItem> currentItems =  this._items.getValue();
            if (currentItems == null) {
                this._items.setValue(newItems);
            } else {
                List<BookItem> mutableItems = new ArrayList<>(currentItems);
                mutableItems.addAll(newItems);
                this._items.setValue(mutableItems);
            }
        });
        this.executor = executor;
    }

    public void setQuery(QueryData data) {
        Log.d("MyTag", "New Search" + data.query + "range " + data.startIndex + " limit " + data.lastIndex);
        executor.execute(() ->
                dataSource.Search(data.query, data.startIndex, data.lastIndex - data.startIndex)
        );
    }

    public void clean() {
        this._items.setValue(List.of());
    }

    List<BookItem> toDomain(JSONObject input) {
        Log.d("MyTag", "Converting to Domain");
        List<BookItem> output = new ArrayList<>();
        try {
            JSONArray items = input.getJSONArray("items");
            for (int i = 0; i<items.length(); i++) {
                JSONObject jsonItem = items.getJSONObject(i);
                BookItem item = new BookItem("Not Found", "Not Found", "Not Found");

                if (!jsonItem.isNull("id")) {
                    item.id = jsonItem.getString("id");
                }

                if (!jsonItem.isNull("volumeInfo")) {
                    JSONObject volumeInfo = jsonItem.getJSONObject("volumeInfo");
                    if (!volumeInfo.isNull("title")) {
                        item.title = volumeInfo.getString("title");
                    }

                    if (!volumeInfo.isNull("authors")) {
                        JSONArray authors = volumeInfo.getJSONArray("authors");
                        item.author = authors.getString(0);
                    }

                    if (!volumeInfo.isNull("description")) {
                        item.description = volumeInfo.getString("description");
                    }

                    if (!volumeInfo.isNull("imageLinks")) {
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        item.smallThumbnailUrl = imageLinks.getString("smallThumbnail");
                        item.largeThumbnailUrl = imageLinks.getString("thumbnail");
                    }
                }

                if (!jsonItem.isNull("saleInfo")) {
                    JSONObject saleInfo = jsonItem.getJSONObject("saleInfo");
                    if (!saleInfo.isNull("saleability")) {
                        String saleability = saleInfo.getString("saleability");
                        if (saleability.equals("FOR_SALE")) {
                            item.hasBuyLink = true;
                            item.buyLink = saleInfo.getString("buyLink");
                        } else {
                            item.hasBuyLink = false;
                        }
                    }
                }

                output.add(item);
            }

        } catch (JSONException e) {
            Log.d("MyTag", "Items not found");
        }
        return output;
    }



}
