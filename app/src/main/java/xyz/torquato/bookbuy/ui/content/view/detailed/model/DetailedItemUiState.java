package xyz.torquato.bookbuy.ui.content.view.detailed.model;

public class DetailedItemUiState {
    public String id;

    public String title;

    public String author;

    public String description;

    public String smallThumbnailUrl;

    public String largeThumbnailUrl;

    public String buyLink;

    public DetailedItemUiState(String _title, String _author, String _description) {
        title = _title;
        author = _author;
        description = _description;
    }
}
