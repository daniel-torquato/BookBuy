package xyz.torquato.bookbuy.data.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import xyz.torquato.bookbuy.domain.search.QueryData;

@Singleton
public class SearchRepository {

    private final MutableLiveData<QueryData> _favoriteFiltered =  new MutableLiveData<>();
    public final LiveData<QueryData> favoriteFiltered =  _favoriteFiltered;

    @Inject
    SearchRepository() {}

    public void setSearch(QueryData query) {
        _favoriteFiltered.setValue(query);
    }

}
