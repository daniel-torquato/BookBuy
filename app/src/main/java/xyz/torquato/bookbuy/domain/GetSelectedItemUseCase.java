package xyz.torquato.bookbuy.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.BookRepository;
import xyz.torquato.bookbuy.data.selection.SelectionRepository;

public class GetSelectedItemUseCase {

    private final SelectionRepository selectionRepository;

    private final BookRepository bookRepository;

    @Inject
    public GetSelectedItemUseCase(SelectionRepository selectionRepository, BookRepository bookRepository) {
        this.selectionRepository = selectionRepository;
        this.bookRepository = bookRepository;
    }

    public LiveData<BookItem> __invoke__() {
        return Transformations.switchMap(selectionRepository.selectedItemId, id ->
                {
                    if (id >= 0) {
                        return Transformations.map(bookRepository.items, bookItems -> bookItems.get(id));
                    } else {
                        return new MutableLiveData<>();
                    }
                }
        );
    }
}
