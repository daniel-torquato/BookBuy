package xyz.torquato.bookbuy.data;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.model.BookData;

public class BookDataSource {

    @Inject
    public BookDataSource() {
    }

    public native String example();
    public native BookData getBooks();

    static {
        System.loadLibrary("booklib");
    }
}
