package xyz.torquato.bookbuy.ui.content;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.GetContentUseCase;
import xyz.torquato.bookbuy.ui.content.model.BookMenuUiState;

@HiltViewModel
public class BookMenuViewModel extends ViewModel {

    private final MutableLiveData<BookMenuUiState> bookMenuUiStateLiveData = new MutableLiveData<>();

    public LiveData<BookMenuUiState> bookMenu = bookMenuUiStateLiveData;

    @Inject
    public BookMenuViewModel(GetContentUseCase useCase) {
        bookMenuUiStateLiveData.setValue(
                new BookMenuUiState(
                        useCase.__invoke__().getValue()
                )
        );
    }

}
