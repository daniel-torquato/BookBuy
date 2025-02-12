package xyz.torquato.bookbuy.ui.content.view.detailed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.GetSelectedItemUseCase;
import xyz.torquato.bookbuy.domain.PerformFavoriteUseCase;
import xyz.torquato.bookbuy.ui.content.view.detailed.model.DetailedItemUiState;

@HiltViewModel
public class DetailedItemViewModel extends ViewModel  {

    public LiveData<DetailedItemUiState> uiState;

    private final PerformFavoriteUseCase  performFavoriteUseCase;

    @Inject
    public DetailedItemViewModel(GetSelectedItemUseCase getSelectedItemUseCase, PerformFavoriteUseCase performFavoriteUseCase) {
        uiState = Transformations.map(getSelectedItemUseCase.__invoke__(), selectedItem ->
                new DetailedItemUiState(
                        selectedItem.id,
                        selectedItem.title,
                        selectedItem.author,
                        selectedItem.description,
                        selectedItem.smallThumbnailUrl,
                        selectedItem.largeThumbnailUrl,
                        selectedItem.buyLink
                )
        );
        this.performFavoriteUseCase = performFavoriteUseCase;
    }

    public void OnPerformFavorite(Boolean isFavorite) {
        performFavoriteUseCase.__invoker__(Objects.requireNonNull(uiState.getValue()).id, isFavorite);
    }

}
