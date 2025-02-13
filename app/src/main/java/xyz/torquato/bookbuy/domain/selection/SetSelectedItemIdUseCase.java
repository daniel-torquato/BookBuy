package xyz.torquato.bookbuy.domain.selection;

import javax.inject.Inject;

import xyz.torquato.bookbuy.data.selection.SelectionRepository;

public class SetSelectedItemIdUseCase {

    private final SelectionRepository repository;

    @Inject
    public SetSelectedItemIdUseCase(SelectionRepository repository) {
        this.repository = repository;
    }

    public void __invoke__(Integer id) {
        repository.setSelectedItemId(id);
    }
}
