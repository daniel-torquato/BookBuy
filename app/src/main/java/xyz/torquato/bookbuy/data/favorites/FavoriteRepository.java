package xyz.torquato.bookbuy.data.favorites;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FavoriteRepository {

    @Inject
    FavoriteRepository() {}

    public boolean isFavorite(String id) {
        return false;
    }

    public void addToFavorite(String id) {
        Log.d("MyTag", "Added " + id);
    }

    public void removeFromFavorite(String id) {
        Log.d("MyTag", "Removed " + id);
    }
}
