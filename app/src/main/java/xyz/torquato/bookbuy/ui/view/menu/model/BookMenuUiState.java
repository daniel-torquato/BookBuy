package xyz.torquato.bookbuy.ui.view.menu.model;

import java.util.List;

import xyz.torquato.bookbuy.domain.model.BookItem;

public class BookMenuUiState {

    public List<BookItem> content;

    public BookMenuUiState(List<BookItem> _content) {
        content = _content;
    }


}
