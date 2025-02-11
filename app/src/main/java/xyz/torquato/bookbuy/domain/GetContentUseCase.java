package xyz.torquato.bookbuy.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.BookRepository;

public class GetContentUseCase {

    private final BookRepository repository;

    public LiveData<List<BookItem>> __invoke__() {
        return repository.items;
    }

    @Inject
    public GetContentUseCase(BookRepository repository) {
        this.repository = repository;
    }
}
