package xyz.torquato.bookbuy.data;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.model.BookList;

public class BookDataSource {

    @Inject
    public BookDataSource() {}

    public native String example();

    public native BookList getBooks();

    static {
        System.loadLibrary("booklib");
    }
}
