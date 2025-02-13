package xyz.torquato.bookbuy.domain.selection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.books.BookRepository;
import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;
import xyz.torquato.bookbuy.data.selection.SelectionRepository;
import xyz.torquato.bookbuy.domain.content.model.SelectedBookItem;

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
                        return Transformations.switchMap(bookRepository.items, bookItems -> {
                            String itemId = bookItems.get(id).id;
                            return Transformations.map(favoriteRepository.isFavorite(itemId), isFavorite ->
                                    new SelectedBookItem(
                                            bookItems.get(id),
                                            isFavorite
                                    )
                            );
                        });
                    } else {
                        return new MutableLiveData<>();
                    }
                }
        );
    }
}
