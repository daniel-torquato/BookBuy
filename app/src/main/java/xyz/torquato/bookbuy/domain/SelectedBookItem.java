package xyz.torquato.bookbuy.domain;

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
