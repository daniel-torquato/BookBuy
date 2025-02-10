package xyz.torquato.bookbuy.domain;

public class BookItem {
    public String title;
    public String author;

    public String description;

    public String smallThumbnailUrl;

    public String largeThumbnailUrl;

    public String buyLink;

    public BookItem(String _title, String _author, String _description) {
        title = _title;
        author = _author;
        description = _description;
    }
}
