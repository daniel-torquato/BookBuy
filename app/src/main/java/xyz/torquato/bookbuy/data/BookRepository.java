package xyz.torquato.bookbuy.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import xyz.torquato.bookbuy.domain.BookItem;

public class BookRepository {
    public final MutableLiveData<List<BookItem>> example = new MutableLiveData<>();

    @Inject
    public BookRepository(BookDataSource dataSource) {

        dataSource.getBooks();
        if (dataSource.getError() == null) {
            JSONObject result = dataSource.getResult();

            example.setValue(toDomain(result));
        } else {
            Log.d("MyTag", dataSource.getError().toString());
        }

    }

    public LiveData<List<BookItem>> getItems() {
        return example;
    }


    List<BookItem> toDomain(JSONObject input) {
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
