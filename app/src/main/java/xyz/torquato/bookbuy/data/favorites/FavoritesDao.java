package xyz.torquato.bookbuy.data.favorites;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import javax.inject.Singleton;

@Singleton
@Dao
public interface FavoritesDao {
    @Query("SELECT EXISTS(SELECT * FROM Favorites WHERE book_id=:id)")
    Boolean hasFavorite(String id);

    @Insert
    void insert(Favorites favorite);

    @Delete
    void delete(Favorites favorites);
}