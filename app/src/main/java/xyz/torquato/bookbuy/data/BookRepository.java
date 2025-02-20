package xyz.torquato.bookbuy.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import xyz.torquato.bookbuy.domain.BookItem;

public class BookRepository {
    public final MutableLiveData<List<BookItem>> example = new MutableLiveData<>();

    @Inject
    public BookRepository(BookDataSource dataSource) {
        example.setValue(List.of(
                new BookItem(dataSource.example(), "Author", "Description"),
                new BookItem("Title3", "AuthorX", "DescriptionZ")
        ));
    }

    public LiveData<List<BookItem>> getItems() {
        return example;
    }
}
