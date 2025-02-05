package xyz.torquato.bookbuy.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import xyz.torquato.bookbuy.data.BookRepository;

public class GetContentUseCase {

    private final BookRepository repository;

    public LiveData<List<BookItem>> __invoke__() {
        return repository.getItems();
    }

    public GetContentUseCase(BookRepository _repository) {
        repository = _repository;
    }
}
