package xyz.torquato.bookbuy.data.favorites;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorites {
    @PrimaryKey
    @ColumnInfo(name = "book_id")
    @NonNull
    public String id;

    Favorites(
            @NonNull String id
    ) {
        this.id = id;
    }
}