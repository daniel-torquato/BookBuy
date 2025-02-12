package xyz.torquato.bookbuy.ui.content.view.detailed;

import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import xyz.torquato.bookbuy.domain.GetSelectedItemUseCase;
import xyz.torquato.bookbuy.domain.PerformFavoriteUseCase;
import xyz.torquato.bookbuy.ui.content.view.detailed.model.DetailedItemUiState;

@HiltViewModel
public class DetailedItemViewModel extends ViewModel  {

    public MutableLiveData<DetailedItemUiState> uiState = new MutableLiveData<>();

    private final PerformFavoriteUseCase  performFavoriteUseCase;

    @Inject
    public DetailedItemViewModel(GetSelectedItemUseCase getSelectedItemUseCase, PerformFavoriteUseCase performFavoriteUseCase) {
        getSelectedItemUseCase.__invoke__().observeForever(selectedItem -> uiState.setValue(
                new DetailedItemUiState(
                        selectedItem.id,
                        selectedItem.title,
                        selectedItem.author,
                        selectedItem.description,
                        selectedItem.smallThumbnailUrl,
                        selectedItem.largeThumbnailUrl,
                        selectedItem.hasBuyLink,
                        selectedItem.buyLink
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
