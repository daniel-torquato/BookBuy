package xyz.torquato.bookbuy.domain.favorites;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;

public class PerformFavoriteUseCase {

    FavoriteRepository favoriteRepository;

    @Inject
    public PerformFavoriteUseCase(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void __invoker__(String id, boolean isFavorite) {
        if (isFavorite) {
            favoriteRepository.addToFavorite(id);
        } else {
            favoriteRepository.removeFromFavorite(id);
        }
    }
}
