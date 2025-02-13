package xyz.torquato.bookbuy.data.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.concurrency.IOExecutor;

@Singleton
public class FavoriteRepository {

    private final FavoritesDao favoritesDao;
    private final Set<String> favoriteList = new HashSet<>();
    private final Executor executor;

    @Inject
    public FavoriteRepository(
            FavoritesDao favoritesDao,
            IOExecutor executor
    ) {
        this.favoritesDao = favoritesDao;
        this.executor = executor;
    }

    public LiveData<Boolean> isFavorite(String id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>(false);
        executor.execute(() -> {
            if (favoritesDao.hasFavorite(id)) {
                result.postValue(true);
                favoriteList.add(id);
            }
        });
        if (favoriteList.contains(id)) {
            result.setValue(true);
        }
        return result;
    }

    public void addToFavorite(String id) {
        if (!favoriteList.contains(id)) {
            executor.execute(() -> {
                if (!favoritesDao.hasFavorite(id)) {
                    favoritesDao.insert(new Favorites(id));
                }
            });
            favoriteList.add(id);
        }
    }

    public void removeFromFavorite(String id) {
        favoriteList.remove(id);
        executor.execute(() -> {
            favoritesDao.delete(new Favorites(id));
        });
    }
}
