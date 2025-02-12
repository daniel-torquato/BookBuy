package xyz.torquato.bookbuy.ui.content.view.detailed.model;

public class DetailedItemUiState {
    public String id;

    public String title;

    public String author;

    public String description;

    public String smallThumbnailUrl;

    public String largeThumbnailUrl;

    public Boolean hasBuyLink;

    public String buyLink;

    public DetailedItemUiState(
            String id,
            String title,
            String author,
            String description,
            String smallThumbnailUrl,
            String largeThumbnailUrl,
            Boolean hasBuyLink,
            String buyLink
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.smallThumbnailUrl = smallThumbnailUrl;
        this.largeThumbnailUrl = largeThumbnailUrl;
        this.hasBuyLink = hasBuyLink;
        this.buyLink = buyLink;
    }
}
