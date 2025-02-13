package xyz.torquato.bookbuy.ui.view.detailed;

import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.selection.GetSelectedItemUseCase;
import xyz.torquato.bookbuy.domain.favorites.PerformFavoriteUseCase;
import xyz.torquato.bookbuy.ui.view.detailed.model.DetailedItemUiState;

@HiltViewModel
public class DetailedItemViewModel extends ViewModel  {

    public MutableLiveData<DetailedItemUiState> uiState = new MutableLiveData<>();

    private final PerformFavoriteUseCase  performFavoriteUseCase;

    @Inject
    public DetailedItemViewModel(GetSelectedItemUseCase getSelectedItemUseCase, PerformFavoriteUseCase performFavoriteUseCase) {
        getSelectedItemUseCase.__invoke__().observeForever(selectedItem -> uiState.setValue(
                new DetailedItemUiState(
                        selectedItem.item.id,
                        selectedItem.item.title,
                        selectedItem.item.author,
                        selectedItem.item.description,
                        selectedItem.item.smallThumbnailUrl,
                        selectedItem.item.largeThumbnailUrl,
                        selectedItem.item.hasBuyLink,
                        selectedItem.item.buyLink,
                        selectedItem.isFavorite
                )
        ));
        this.performFavoriteUseCase = performFavoriteUseCase;
    }

    public void OnPerformFavorite(Boolean isFavorite) {
        performFavoriteUseCase.__invoker__(Objects.requireNonNull(uiState.getValue()).id, isFavorite);
    }

    public Intent getBuyIntent() {
        Intent openBrowse = null;
        DetailedItemUiState currentUiState = uiState.getValue();
        if (currentUiState != null && currentUiState.hasBuyLink) {
            openBrowse = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(currentUiState.buyLink)
            );
        }
        return openBrowse;
    }
}
