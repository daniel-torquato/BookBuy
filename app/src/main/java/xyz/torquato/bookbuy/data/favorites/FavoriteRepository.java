package xyz.torquato.bookbuy.data.favorites;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FavoriteRepository {

    private final Set<String> favoriteList = new HashSet<>();

    @Inject
    FavoriteRepository() {}

    public boolean isFavorite(String id) {
        return favoriteList.contains(id);
    }

    public void addToFavorite(String id) {
        Log.d("MyTag", "Added " + id);
        favoriteList.add(id);
    }

    public void removeFromFavorite(String id) {
        Log.d("MyTag", "Removed " + id);
        favoriteList.remove(id);
    }
}
