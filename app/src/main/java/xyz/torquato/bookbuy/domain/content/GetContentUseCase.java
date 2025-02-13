package xyz.torquato.bookbuy.domain.content;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.books.BookRepository;
import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;
import xyz.torquato.bookbuy.domain.model.BookItem;

public class GetContentUseCase {

    private final BookRepository bookRepository;
    private final FavoriteRepository favoriteRepository;

    public LiveData<List<BookItem>> __invoke__() {
        return Transformations.switchMap(favoriteRepository.favoriteFiltered, isFiltered -> {
            if (isFiltered) {
                return Transformations.switchMap(bookRepository.items, bookItems -> {
                    List<String> booksIds = bookItems.stream().map(item -> item.id).collect(Collectors.toList());
                    return Transformations.map(favoriteRepository.filterAll(booksIds),
                            filteredIds -> bookItems.stream().filter(
                                    item -> filteredIds.contains(item.id)
                            ).collect(Collectors.toList())
                    );
                });
            } else
                return bookRepository.items;
        });
    }

    @Inject
    public GetContentUseCase(BookRepository bookRepository, FavoriteRepository favoriteRepository) {
        this.bookRepository = bookRepository;
        this.favoriteRepository = favoriteRepository;
    }
}
