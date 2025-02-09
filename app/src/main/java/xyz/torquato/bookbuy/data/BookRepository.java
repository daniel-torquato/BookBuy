package xyz.torquato.bookbuy.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.model.BookData;
import xyz.torquato.bookbuy.domain.BookItem;

public class BookRepository {
    public final MutableLiveData<List<BookItem>> example = new MutableLiveData<>();

    @Inject
    public BookRepository(BookDataSource dataSource) {
        BookData check = dataSource.getBooks();
        example.setValue(List.of(
                new BookItem(check.title, check.author, check.description),
                new BookItem("Title3", "AuthorX", "DescriptionZ")
        ));
    }

    public LiveData<List<BookItem>> getItems() {
        return example;
    }
}
