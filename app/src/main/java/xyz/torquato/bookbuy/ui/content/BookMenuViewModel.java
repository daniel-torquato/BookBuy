package xyz.torquato.bookbuy.ui.content;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.GetContentUseCase;
import xyz.torquato.bookbuy.domain.QueryData;
import xyz.torquato.bookbuy.domain.SetQueryUseCase;
import xyz.torquato.bookbuy.ui.content.model.BookMenuUiState;

@HiltViewModel
public class BookMenuViewModel extends ViewModel {

    public LiveData<BookMenuUiState> bookMenu;

    private final SetQueryUseCase setContentUseCase;

    @Inject
    public BookMenuViewModel(GetContentUseCase getContentUseCase, SetQueryUseCase setQueryUseCase) {
        this.setContentUseCase = setQueryUseCase;
        bookMenu = Transformations.map(getContentUseCase.__invoke__(), bookItems -> {
            return new BookMenuUiState(bookItems);
        });
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
}
