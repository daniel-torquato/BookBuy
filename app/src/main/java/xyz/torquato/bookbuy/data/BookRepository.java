package xyz.torquato.bookbuy.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.model.BookList;
import xyz.torquato.bookbuy.domain.BookItem;

public class BookRepository {
    public final MutableLiveData<List<BookItem>> example = new MutableLiveData<>();

    @Inject
    public BookRepository(BookDataSource dataSource) {
        BookList check = dataSource.getBooks();
        List<BookItem> output = new ArrayList<>();

        check.items.forEach(bookData -> output.add(new BookItem(bookData.title, bookData.author, bookData.description)));

        example.setValue(output);
    }

    public LiveData<List<BookItem>> getItems() {
        return example;
    }
}
