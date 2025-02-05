package xyz.torquato.bookbuy.ui.content;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import xyz.torquato.bookbuy.domain.GetContentUseCase;
import xyz.torquato.bookbuy.ui.content.model.BookMenuUiState;

public class BookMenuViewModel extends ViewModel {

    private final GetContentUseCase useCase = new GetContentUseCase();

    private final MutableLiveData<BookMenuUiState> bookMenuUiStateLiveData = new MutableLiveData<>();

    public LiveData<BookMenuUiState> bookMenu = bookMenuUiStateLiveData;

    public BookMenuViewModel() {
        bookMenuUiStateLiveData.setValue(
                new BookMenuUiState(
                        useCase.__invoke__().getValue()
                )
        );
    }

}
