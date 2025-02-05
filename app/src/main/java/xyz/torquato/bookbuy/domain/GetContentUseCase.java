package xyz.torquato.bookbuy.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class GetContentUseCase {

    public LiveData<List<BookItem>> __invoke__() {
        MutableLiveData<List<BookItem>> example = new MutableLiveData<>();
        example.setValue(List.of(
                new BookItem("Title", "Author", "Description"),
                new BookItem("Title3", "AuthorX", "DescriptionZ")
        ));
        return example;
    }
}
