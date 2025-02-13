package xyz.torquato.bookbuy.ui.view.menu;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.content.GetContentUseCase;
import xyz.torquato.bookbuy.domain.favorites.GetFavoriteFilterUseCase;
import xyz.torquato.bookbuy.domain.favorites.SetFavoriteFilterUseCase;
import xyz.torquato.bookbuy.domain.search.QueryData;
import xyz.torquato.bookbuy.domain.search.SetQueryUseCase;
import xyz.torquato.bookbuy.domain.selection.SetSelectedItemIdUseCase;
import xyz.torquato.bookbuy.ui.view.menu.model.BookMenuUiState;

@HiltViewModel
public class BookMenuViewModel extends ViewModel {

    public LiveData<BookMenuUiState> bookMenu;
    public LiveData<Boolean> isFiltered;

    private final SetQueryUseCase setContentUseCase;
    private final SetSelectedItemIdUseCase setSelectedItemIdUseCase;
    private final SetFavoriteFilterUseCase setFavoriteFilterUseCase;
    private final GetFavoriteFilterUseCase getFavoriteFilterUseCase;

    @Inject
    public BookMenuViewModel(GetContentUseCase getContentUseCase, SetQueryUseCase setQueryUseCase, SetSelectedItemIdUseCase setSelectedItemIdUseCase, SetFavoriteFilterUseCase setFavoriteFilterUseCase, GetFavoriteFilterUseCase getFavoriteFilterUseCase) {
        this.setContentUseCase = setQueryUseCase;
        this.setSelectedItemIdUseCase = setSelectedItemIdUseCase;
        bookMenu = Transformations.map(getContentUseCase.__invoke__(), bookItems -> {
            return new BookMenuUiState(bookItems);
        });
        this.setFavoriteFilterUseCase = setFavoriteFilterUseCase;
        this.getFavoriteFilterUseCase = getFavoriteFilterUseCase;
        isFiltered = getFavoriteFilterUseCase.__invoke__();
    }

    public void Search(
            String query,
            int startIndex,
            int maxResults
    ) {
        Log.d("MyTag", "Searching " + query);
        QueryData data = new QueryData(query, startIndex, maxResults);
        setContentUseCase.__invoke__(data);
    }

    public void OnSelect(
            Integer id
    ) {
        setSelectedItemIdUseCase.__invoke__(id);
    }

    void setFavoriteFilter(Boolean isFiltered) {
        setFavoriteFilterUseCase.__invoke__(isFiltered);
    }
}
