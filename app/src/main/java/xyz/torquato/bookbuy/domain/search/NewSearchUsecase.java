package xyz.torquato.bookbuy.domain.search;

import android.util.Range;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.data.books.BookRepository;
import xyz.torquato.bookbuy.data.search.SearchRepository;

@Singleton
public class NewSearchUsecase {

    private static final Range<Integer> DEFAULT_START_INDEX = new Range<>(0, 10);

    private final BookRepository repository;
    private final SearchRepository searchRepository;

    @Inject
    public NewSearchUsecase(BookRepository repository, SearchRepository searchRepository) {
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    public void __invoke__(String query) {
        QueryData lastSearch = searchRepository.favoriteFiltered.getValue();
        if (lastSearch != null) {
            if (!Objects.equals(query, lastSearch.query)) {
                repository.clean();
                performNewSearch(query);
            }
        } else {
            performNewSearch(query);
        }
    }

    private void performNewSearch(String query) {
        QueryData newSearch = new QueryData(query,
                DEFAULT_START_INDEX.getLower(),
                DEFAULT_START_INDEX.getUpper()
        );
        repository.setQuery(newSearch);
        searchRepository.setSearch(newSearch);
    }
}
