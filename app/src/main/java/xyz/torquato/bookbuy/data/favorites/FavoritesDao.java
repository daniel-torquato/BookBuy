package xyz.torquato.bookbuy.data.favorites;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import javax.inject.Singleton;

@Singleton
@Dao
public interface FavoritesDao {
    @Query("SELECT EXISTS(SELECT * FROM Favorites WHERE book_id=:id)")
    Boolean hasFavorite(String id);

    @Query("SELECT book_id FROM Favorites WHERE book_id IN (:items)")
    List<String> getAll(List<String> items);

    @Insert
    void insert(Favorites favorite);

    @Delete
    void delete(Favorites favorites);
}