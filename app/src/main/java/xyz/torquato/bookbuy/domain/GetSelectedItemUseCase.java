package xyz.torquato.bookbuy.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.BookRepository;
import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;
import xyz.torquato.bookbuy.data.selection.SelectionRepository;

public class GetSelectedItemUseCase {

    private final SelectionRepository selectionRepository;
    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;

    @Inject
    public GetSelectedItemUseCase(SelectionRepository selectionRepository, FavoriteRepository favoriteRepository, BookRepository bookRepository) {
        this.selectionRepository = selectionRepository;
        this.favoriteRepository = favoriteRepository;
        this.bookRepository = bookRepository;
    }

    public LiveData<SelectedBookItem> __invoke__() {
        return Transformations.switchMap(selectionRepository.selectedItemId, id ->
                {
                    if (id >= 0) {
                        return Transformations.map(bookRepository.items, bookItems -> {
                            String itemId = bookItems.get(id).id;
                            Boolean isFavorite = favoriteRepository.isFavorite(itemId);
                            return new SelectedBookItem(
                                    bookItems.get(id),
                                    isFavorite
                            );
                        });
                    } else {
                        return new MutableLiveData<>();
                    }
                }
        );
    }
}
