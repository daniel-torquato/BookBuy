package xyz.torquato.bookbuy.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import xyz.torquato.bookbuy.data.BookRepository;
import xyz.torquato.bookbuy.domain.GetContentUseCase;
import xyz.torquato.bookbuy.ui.content.BookMenuViewModel;

@Module
@InstallIn(FragmentComponent.class)
public class DataModule {

    @Provides
    public static BookRepository providesBookRepository() {
        return new BookRepository();
    }

    @Provides
    public static GetContentUseCase provideGetContentUseCase(
            BookRepository bookRepository
    ) {
        return new GetContentUseCase(bookRepository);
    }

    @Provides
    public static BookMenuViewModel provideBookMenuViewModel(
            GetContentUseCase useCase
    ) {
        return new BookMenuViewModel(useCase);
    }


}

