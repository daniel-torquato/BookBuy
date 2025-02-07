package xyz.torquato.bookbuy.data;

import javax.inject.Inject;

public class BookDataSource {

    @Inject
    public BookDataSource() {}

    public native void example();

    static {
        System.loadLibrary("booklib");
    }
}
