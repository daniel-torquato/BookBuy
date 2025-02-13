package xyz.torquato.bookbuy.domain.search;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.data.books.BookRepository;
import xyz.torquato.bookbuy.data.search.SearchRepository;

@Singleton
public class IncreaseSearchUseCase {

    private static final int DEFAULT_SEARCH_INCREMENT = 5;

    private final BookRepository bookRepository;
    private final SearchRepository searchRepository;

    @Inject
    public IncreaseSearchUseCase(
            BookRepository bookRepository,
            SearchRepository searchRepository
    ) {
        this.bookRepository = bookRepository;
        this.searchRepository = searchRepository;
    }

    public void __invoke__() {
        QueryData lastSearch = searchRepository.favoriteFiltered.getValue();
        if (lastSearch != null) {
            QueryData newSearch = new QueryData(
                    lastSearch.query,
                    lastSearch.lastIndex + 1,
                    lastSearch.lastIndex + DEFAULT_SEARCH_INCREMENT
            );
            searchRepository.setSearch(newSearch);
            bookRepository.setQuery(newSearch);
        }
    }


}
