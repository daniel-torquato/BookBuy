package xyz.torquato.bookbuy.ui.content.model;

import java.util.List;

import xyz.torquato.bookbuy.domain.BookItem;

public class BookMenuUiState {

    public List<BookItem> content;

    public BookMenuUiState(List<BookItem> _content) {
        content = _content;
    }


}
