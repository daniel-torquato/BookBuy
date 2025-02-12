package xyz.torquato.bookbuy.data.favorites;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FavoriteRepository {

    private final FavoritesDao favoritesDao;
    private final Set<String> favoriteList = new HashSet<>();

    @Inject
    public FavoriteRepository(
            FavoritesDao favoritesDao
    ) {
        this.favoritesDao = favoritesDao;
    }

    public boolean isFavorite(String id) {
        return favoriteList.contains(id) || favoritesDao.hasFavorite(id);
    }

    public void addToFavorite(String id) {
        if (!favoriteList.contains(id) && !favoritesDao.hasFavorite(id)) {
            favoriteList.add(id);
            favoritesDao.insert(new Favorites(id));
        }
    }

    public void removeFromFavorite(String id) {
        if (!favoriteList.contains(id) && !favoritesDao.hasFavorite(id)) {
            favoriteList.remove(id);
            favoritesDao.delete(new Favorites(id));
        }
    }
}
