package xyz.torquato.bookbuy.domain;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.BookRepository;

public class SetQueryUseCase {

    private final BookRepository repository;

    @Inject
    public SetQueryUseCase(BookRepository repository) {
        this.repository = repository;
    }

    public void __invoke__(QueryData data) {
        repository.setQuery(data);
    }
}
