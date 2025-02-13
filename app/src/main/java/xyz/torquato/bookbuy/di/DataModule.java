package xyz.torquato.bookbuy.di;

import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import xyz.torquato.bookbuy.concurrency.IOExecutor;
import xyz.torquato.bookbuy.data.BookDataSource;
import xyz.torquato.bookbuy.data.BookRepository;
import xyz.torquato.bookbuy.data.favorites.FavoriteRepository;
import xyz.torquato.bookbuy.data.favorites.FavoritesDao;
import xyz.torquato.bookbuy.data.favorites.FavoritesDatabase;
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

    @Provides
    public static FavoritesDatabase providesFavoritesDatabase(
            @ApplicationContext Context appContext
    ) {
        return Room.databaseBuilder(appContext,
                        FavoritesDatabase.class, "favorite-books")
                .build();
    }

    @Provides
    public static IOExecutor providesDBExecutor()  {
        return new IOExecutor(Executors.newFixedThreadPool(3));
    }

    @Provides
    public static FavoritesDao providesFavoritesDao(
            FavoritesDatabase favoritesDatabase
    ) {
        return favoritesDatabase.favoritesDao();
    }

    @Provides
    public static FavoriteRepository providesFavoriteRepository(
            FavoritesDao favoritesDao,
            IOExecutor executor
    ) {
        return new FavoriteRepository(favoritesDao, executor);
    }


    @Singleton
    @Provides
    public static BookDataSource providesBookDataSource() {
        return new BookDataSource();
    }

    @Provides
    public static BookRepository providesBookRepository(
            BookDataSource bookDataSource,
            IOExecutor executor
    ) {
        return new BookRepository(bookDataSource, executor);
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
            FavoriteRepository favoriteRepository,
            BookRepository bookRepository
    ) {
        return new GetSelectedItemUseCase(selectionRepository, favoriteRepository, bookRepository);
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

