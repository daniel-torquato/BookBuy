package xyz.torquato.bookbuy.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import xyz.torquato.bookbuy.domain.BookItem;

public class BookRepository {
    public final MutableLiveData<List<BookItem>> example = new MutableLiveData<>();

    public BookRepository() {
        example.setValue(List.of(
                new BookItem("Title", "Author", "Description"),
                new BookItem("Title3", "AuthorX", "DescriptionZ")
        ));
    }

    public LiveData<List<BookItem>> getItems() {
        return example;
    }
}
