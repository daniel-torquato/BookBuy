package xyz.torquato.bookbuy.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import xyz.torquato.bookbuy.data.BookDataSource;
import xyz.torquato.bookbuy.data.BookRepository;
import xyz.torquato.bookbuy.domain.GetContentUseCase;
import xyz.torquato.bookbuy.domain.SetQueryUseCase;
import xyz.torquato.bookbuy.ui.content.BookMenuViewModel;

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
    public static BookMenuViewModel provideBookMenuViewModel(
            GetContentUseCase getContentUseCase,
            SetQueryUseCase setQueryUseCase
    ) {
        return new BookMenuViewModel(getContentUseCase, setQueryUseCase);
    }


}

