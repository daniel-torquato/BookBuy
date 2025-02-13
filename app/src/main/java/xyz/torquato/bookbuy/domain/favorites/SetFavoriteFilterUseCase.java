package xyz.torquato.bookbuy.domain.favorites;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;

@Singleton
public class SetFavoriteFilterUseCase {

    FavoriteRepository favoriteRepository;

    @Inject
    public SetFavoriteFilterUseCase(
            FavoriteRepository favoriteRepository
    ) {
        this.favoriteRepository = favoriteRepository;
    }

    public void __invoke__(Boolean isFiltered) {
        favoriteRepository.setFilter(isFiltered);
    }
}
