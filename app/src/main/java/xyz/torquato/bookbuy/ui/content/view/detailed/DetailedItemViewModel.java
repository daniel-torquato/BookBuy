package xyz.torquato.bookbuy.ui.content.view.detailed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.GetSelectedItemUseCase;
import xyz.torquato.bookbuy.ui.content.view.detailed.model.DetailedItemUiState;

@HiltViewModel
public class DetailedItemViewModel extends ViewModel  {

    public LiveData<DetailedItemUiState> uiState;

    @Inject
    public DetailedItemViewModel(GetSelectedItemUseCase getSelectedItemUseCase) {
        uiState = Transformations.map(getSelectedItemUseCase.__invoke__(), selectedItem ->
                new DetailedItemUiState(selectedItem.title, selectedItem.author, selectedItem.description)
        );
    }


}
