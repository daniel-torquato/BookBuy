package xyz.torquato.bookbuy.data.favorites;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import javax.inject.Singleton;

@Singleton
@Database(entities = {Favorites.class}, version = 1)
public abstract class FavoritesDatabase extends RoomDatabase {
    public abstract FavoritesDao favoritesDao();
}
