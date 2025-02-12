package xyz.torquato.bookbuy.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import xyz.torquato.bookbuy.data.BookDataSource;
import xyz.torquato.bookbuy.data.BookRepository;
import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;
import xyz.torquato.bookbuy.data.selection.SelectionRepository;
import xyz.torquato.bookbuy.domain.GetContentUseCase;
import xyz.torquato.bookbuy.domain.GetSelectedItemUseCase;
import xyz.torquato.bookbuy.domain.PerformFavoriteUseCase;
import xyz.torquato.bookbuy.domain.SetQueryUseCase;
import xyz.torquato.bookbuy.domain.SetSelectedItemIdUseCase;
import xyz.torquato.bookbuy.ui.content.BookMenuViewModel;
import xyz.torquato.bookbuy.ui.content.view.detailed.DetailedItemViewModel;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Singleton
    @Provides
    public static BookDataSource providesBookDataSource() {
        return new BookDataSource();
    }

    @Provides
    public static BookRepository providesBookRepository(
            BookDataSource bookDataSource
    ) {
        return new BookRepository(bookDataSource);
    }

    @Provides
    public static GetContentUseCase provideGetContentUseCase(
            BookRepository bookRepository
    ) {
        return new GetContentUseCase(bookRepository);
    }

    @Provides
    public static SetQueryUseCase provideSetQueryUseCase(
            BookRepository bookRepository
    ) {
        return new SetQueryUseCase(bookRepository);
    }

    @Provides
    public static SetSelectedItemIdUseCase provideSetSelectedItemUseCase(
            SelectionRepository selectionRepository
    ) {
        return new SetSelectedItemIdUseCase(selectionRepository);
    }

    @Provides
    public static GetSelectedItemUseCase provideGetSelectedItemUseCase(
            SelectionRepository selectionRepository,
            BookRepository bookRepository
    ) {
        return new GetSelectedItemUseCase(selectionRepository, bookRepository);
    }

    @Provides
    public static PerformFavoriteUseCase providePerformFavoriteUseCase(
            FavoriteRepository favoriteRepository
    ) {
        return new PerformFavoriteUseCase(favoriteRepository);
    }

    @Provides
    public static BookMenuViewModel provideBookMenuViewModel(
            GetContentUseCase getContentUseCase,
            SetQueryUseCase setQueryUseCase,
            SetSelectedItemIdUseCase setSelectedItemIdUseCase
    ) {
        return new BookMenuViewModel(getContentUseCase, setQueryUseCase, setSelectedItemIdUseCase);
    }

    @Provides
    public static DetailedItemViewModel provideDetailedItemViewModel(
            GetSelectedItemUseCase getSelectedItemUseCase,
            PerformFavoriteUseCase performFavoriteUseCase
    ) {
        return new DetailedItemViewModel(getSelectedItemUseCase, performFavoriteUseCase);
    }


}

