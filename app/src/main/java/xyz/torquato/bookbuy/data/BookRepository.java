package xyz.torquato.bookbuy.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.model.QueryResult;
import xyz.torquato.bookbuy.domain.BookItem;
import xyz.torquato.bookbuy.domain.QueryData;

public class BookRepository {

    private final BookDataSource dataSource;
    public final LiveData<List<BookItem>> items;

    @Inject
    public BookRepository(BookDataSource dataSource) {
        this.dataSource = dataSource;
        items = Transformations.map(dataSource.data, data -> {
            if (data instanceof QueryResult.Valid) {
                return toDomain(((QueryResult.Valid) data).data);
            } else if (data instanceof QueryResult.Error) {
                return List.of(new BookItem("No Title", "No Author", "No Description"));
            }
            return List.of();
        });
    }

    public void setQuery(QueryData data) {
        dataSource.Search(data.query, data.startIndex, data.maxResults);
    }

    List<BookItem> toDomain(JSONObject input) {
        Log.d("MyTag", "Converting to Domain");
        List<BookItem> output = new ArrayList<>();
        try {
            JSONArray items = input.getJSONArray("items");
            for (int i = 0; i<items.length(); i++) {
                JSONObject jsonItem = items.getJSONObject(i);
                BookItem item = new BookItem("Not Found", "Not Found", "Not Found");

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
                    if (!jsonItem.isNull("saleability")) {
                        String saleability = saleInfo.getString("saleability");
                        if (saleability.equals("FOR_SALE")) {
                            item.buyLink = saleInfo.getString("buyLink");
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
