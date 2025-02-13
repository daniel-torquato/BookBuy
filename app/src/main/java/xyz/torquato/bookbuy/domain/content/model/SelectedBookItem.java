package xyz.torquato.bookbuy.domain.content.model;

import xyz.torquato.bookbuy.domain.model.BookItem;

public class SelectedBookItem {

    public BookItem item;

    public Boolean isFavorite;

    SelectedBookItem(
            BookItem item,
            Boolean isFavorite
    ) {
        this.item = item;
        this.isFavorite = isFavorite;
    }
}
