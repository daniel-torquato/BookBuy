package xyz.torquato.bookbuy.domain.favorites;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;

@Singleton
public class GetFavoriteFilterUseCase {
    FavoriteRepository favoriteRepository;

    @Inject
    public GetFavoriteFilterUseCase(
            FavoriteRepository favoriteRepository
    ) {
        this.favoriteRepository = favoriteRepository;
    }

    public LiveData<Boolean> __invoke__() {
        return favoriteRepository.favoriteFiltered;
    }
}
